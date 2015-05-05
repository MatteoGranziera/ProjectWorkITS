'''
Example of twitter API usage.

Requires Twitter APP credentials stored in the following env vars:
  TWITTER_API_PKEY
  TWITTER_API_SECRET

Saves tweets in the DB.
'''
# standard library
import base64

# 3rd party
import requests


def get_bearer_token(consumer_key, app_secret):
    credentials = '{}:{}'.format(consumer_key, app_secret)
    credentials_enc = base64.b64encode(credentials.encode())
    resp = requests.post(
        'https://api.twitter.com/oauth2/token',
        headers={'Authorization': 'Basic {}'.format(credentials_enc.decode())},
        data={'grant_type': 'client_credentials'}
    )
    resp.raise_for_status()
    resp_data = resp.json()
    return resp_data['access_token']


def get_tweets(word, token):
    '''
    Returns:
      a list of tweets (dictionaries) as returned by twitter API
    '''
    url = 'https://api.twitter.com/1.1/search/tweets.json'
    resp = requests.get(
        url,
        params={'q': word, 'lang': 'it', 'count':100},
        headers={'Authorization': 'Bearer {}'.format(token)}
    )
    resp.raise_for_status()
    data = resp.json()
    return data['statuses']

if __name__ == '__main__':
    import os
    import sys
    print('getting bearer token')
    token = get_bearer_token(
        os.getenv('TWITTER_API_PKEY'),
        os.getenv('TWITTER_API_SECRET')
        )
    print('bearer token created!')
    print('getting tweets')
    tweets = get_tweets(sys.argv[1], token)
    print('got {} tweets'.format(len(tweets)))
