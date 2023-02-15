const OAuth = require('./oauth');
const { Repository } = require('./repository');
const { Profile } = require('./profile');
const { MiniProfile } = require('./miniprofile');
const getUserByLogin = OAuth.getUserByLogin;
const getReposByLogin = OAuth.getReposByLogin;
const getFollowersByLogin = OAuth.getFollowersByLogin;
const getFollowingByLogin = OAuth.getFollowingByLogin;
const requestQuery = OAuth.requestQuery;
const parseRepos = OAuth.parseRepos;
const parseUsers = OAuth.parseUsers;

const MOCK_JSON_USER = JSON.parse('{ \
    "data": { \
      "user": { \
        "createdAt": "2012-08-30T20:38:24Z", \
        "bio": "Lorem ipsum", \
        "name": "John Doe", \
        "websiteUrl": "https://twitter.com/john", \
        "avatarUrl": "https://avatars2.githubusercontent.com/u/2250986?v=4", \
        "login": "john", \
        "email": "john@email.com", \
        "followers": { \
          "totalCount": 15 \
        }, \
        "following": { \
          "totalCount": 21 \
        }, \
        "repositories": { \
          "totalCount": 7 \
        } \
      } \
    } \
  }');

const MOCK_JSON_REPOS = JSON.parse('{ \
    "data": { \
      "user": { \
        "repositories": { \
          "nodes": [ \
            { \
              "nameWithOwner": "john/repo1", \
              "description": "First repo" \
            }, \
            { \
              "nameWithOwner": "john/repo2", \
              "description": "Second repo" \
            }, \
            { \
              "nameWithOwner": "john/repo3", \
              "description": "Contrived empty repository for demo purposes" \
            } \
          ] \
        } \
      } \
    } \
  }');

const MOCK_JSON_FOLLOWERS = JSON.parse('{ \
  "data": { \
    "user": { \
      "followers": { \
        "nodes": [ \
          { \
            "name": "John Appleseed", \
            "login": "johnapple", \
            "avatarUrl": "genericurl.com/avatar.jpg" \
          }, \
          { \
            "name": "Jane Appleseed", \
            "login": "janeapple", \
            "avatarUrl": "genericurl.com/avatar.jpg" \
          }, \
          { \
            "name": "Ada Lovelace", \
            "login": "ada", \
            "avatarUrl": "genericurl.com/avatar.jpg" \
          } \
        ] \
      } \
    } \
  } \
}');

const MOCK_JSON_FOLLOWING = JSON.parse('{ \
  "data": { \
    "user": { \
      "following": { \
        "nodes": [ \
          { \
            "name": "John Appleseed", \
            "login": "johnapple", \
            "avatarUrl": "genericurl.com/avatar.jpg" \
          }, \
          { \
            "name": "Jane Appleseed", \
            "login": "janeapple", \
            "avatarUrl": "genericurl.com/avatar.jpg" \
          }, \
          { \
            "name": "Ada Lovelace", \
            "login": "ada", \
            "avatarUrl": "genericurl.com/avatar.jpg" \
          } \
        ] \
      } \
    } \
  } \
}');

beforeEach(() => {
    fetch.resetMocks();
});

test('testing whether the query is requested correctly', () => {
    fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_USER));
    
    return requestQuery('test_query')
        .then(res => res.data.user)
        .then(user => {
            expect(user.name).toBe('John Doe');
            expect(user.bio).toBe('Lorem ipsum');
            expect(user.email).toBe('john@email.com');
            expect(user.following.totalCount).toBe(21);

            expect(fetch.mock.calls[0][0]).toEqual('https://api.github.com/graphql');
            expect(fetch.mock.calls[0][1].body).toBe('test_query');
        });
});

test('testing whether a query response for a user is parsed correctly', () => {
    fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_USER));
    
    return getUserByLogin('test_login')
        .then(user => {
            expect(user.name).toBe('John Doe');
            expect(user.bio).toBe('Lorem ipsum');
            expect(user.numFollowing).toBe(21);
            expect(user instanceof Profile).toBe(true);
        });
});

test('testing whether a JSON response containing multiple repositories is parsed correctly', () => {
    repos = parseRepos(MOCK_JSON_REPOS);
    expect(repos[0] instanceof Repository).toBe(true);
    expect(repos[0].nameWithOwner).toBe('john/repo1');
    expect(repos[0].description).toBe('First repo');
    expect(repos[1].nameWithOwner).toBe('john/repo2');
    expect(repos[2].nameWithOwner).toBe('john/repo3');
});

test('testing whether a query response for multiple repositories is parsed correctly', () => {
    fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_REPOS));

    return getReposByLogin('test_login')
        .then(repos => {
            expect(repos[0].nameWithOwner).toBe('john/repo1');
            expect(repos[0].description).toBe('First repo');
            expect(repos[1].nameWithOwner).toBe('john/repo2');
            expect(repos[2].nameWithOwner).toBe('john/repo3');
        });
});

test('testing whether a JSON response containing followers is parsed correctly', () => {
  users = parseUsers(MOCK_JSON_FOLLOWERS, 'followers');
  expect(users[0] instanceof MiniProfile).toBe(true);
  expect(users[0].name).toBe('John Appleseed');
  expect(users[0].login).toBe('johnapple');
  expect(users[0].avatarUrl).toBe('genericurl.com/avatar.jpg');
  expect(users[1].name).toBe('Jane Appleseed');
  expect(users[2].name).toBe('Ada Lovelace');
});

test('testing whether a query response for followers is parsed correctly', () => {
  fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_FOLLOWERS));

  return getFollowersByLogin('test', 3)
    .then(users => {
      expect(users[0].name).toBe('John Appleseed');
      expect(users[0].login).toBe('johnapple');
      expect(users[0].avatarUrl).toBe('genericurl.com/avatar.jpg')
      expect(users[1].name).toBe('Jane Appleseed');
      expect(users[2].name).toBe('Ada Lovelace');
    })
});

test('testing whether a JSON response containing following is parsed correctly', () => {
  users = parseUsers(MOCK_JSON_FOLLOWING, 'following');
  expect(users[0] instanceof MiniProfile).toBe(true);
  expect(users[0].name).toBe('John Appleseed');
  expect(users[0].login).toBe('johnapple');
  expect(users[0].avatarUrl).toBe('genericurl.com/avatar.jpg');
  expect(users[1].name).toBe('Jane Appleseed');
  expect(users[2].name).toBe('Ada Lovelace');
});

test('testing whether a query response for following is parsed correctly', () => {
  fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_FOLLOWING));

  return getFollowingByLogin('test_login', 3)
    .then(users => {
      expect(users[0].name).toBe('John Appleseed');
      expect(users[0].login).toBe('johnapple');
      expect(users[0].avatarUrl).toBe('genericurl.com/avatar.jpg')
      expect(users[1].name).toBe('Jane Appleseed');
      expect(users[2].name).toBe('Ada Lovelace');
    })
});