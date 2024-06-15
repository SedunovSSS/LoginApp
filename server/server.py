from flask import Flask, request, redirect, render_template, make_response
from flask_sqlalchemy import SQLAlchemy
import requests

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///database.db"
db = SQLAlchemy(app)

HOST = '0.0.0.0'
PORT = 1000

class Users(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    login = db.Column(db.String(150), nullable=False, unique=True)
    password = db.Column(db.String(150), nullable=False)
    isSuperUser = db.Column(db.Boolean())

    def __repr__(self):
        return '<Users %r>' % self.id
    

@app.route('/registration', methods=['POST'])
def registration():
    login = request.form['login'].lower()
    password = request.form['password']
    if login == '':
        return 'login is empty'
    if ' ' in login:
        return 'spaces in login'
    if len(login) > 32:
        return 'length login > 32'
    if not login.isascii():
        return 'login exist special characters'
    exists = db.session.query(Users.id).filter_by(login=login).first() is not None
    if exists:
        return 'user already exists'
    if len(password) < 5:
        return 'length password < 5'
    if len(password) > 32:
        return 'length password > 32'
    isSuperUser = Users.query.all() == []
    user = Users(login=login, password=password, isSuperUser=isSuperUser)
    try:
        db.session.add(user)
        db.session.commit()
        return 'success'
    except:
        return 'error'
    

@app.route('/login', methods=['POST'])
def login():
    login = request.form['login'].lower()
    password = request.form['password']
    exists = db.session.query(Users.id).filter_by(login=login).first() is not None
    if not exists:
        return 'user not found'
    user = Users.query.filter_by(login=login).first()
    if user.password != password:
        return 'incorrect password'
    return 'success'


@app.route('/delete_user', methods=['POST'])
def delete_user():
    login = request.form['login'].lower()
    password = request.form['password']
    exists = db.session.query(Users.id).filter_by(login=login).first() is not None
    if not exists:
        return 'user not found'
    user = Users.query.filter_by(login=login).first()
    if user.password != password:
        return 'incorrect password'
    try:
        db.session.delete(user)
        db.session.commit()
        return 'success'
    except:
        return 'error'


@app.route('/change_password', methods=['POST'])
def change_password():
    login = request.form['login'].lower()
    password = request.form['password']
    new_password = request.form['new_password']
    exists = db.session.query(Users.id).filter_by(login=login).first() is not None
    if not exists:
        return 'user not found'
    user = Users.query.filter_by(login=login).first()
    if user.password != password:
        return 'incorrect password'
    if len(new_password) < 5:
        return 'length new password < 5'
    if len(new_password) > 32:
        return 'length new password > 32'
    try:
        user.password = new_password
        db.session.commit()
        return 'success'
    except:
        return 'error'
    

@app.route('/is_super_user', methods=["POST"])
def is_super_user():
    login = request.form['login'].lower()
    exists = db.session.query(Users.id).filter_by(login=login).first() is not None
    if not exists:
        return 'user not found'
    user = Users.query.filter_by(login=login).first()
    return 'yes' if user.isSuperUser else 'no'

if __name__ == '__main__':
    app.run(host=HOST, port=PORT, debug=True)
