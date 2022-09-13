from flask import Flask,request
from flask_cors import CORS


app = Flask(__name__,static_url_path='/static')
CORS(app)

from superai import routes
