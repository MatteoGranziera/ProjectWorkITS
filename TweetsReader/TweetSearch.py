'''
Example of twitter API usage.

Requires Twitter APP credentials stored in the following env vars:
  TWITTER_API_PKEY
  TWITTER_API_SECRET

Saves tweets in the DB.
'''
# standard library
import base64
import json

# 3rd party
import requests


def get_bearer_token(consumer_key, app_secret):
    '''
    Returns:
        a token needed to work with twitter APIs
    '''
    credentials = '{}:{}'.format(consumer_key, app_secret)
    credentials_enc = base64.b64encode(credentials.encode())
    '''
    resp = requests.post(
        'https://api.twitter.com/oauth2/token',
        headers={'Authorization': 'Basic {}'.format(credentials_enc.decode())},
        data={'grant_type': 'client_credentials'}
    )
    resp.raise_for_status()
    data = resp.json()
    bearer_token = base64.b64encode(data['access_token'].encode())
    resp = requests.post(
        'https://api.twitter.com/oauth2/token',
        headers={'Authorization': 'Basic {}'.format(bearer_token.decode()), 'Content_type':'application/x-www-form-urlencoded;charset=UTF-8'.encode()},
        data={'grant_type': 'client_credentials'}
    )
    '''
    resp = requests.post(
        'https://api.twitter.com/oauth2/token',
        headers={'Authorization': 'Basic {}'.format(credentials_enc.decode()), 'Content_type':'application/x-www-form-urlencoded;charset=UTF-8'.encode()},
        data={'grant_type': 'client_credentials'}
    )
    resp.raise_for_status()
    resp_data = resp.json()
    #return resp_data
    return resp_data['access_token']

#URL to make request: https://api.twitter.com/1.1/geo/search.json
#Method: GET
#Headers: 'Authorization':'Bearer <token>'
#Params: 'query':'<state name>'
def get_tweets(word, token):
    '''
    Returns:
      a list of tweets (dictionaries) as returned by twitter API
    '''
    states_id={
    "Austria":"30410557050f13a5",
    "Belgium":"78bfaf3f12c05982",
    "Bulgaria":"1ef1183ed7056dc1",
    "Croatia":"8d65596349ee2e01",
    "Cyprus":"f0af1239cbebb474",
    "Czech Republic":"6b5d375c346e3be9",
    "Denmark":"c29833e68a86e703",
    "Estonia":"e222580e9a58b499",
    "Finland":"e7c97cdfef3a741a",
    "France":"991b4344edc2d520",
    "Germany":"fdcd221ac44fa326",
    "Greece":"2ee7eeaa84dbe65a",
    "Hungary":"81b8dcbe189773f2",
    "Ireland":"78e9729ff12a648e",
    "Italy":"c799e2d3a79f810e",
    "Latvia":"d0e642e8a900f679", #Lettonia
    "Lithuania":"d5cde4dddd7e6f94",
    "Luxembourg":"7fb8d824354f11ea",
    "Malta":"1d834adff5d584df",
    "The Netherlands":"879d7cfc66c9c290",
    "Poland":"d9074951d5976bdf",
    "Portugal":"c9f6408fbe911554",
    "Romania":"f7531639e8db5e12",
    "Slovakia":"34ed2e67dd5a22bb",
    "Slovenia":"58f54743b1a62911",
    "Spain":"ecdce75d48b13b64",
    "Sweeden":"82b141af443cb1b8",
    "United Kingdom":"6416b8512febefc9",
    }
    url = 'https://api.twitter.com/1.1/search/tweets.json'
    resp = requests.get(
        url,
        params={'q': word, 'lang': 'it', 'count':100, 'result_type':'both'},
        headers={'Authorization': 'Bearer {}'.format(token)}
    )
    resp.raise_for_status()
    data = resp.json()
    return data['statuses']

def clean_tweets(tweets):
    #First clean, then save! DIO CAN!
    keys = ['coordinates', 'lang', 'place', ]
    #for tweet in tweets:

    tweets = json.dumps(tweets)
    out = open('./prova','w')
    out.write(tweets)
    out.close()
    return tweets

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
    tweets = clean_tweets(tweets)
