"""Object that holds information about an author."""
import json


class Author:
    def __init__(self):
        self.name = ''
        self.author_url = ''
        self.author_id = ''
        self.rating = 0.0
        self.rating_count = 0
        self.review_count = 0
        self.image_url = ''
        self.related_authors = []
        self.author_books = []

    def __str__(self):
        res = str('Name: ' + self.name + '\n'
                  + 'Author URL: ' + self.author_url + '\n'
                  + 'Author Photo URL: ' + self.image_url + '\n'
                  + 'Author ID: ' + self.author_id + ', Rating: ' + str(self.rating)
                  + ', Rating Count: ' + str(self.rating_count)
                  + ', Review Count: ' + str(self.review_count) + '\n'
                  + str(len(self.related_authors)) + ' related authors, '
                  + str(len(self.author_books)) + ' authored books')

        return res

def author_decoder(obj):
    """Custom JSON deserializer."""
    author = Author()
    author.name = obj['name']
    author.author_url = obj['author_url']
    author.author_id = obj['author_id']
    author.rating = float(obj['rating'])
    author.rating_count = int(obj['rating_count'])
    author.review_count = int(obj['review_count'])
    author.image_url = obj['image_url']
    author.related_authors = obj['related_authors']
    author.author_books = obj['author_books']

    return author

class AuthorEncoder(json.JSONEncoder):
    """Custom JSON serializer."""
    def default(self, obj):
        """Default serializer method."""
        if isinstance(obj, Author):
            return {"name" : obj.name,
                    "author_url" : obj.author_url,
                    "author_id" : obj.author_id,
                    "rating" : obj.rating,
                    "rating_count" : obj.rating_count,
                    "review_count" : obj.review_count,
                    "image_url" : obj.image_url,
                    "related_authors" : obj.related_authors,
                    "author_books" : obj.author_books}

        return json.JSONEncoder.default(self, obj)
