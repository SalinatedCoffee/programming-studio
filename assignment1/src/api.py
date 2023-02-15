"""Module that contains various API-related methods."""
from flask import Flask
from flask import request
from flask import Response
import db

app = Flask(__name__)

def enforce_content_type(header):
    """Check header for valid Content-Type."""
    return header == 'application/json'

@app.route('/')
def landing_page():
    """Placeholder for root directory."""
    return 'Welcome'

@app.route('/api/books', methods=['GET', 'DELETE', 'PUT', 'POST'])
def api_books():
    """Handles book API requests."""
    query_params = request.args.to_dict(flat=False)

    if request.method == 'GET':
        res = api_books_get(query_params)
        return str(res)
    if request.method == 'DELETE':
        res = api_books_delete(query_params)
        return str(res)
    if request.method == 'PUT':
        if enforce_content_type(request.content_type) is True:
            res = api_books_put(request.json, query_params)
            return res
        else:
            return Response('{\'message\':\'Got POST request with invalid Content-Type header\'}',
                            status=415, mimetype='application/json')
    if request.method == 'POST':
        if enforce_content_type(request.content_type) is True:
            api_books_post(request.form, query_params)
        else:
            return Response('{\'message\':\'Got POST request with invalid Content-Type header\'}',
                            status=415, mimetype='application/json')
    return None

def api_books_get(query):
    """Wrapper for GET requests."""
    res = db.query_books(query)
    return res

def api_books_delete(query):
    """Wrapper for DELETE requests."""
    res = db.delete_book(query)
    return res

def api_books_put(data, query):
    """Wrapper for PUT requests."""
    res = db.update_book(data, query)
    return res

def api_books_post(data, query):
    """Wrapper for POST requests."""
    return None

@app.route('/api/authors', methods=['GET', 'DELETE', 'PUT', 'POST'])
def api_authors():
    """Handles author API requests."""
    query_params = request.args.to_dict(flat=False)
    if request.method == 'GET':
        res = api_authors_get(query_params)
        return res
    elif request.metnod == 'DELETE':
        res = api_authors_delete(query_params)
        return res
    elif request.method == 'PUT':
        if enforce_content_type(request.content_type) is True:
            res = api_authors_put(request.json, query_params)
            return res
        else:
            return Response('{\'message\':\'Got POST request with invalid Content-Type header\'}',
                            status=415, mimetype='application/json')
    elif request.method == 'POST':
        if enforce_content_type(request.content_type) is True:
            api_authors_post()
        else:
            return Response('{\'message\':\'Got POST request with invalid Content-Type header\'}',
                            status=415, mimetype='application/json')
    return None

def api_authors_get(query):
    """Wrapper for GET requests."""
    res = db.query_authors(query)
    return res

def api_authors_delete(query):
    """Wrapper for DELETE requests."""
    res = db.delete_author(query)
    return res

def api_authors_put(data, query):
    """Wrapper for PUT requests."""
    res = db.update_author(data, query)
    return res

def api_authors_post():
    """Wrapper for POST requests."""
    return None
