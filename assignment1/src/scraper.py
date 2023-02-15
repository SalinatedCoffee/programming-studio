"""Contains various methods related to web scraping."""
from collections import deque
import re
import time
import random
import book as bk
import author as at

CRAWL_DELAY_MIN = 2 # wait time before requesting a new page
CRAWL_DELAY_MAX = 7
CRAWL_COOLDOWN = 60
BOOK_URL_PREFIX = 'https://www.goodreads.com/book/show/'
AUTHOR_URL_PREFIX = 'https://www.goodreads.com/author/show/'

known_books = set()
known_authors = set()
queued_books = deque()
queued_authors = deque()

def scrape_web(books, authors, start_book_id, browser, book_num, author_num):
    """Scrapes book_num books and author_num authors."""
    queued_books.append(start_book_id)
    while len(books) < book_num:
        book_id = queued_books.pop()
        book_bs4 = request_page(str(BOOK_URL_PREFIX + book_id), browser)
        book_obj = get_book_info(book_bs4)
        if book_obj is not None:
            books.append(book_obj)

    author_bs4 = request_page(books[0].author_url, browser)
    authors.append(get_author_info(author_bs4, browser))

    while len(authors) < author_num:
        author_id = queued_authors.pop()
        author_bs4 = request_page(str(AUTHOR_URL_PREFIX + author_id), browser)
        author_obj = get_author_info(author_bs4, browser)
        if author_obj is not None:
            authors.append(author_obj)

def get_book_info(book_bs4):
    """Given a BeautifulSoup object of a book page,
    creates a Book object and populates its variables.
    Returns the created Book object.
    """
    # If entry already exists for book, return None.
    book_prep = book_bs4.body
    if book_prep is None:
        return None

    book_prep = book_prep.find(class_='wtrUp wtrLeft')
    if book_prep is None:
        return None

    book_id = book_prep.form.find(id='book_id')['value']
    if book_id in known_books:
        return None

    known_books.add(book_id)
    book_obj = bk.Book()
    book_obj.book_id = book_id

    book_obj.book_url = book_bs4.head.link['href']
    book_obj.isbn = book_bs4.head.find(property='books:isbn')['content']
    book_obj.title = book_bs4.body.find(id='bookTitle').string.strip()
    book_prep = book_bs4.body.find(id='coverImage')
    if book_prep is None:
        book_obj.image_url = 'null'
    else:
        book_obj.image_url = book_prep['src']

    book_meta = book_bs4.body.find(id="bookMeta")
    book_obj.rating = float(book_meta.find(itemprop='ratingValue').string)
    book_obj.rating_count = int(book_meta.find(itemprop='ratingCount')['content'])
    book_obj.review_count = int(book_meta.find(itemprop='reviewCount')['content'])

    author_info = book_bs4.body.find(class_='authorName')
    book_obj.author_url = author_info['href']
    book_obj.author = author_info.span.string

    similar = book_bs4.body.find(class_='carouselRow').ul.find_all('li')
    similar_titles = []
    for book in similar:
        similar_book_id = book['id'][10:]
        queued_books.append(similar_book_id)
        similar_book_title = book.a.img['alt']
        similar_titles.append(similar_book_title)

    book_obj.similar_books = similar_titles

    return book_obj

def get_author_info(author_bs4, browser):
    """Given a BeautifulSoup object of an author page,
    creates an Author object and populates its variables.
    A mechanicalsoup.StatefulBrowser object is required for webpage traversal.
    Returns the created Author object.
    """
    raw_url = author_bs4.head.link['href']
    author_id = re.search('show/[0-9]*', raw_url).group(0)[5:]

    # If entry already exists for author, return None.
    if author_id in known_authors:
        return None

    known_authors.add(author_id)
    author_obj = at.Author()
    author_obj.author_id = author_id
    author_obj.author_url = raw_url

    author_obj.name = author_bs4.body.find(class_='authorName').span.string

    author_prep = author_bs4.body \
                           .find(class_='leftContainer authorLeftContainer').find('img')
    if author_prep is None:
        author_obj.image_url = 'null'
    else:
        author_obj.image_url = author_prep['src']

    author_meta = author_bs4.body.find(class_='hreview-aggregate')
    author_obj.rating = float(author_meta.find(class_='rating').span.string)
    author_obj.rating_count = int(author_meta.find(class_='votes').span['content'])
    author_obj.review_count = int(author_meta.find(class_='count').span['content'])

    # Get links for similar authors / books by author
    links = author_meta.find_all('a')
    prefix = 'https://www.goodreads.com'
    books_by_author = str(prefix + links[0]['href'])
    similar_authors = str(prefix + links[1]['href'])
    author_obj.author_books = get_books_by_author(books_by_author, browser)
    author_obj.related_authors = get_similar_authors(similar_authors, browser)

    return author_obj

def get_books_by_author(url, browser):
    """Given an URL of an author's books page, scrapes the titles of the publications.
    A mechanicalsoup.StatefulBrowser object is required for webpage traversal.
    Returns a list of the title of books written by an author.
    """
    author_books_bs = request_page(url, browser)
    book_titles = []
    next_page = author_books_bs.find(class_='next_page')

    while True:
        books = author_books_bs.find_all(class_='bookTitle')
        for book in books:
            # book_url = book['href']
            # book_id = re.search('show/[0-9]*', book_url).group(0)[5:]
            book_titles.append(book.span.string)

        try:    # attempt to construct url of next page
            next_page_url = 'https://www.goodreads.com' + next_page['href']
        except: # if failed, the current page is the last page
            break

        author_books_bs = request_page(next_page_url, browser)
        next_page = author_books_bs.find(class_='next_page')

    return book_titles

def get_similar_authors(url, browser):
    """Given an URL of an author's similar authors page, scrapes the names of relevant authors.
    A mechanicalsoup.StatefulBrowser object is required for webpage traversal.
    Returns a list of the names of authors similar to an author.
    """
    author_similar_bs = request_page(url, browser)
    author_names = []

    authors = author_similar_bs.body.find_all(class_='responsiveAuthor')[1:]
    for author in authors:
        author = author.find(class_='objectLockupPrimaryContent__main').a
        author_id = re.search('show/[0-9]*.', author['href']).group(0)[5:-1]
        queued_authors.append(author_id)
        author_name = author.span.string
        author_names.append(author_name)

    return author_names

def request_page(url, browser):
    """Given an URL of a webpage, announces the webpage to be requseted,
    waits CRAWL_DELAY_SEC, and requests the page.
    A mechanicalsoup.StatefulBrowser object is required for link traversal.
    Returns a BeautifulSoup object of the requested webpage at the given URL.
    """
    delay_by = random.randint(CRAWL_DELAY_MIN, CRAWL_DELAY_MAX)
    print('Waiting ' + str(delay_by) + \
          ' second(s) before requesting page at [' + url + ']...')
    time.sleep(delay_by)
    response = browser.open(url)
    print(response)

    if response.status_code == 403:
        print('Got response of 403 forbidden. Resting for ' + str(CRAWL_COOLDOWN) + ' seconds.')
        time.sleep(CRAWL_COOLDOWN)
        print(browser.open(url))

    res = browser.get_current_page()

    return res
