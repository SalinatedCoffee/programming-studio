"""Contains methods that reads / writes from a local MongoDB instance."""
import json
import os
from dotenv import load_dotenv
from pymongo import MongoClient
import book as bk
import author as at


def write_books(books, client):
    """Write book objects in books into database."""
    database = client.mp2
    collection = database.books

    for book in books:
        book_json = json.dumps(book, cls=bk.BookEncoder)
        collection.insert_one(json.loads(book_json))

def write_authors(authors, client):
    """Write author objects in authors into database."""
    database = client.mp2
    collection = database.authors

    for author in authors:
        author_json = json.dumps(author, cls=at.AuthorEncoder)
        collection.insert_one(json.loads(author_json))

def read_books(client):
    """Load all book documents from database into memory."""
    books = []
    database = client.mp2
    collection = database.books

    for book in collection.find():
        book_obj = bk.book_decoder(book)
        books.append(book_obj)

    return books

def read_authors(client):
    """Load all author documents from database into memory."""
    authors = []
    database = client.mp2
    collection = database.authors

    for author in collection.find():
        author_obj = at.author_decoder(author)
        authors.append(author_obj)

    return authors

def empty_db(client):
    """Remove all documents in both books and authors collections in database."""
    database = client.mp2
    collection_books = database.books
    collection_authors = database.authors

    res = collection_books.delete_many({})
    print('Deleted ' + str(res.deleted_count) + ' book documents.')
    res = collection_authors.delete_many({})
    print('Deleted ' + str(res.deleted_count) + ' author documents.')

def query_books(query):
    """Given a dictionary representation of the search query, search and return relevant
    books from the database.
    """
    load_dotenv()
    client = MongoClient(os.getenv('ATLAS_STRING'))
    keys = list(query)
    if len(query[keys[0]]) == 1:
        query_dict = {keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}}
    elif len(query[keys[0]]) == 2 and 'or' in query:
        query_dict = {'$or':[{keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}},
                             {keys[0]:{'$regex':query[keys[0]][1], '$options':'i'}}]}
    elif len(query[keys[0]]) == 2 and 'and' in query:
        query_dict = {'$and':[{keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}},
                              {keys[0]:{'$regex':query[keys[0]][1], '$options':'i'}}]}

    res = []
    for book in client.mp2.books.find(query_dict):
        res.append(book)
    client.close()
    return res

def delete_book(query):
    """Given a dictionary representation of the search query, search and delete the first
    book from the database.
    """
    load_dotenv()
    client = MongoClient(os.getenv('ATLAS_STRING'))
    keys = list(query)
    res = client.mp2.books.delete_one({keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}})
    client.close()
    return str(res.deleted_count)

def update_book(data, query):
    """Given dictionary representations of the data and search query, search and update the
    first book from the database with the data.
    """
    load_dotenv()
    client = MongoClient(os.getenv('ATLAS_STRING'))
    data_keys = list(data)
    query_keys = list(query)
    update_dict = {'$set':{data_keys[0]:data[data_keys[0]]}}
    query_dict = {query_keys[0]:{'$regex':query[query_keys[0]][0], '$options':'i'}}
    res = client.mp2.books.update_one(query_dict, update_dict)
    client.close()
    return str(res.modified_count)


def query_authors(query):
    """Given a dictionary representation of the search query, search and return relevant
    authors from the database.
    """
    load_dotenv()
    client = MongoClient(os.getenv('ATLAS_STRING'))
    keys = list(query)
    if len(query[keys[0]]) == 1:
        query_dict = {keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}}
    elif len(query[keys[0]]) == 2 and 'or' in query:
        query_dict = {'$or':[{keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}},
                             {keys[0]:{'$regex':query[keys[0]][1], '$options':'i'}}]}
    elif len(query[keys[0]]) == 2 and 'and' in query:
        query_dict = {'$and':[{keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}},
                              {keys[0]:{'$regex':query[keys[0]][1], '$options':'i'}}]}

    res = []
    for book in client.mp2.authors.find(query_dict):
        res.append(book)
    client.close()
    return res

def delete_author(query):
    """Given a dictionary representation of the search query, search and delete the first
    author from the database.
    """
    load_dotenv()
    client = MongoClient(os.getenv('ATLAS_STRING'))
    keys = list(query)
    res = client.mp2.authors.delete_one({keys[0]:{'$regex':query[keys[0]][0], '$options':'i'}})
    client.close()
    return str(res.deleted_count)

def update_author(data, query):
    """Given dictionary representations of the data and search query, search and update the
    first author from the database with the data.
    """
    load_dotenv()
    client = MongoClient(os.getenv('ATLAS_STRING'))
    data_keys = list(data)
    query_keys = list(query)
    update_dict = {'$set':{data_keys[0]:data[data_keys[0]]}}
    query_dict = {query_keys[0]:{'$regex':query[query_keys[0]][0], '$options':'i'}}
    res = client.mp2.authors.update_one(query_dict, update_dict)
    client.close()
    return str(res.modified_count)
