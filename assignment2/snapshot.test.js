import React from 'react';
import renderer from 'react-test-renderer';
import ProfileComponent from './ProfileComponent';
import RepositoriesComponent from './RepositoriesComponent';
import UsersComponent from './UsersComponent';


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

beforeEach(() => {
    fetch.resetMocks();
});

test('testing whether the profile page renders correctly', async () => {
  fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_USER));

  const tree = renderer.create(<ProfileComponent />).toJSON();
  expect(tree).toMatchSnapshot();
});

test('testing whether the repositories page renders correctly', async () => {
  fetch.mockResponseOnce(JSON.stringify(MOCK_JSON_REPOS));

  const tree = renderer.create(<RepositoriesComponent />).toJSON();
  expect(tree).toMatchSnapshot();
})