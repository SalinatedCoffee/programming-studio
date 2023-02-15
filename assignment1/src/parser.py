"""Contains methods that reads / writes Book and Author objects from / into JSON."""
import json
import book as bk
import author as at


def write_books(books):
    """Given a list of books, serialize Book objects into JSON
    and write into ../json/books.json.
    """
    json_f = open('../json/books.json', 'w')

    for book in books:
        book_json = json.dumps(book, cls=bk.BookEncoder)
        json_f.write(book_json + '\n')

    json_f.close()

def write_authors(authors):
    """Given a list of authors, serialize Author objects into JSON
    and write into ../json/authors.json.
    """
    json_f = open('../json/authors.json', 'w')

    for author in authors:
        author_json = json.dumps(author, cls=at.AuthorEncoder)
        json_f.write(author_json + '\n')

    json_f.close()

def read_books():
    """Deserializes JSON objects in ../json/books.json
    and returns the decoded list of Books.
    """
    json_f = open('../json/books.json', 'r')
    books = []

    for json_obj in json_f:
        books.append(json.loads(json_obj, object_hook=bk.book_decoder))
    json_f.close()

    return books

def read_authors():
    """Deserialized JSON objects in ../json/authors.json
    and returns the decoded list of Authors.
    """
    json_f = open('../json/authors.json', 'r')
    authors = []

    for json_obj in json_f:
        authors.append(json.loads(json_obj, object_hook=at.author_decoder))
    json_f.close()

    return authors
