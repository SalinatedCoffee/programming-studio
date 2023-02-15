"""Main script that handles argument parsing and orchestrates data flow."""
import os
import argparse
import sys
import parser
from pymongo import MongoClient
from dotenv import load_dotenv
import mechanicalsoup
import scraper
import db


# Parse command line arguments
argparser = argparse.ArgumentParser(description='Crawls the website goodreads.com.')
argparser.add_argument('book_id', type=str, help='ID of the book from which to start crawling')
argparser.add_argument('book_num', type=int, help='number of books to crawl.')
argparser.add_argument('author_num', type=int, help='number of authors to crawl')
argparser.add_argument('--noscrape', action='store_true', help='skip web scraping')
argparser.add_argument('--load', type=str,
                       help='source (memory, json, db) from which to load entries')
argparser.add_argument('--save', type=str,
                       help='destination (memory, json, db) to which to save entries')
argparser.add_argument('--purgedb', action='store_true', help='empty database')
arguments = argparser.parse_args()



# load environment variables
load_dotenv()

client = MongoClient(os.getenv('ATLAS_STRING'))

books = []
authors = []
if arguments.noscrape is False:
    # Initialize StatefulBrowser object with useragent Chrome running on macOS
    browser = mechanicalsoup.StatefulBrowser(
        soup_config={'features':'lxml'},
        raise_on_404=True,
        user_agent='Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36')
    scraper.scrape_web(books, authors, arguments.book_id, browser,
                       arguments.book_num, arguments.author_num)
    print('Scraped ' + str(len(books)) + ' books and ' + str(len(authors)) + ' authors.')
    browser.close()

if arguments.purgedb is True:
    db.empty_db(client)
    client.close()
    sys.exit()


if arguments.load == 'json':
    print('Loading entries from JSON...')
    books = parser.read_books()
    authors = parser.read_authors()
    print('Loaded ' + str(len(books)) + ' books and '
          + str(len(authors)) + ' authors from JSON.')
elif arguments.load == 'db':
    print('Loading entries from database...')
    books = db.read_books(client)
    authors = db.read_authors(client)
    print('Loaded ' + str(len(books)) + ' books and '
          + str(len(authors)) + ' authors from database.')
elif arguments.load is not None:
    print('Unknown source (' + arguments.load + ') was specified.')
    client.close()
    sys.exit()

if arguments.save == 'json':
    parser.write_books(books)
    parser.write_authors(authors)
elif arguments.save == 'db':
    db.write_books(books, client)
    db.write_authors(authors, client)
elif arguments.save is not None:
    print('Unknown destination (' + arguments.save + ') was specified.')
    client.close()
    sys.exit()

print('Exiting with ' + str(len(books)) + ' books and '
      + str(len(authors)) + ' authors in memory.')

client.close()
