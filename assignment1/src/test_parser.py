import unittest
import parser
import json
import book as bk
import author as at

class TestJSONOperations(unittest.TestCase):
    def test_write_book(self):
        book = bk.Book()
        book.title = "JSON Write Test"
        book.book_id = "2468"
        book.rating = 4.5

        parser.write_books([book])

        f = open('../json/books.json', 'r')
        
        for obj in f:
            book_recovered = json.loads(obj, object_hook=bk.book_decoder)

        self.assertEqual(book_recovered.title, "JSON Write Test")
        self.assertEqual(book_recovered.book_id, "2468")
        self.assertEqual(book_recovered.rating, 4.5)

        f.close()

    def test_write_author(self):
        author = at.Author()
        author.name = "JSON Write Test"
        author.author_id = "1357"
        author.rating = 4.5

        parser.write_authors([author])

        f = open('../json/authors.json', 'r')

        for obj in f:
            author_recovered = json.loads(obj, object_hook=at.author_decoder)

        self.assertEqual(author_recovered.name, "JSON Write Test")
        self.assertEqual(author_recovered.author_id, "1357")
        self.assertEqual(author_recovered.rating, 4.5)

        f.close()