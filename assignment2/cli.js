#!/usr/bin/env node
const { ArgumentParser } = require('argparse');
global.fetch = require('node-fetch');
const OAuth = require('./oauth');
const getUserByLogin = OAuth.getUserByLogin;
const getReposByLogin = OAuth.getReposByLogin;
const getFollowingByLogin = OAuth.getFollowingByLogin;
const getFollowersByLogin = OAuth.getFollowersByLogin;

const parser = new ArgumentParser({
    description: 'Fetches and displays various information about a GitHub user.'
});

parser.add_argument('-l', '--login', {help: 'User\'s GitHub login'});
parser.add_argument('-g', '--following', {
    help: 'Display the list of users followed by the user with specified login',
    action: 'store_true'
});
parser.add_argument('-s', '--followers', {
    help: 'Display the list of users following the user with specified login',
    action: 'store_true'
});
parser.add_argument('-r', '--repositories', {
    help: 'Display the list of repositories owned by the user with specified login',
    action: 'store_true'
});
parser.add_argument('-p', '--profile', {
    help: 'Display the profile of the user with specified login',
    action: 'store_true'
});

let arguments = parser.parse_args();

if(arguments.profile) {
    getUserByLogin(arguments.login)
        .then((user) => {
            console.log(user.toString() + '\n');
        })
        .catch((error) => console.error(error));
}

if(arguments.following) {
    getFollowingByLogin(arguments.login, 10)
        .then((users) => {
            users.forEach(user => console.log(user.toString() + '\n'));
        })
        .catch((error) => console.error(error));
}

if(arguments.followers) {
    getFollowersByLogin(arguments.login, 10)
        .then((users) => {
            users.forEach(user => console.log(user.toString() + '\n'));
        })
        .catch((error) => console.error(error));
}

if(arguments.repositories) {
    getReposByLogin(arguments.login)
        .then((repos) => {
            repos.forEach(repo => console.log(repo.toString() + '\n'));
        })
        .catch((error) => console.error(error));
}