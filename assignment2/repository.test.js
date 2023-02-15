const EXAMPLE_REPO_JSON = JSON.parse('{ \
  "nameWithOwner": "apple/swift", \
  "description": "The Swift Programming Language" \
}');

const repositories = require('./repository');
const Repository = repositories.Repository;


test('parse and get repository\'s name and owner', () => {
    const repo = new Repository(EXAMPLE_REPO_JSON);
    expect(repo.nameWithOwner).toBe('apple/swift');
});

test('parse and get repository\'s description', () => {
    const repo = new Repository(EXAMPLE_REPO_JSON);
    expect(repo.description).toBe('The Swift Programming Language');
});