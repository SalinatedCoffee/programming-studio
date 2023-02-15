import unittest
import json
import book as bk
import author as at
import db
from pymongo import MongoClient

class TestDBOperations(unittest.TestCase):
    def test_write_book(self):
        book = bk.Book()
        book.book_url = 'google.com'
        book.book_id = '13579'
        book.review_count = 1337
        book.similar_books = ['Bing', 'DuckDuckGo']
        client = MongoClient('localhost', 27017)

        db.write_books([book], client)

        book_db = client.mp2.books.find_one({"book_id":"13579"})

        self.assertEqual(book_db['book_url'], 'google.com')
        self.assertEqual(book_db['review_count'], 1337)
        self.assertEqual(len(book_db['similar_books']), 2)

        client.close()



    def test_write_author(self):
        author = at.Author()
        author.name = 'Google'
        author.author_url = 'google.com'
        author.author_id = '97531'
        author.review_count = 1337
        client = MongoClient('localhost', 27017)

        db.write_authors([author], client)

        author_db = client.mp2.authors.find_one({"author_id":"97531"})

        self.assertEqual(author_db['name'], 'Google')
        self.assertEqual(author_db['author_url'], 'google.com')
        self.assertEqual(author_db['review_count'], 1337)

        client.close()