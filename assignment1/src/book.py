"""Object that holds information about a book."""
import json


class Book:
    def __init__(self):
        self.book_url = ''
        self.title = ''
        self.book_id = ''
        self.isbn = ''
        self.author_url = ''
        self.author = ''
        self.rating = 0.0
        self.rating_count = 0
        self.review_count = 0
        self.image_url = ''
        self.similar_books = []

    def __str__(self):
        res = str('Title: ' + self.title + '\n'
                  + 'Book URL: ' + self.book_url + '\n'
                  + 'Book Cover URL: ' + self.image_url + '\n'
                  + 'Author: ' + self.author + '\n'
                  + 'Author URL: ' + self.author_url + '\n'
                  + 'Book ID: ' + self.book_id + ", ISBN: " + self.isbn
                  + ", Rating: " + str(self.rating)
                  + ", Rating Count: " + str(self.rating_count)
                  + ", Review Count: " + str(self.review_count) + '\n'
                  + str(len(self.similar_books)) + ' similar books')

        return res

def book_decoder(obj):
    """Custom JSON deserializer."""
    book = Book()
    book.book_url = obj['book_url']
    book.title = obj['title']
    book.book_id = obj['book_id']
    book.isbn = obj['isbn']
    book.author_url = obj['author_url']
    book.author = obj['author']
    book.rating = float(obj['rating'])
    book.rating_count = int(obj['rating_count'])
    book.review_count = int(obj['review_count'])
    book.image_url = obj['image_url']
    book.similar_books = obj['similar_books']

    return book

class BookEncoder(json.JSONEncoder):
    """Custom JSON serializer class."""
    def default(self, obj):
        """Default serializer method."""
        if isinstance(obj, Book):
            return {"book_url" : obj.book_url,
                    "title" : obj.title,
                    "book_id" : obj.book_id,
                    "isbn" : obj.isbn,
                    "author_url" : obj.author_url,
                    "author" : obj.author,
                    "rating" : obj.rating,
                    "rating_count" : obj.rating_count,
                    "review_count" : obj.review_count,
                    "image_url" : obj.image_url,
                    "similar_books" : obj.similar_books}

        return json.JSONEncoder.default(self, obj)
