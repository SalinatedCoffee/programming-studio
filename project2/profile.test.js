const EXAMPLE_USER_JSON = JSON.parse('{ \
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

const profiles = require('./profile');
const Profile = profiles.Profile;

test('parse and get user\'s avatar URL', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.avatarUrl).toBe('https://avatars2.githubusercontent.com/u/2250986?v=4');
});

test('parse and get user\'s name', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.name).toBe('John Doe');
});

test('parse and get user\'s GitHub username', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.login).toBe('john');
});

test('parse and get user\'s bio', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.bio).toBe('Lorem ipsum');
});

test('parse and get user\'s website URL', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.website).toBe('https://twitter.com/john');
});

test('parse and get user\'s email address', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.email).toBe('john@email.com');
});

test('parse and get user\'s public repo count', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.numPublicRepos).toBe(7);
});

test('parse and get user\'s number of followers', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.numFollowers).toBe(15);
});

test('parse and get user\'s number of following', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.numFollowing).toBe(21);
});

test('parse and get when the user created the account', () => {
    const user = new Profile(EXAMPLE_USER_JSON);
    expect(user.createdAt).toBe('Thu Aug 30 2012');
})