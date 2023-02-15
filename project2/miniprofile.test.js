const EXAMPLE_USER_JSON = JSON.parse('{ \
    "name": "John Appleseed", \
    "login": "johnapple", \
    "avatarUrl": "genericurl.com/avatar.jpg" \
  }');

const miniprofiles = require('./miniprofile');
const MiniProfile = miniprofiles.MiniProfile;

test('parse and get miniuser\'s avatar URL', () => {
    const user = new MiniProfile(EXAMPLE_USER_JSON);
    expect(user.avatarUrl).toBe('genericurl.com/avatar.jpg');
});

test('parse and get miniuser\'s name', () => {
    const user = new MiniProfile(EXAMPLE_USER_JSON);
    expect(user.name).toBe('John Appleseed');
});

test('parse and get miniuser\'s GitHub username', () => {
    const user = new MiniProfile(EXAMPLE_USER_JSON);
    expect(user.login).toBe('johnapple');
});