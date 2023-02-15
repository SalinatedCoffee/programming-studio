const profiles = require('./profile');
const miniprofiles = require('./miniprofile');
const repositories = require('./repository');
const Profile = profiles.Profile;
const MiniProfile = miniprofiles.MiniProfile;
const Repository = repositories.Repository;
import { OAUTH_TOKEN } from '@env';
//require('dotenv').config();

//const token = process.env.OAUTH_TOKEN;
const token = OAUTH_TOKEN;
const OAUTH_URL = 'https://api.github.com/graphql';
const NUM_OF_REPOS = 20


function requestQuery(query) {
    return fetch(OAUTH_URL, {
        method: 'POST',
        headers: {
            'Authorization': 'bearer ' + token,
            'Content-Type': 'application/json'
        },
        body: query
    })
        .then((response) => response.json())
        .catch((error) => null);
}

function getUserByLogin(login) {
    const QUERY_STRING = JSON.stringify({
        query: 'query { \
                    user(login:"' + login + '") { \
                        avatarUrl, \
                        name, \
                        login, \
                        bio, \
                        websiteUrl, \
                        email, \
                        repositories { totalCount }, \
                        followers { totalCount }, \
                        following { totalCount }, \
                        createdAt \
                    } \
                }'
    });

    return requestQuery(QUERY_STRING)
        .then((json) => {
            if (!json.data.user) {
                return null;
            } 
            else {
                return new Profile(json);
            }
        })
        .catch((error) => null);
}

function getReposByLogin(login) {
    const QUERY_STRING = JSON.stringify({
        query: 'query { \
                    user(login:"' + login + '") { \
                        repositories(last: ' + NUM_OF_REPOS.toString() + ') { \
                            nodes { \
                                nameWithOwner, \
                                description \
                            } \
                        } \
                    } \
                }'
    });
    
    return requestQuery(QUERY_STRING)
        .then((json) => {
            if(!json.data.user) {
                return null;
            }
            else {
                return parseRepos(json);
            }
        })
        .catch((error) => null);
}

function parseRepos(repos) {
    var repo_list = [];

    repos['data']['user']['repositories']['nodes'].forEach(element => repo_list.push(new Repository(element)));
    return repo_list;
}

function getFollowersByLogin(login, num) {
    const QUERY_STRING = JSON.stringify({
        query: 'query { \
                user(login:"' + login + '") { \
                    followers(first:' + num.toString() + ') { \
                        nodes { \
                            login, \
                            name, \
                            avatarUrl \
                        } \
                    } \
                } \
            }'
    });

    return requestQuery(QUERY_STRING)
        .then((json) =>  {
            if(!json.data.user) {
                console.log('[ERROR] oauth.getFollowersByLogin(): requestQuery returned null.');
                return null;
            }
            else {
                return parseUsers(json, 'followers');
            }
        })
        .catch((error) => null);
}

function getFollowingByLogin(login, num) {
    const QUERY_STRING = JSON.stringify({
        query: 'query { \
            user(login:"' + login + '") { \
                following(first:' + num.toString() + ') { \
                    nodes { \
                        login, \
                        name, \
                        avatarUrl \
                    } \
                } \
            } \
        }'
    });
    
    return requestQuery(QUERY_STRING)
        .then((json) => {
            if(!json.data.user) {
                console.log('[ERROR] oauth.getFollowingByLogin(): requestQuery returned null.');
                return null;
            }
            else {
                return parseUsers(json, 'following');
            }
        })
        .catch((error) => null);
}

function parseUsers(users, mode) {
    var users_list = [];
    
    users['data']['user'][mode]['nodes'].forEach(element => users_list.push(new MiniProfile(element)));
    return users_list;
}

module.exports = {
    getUserByLogin,
    getReposByLogin,
    getFollowersByLogin,
    getFollowingByLogin,
    requestQuery,
    parseRepos,
    parseUsers
}