'''
TODO:
    Finish cleaning tweets
    Redis connection and put tweets in queue
    Log system
'''


'''
Module used to:
    connect with twitter APIs and get tweets
    clean tweets from it's useless parts
    saves tweets in a Redis queue

Requires Twitter APP credentials stored in the following env vars:
  TWITTER_API_PKEY
  TWITTER_API_SECRET
'''
# standard library
import base64
import json

# 3rd party
import requests
import redis


def get_bearer_token(consumer_key, secret_key):
    '''
    Parameters:
        consumer_key: public key of my Twitter applications
        secret_key: secret key of my Twitter applications
    Returns:
        a token (bearer) we need to authenticate requests to Twitter APIs
    '''
    credentials = '{}:{}'.format(consumer_key, secret_key)
    credentials_enc = base64.b64encode(credentials.encode())
    url = 'https://api.twitter.com/oauth2/token'
    ctype = 'application/x-www-form-urlencoded;charset=UTF-8'
    resp = requests.post(
        url,
        headers={'Authorization': 'Basic {}'.format(credentials_enc.decode()), 'Content_type':ctype.encode()},
        data={'grant_type': 'client_credentials'}
    )
    resp.raise_for_status()
    resp_data = resp.json()
    return resp_data['access_token']


def get_tweets(word, token):
    '''
    Parameters:
        word: the query to make to search API
        token: the key we need to authenticate requests
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

    for state in states_id:
        resp = requests.get(
            'https://api.twitter.com/1.1/search/tweets.json',
            params={'q':'place:' + states_id.get(state) + ' ' +  word, 'count':1, 'result_type':'recent'},
            headers={'Authorization': 'Bearer {}'.format(token)}
            )
        resp.raise_for_status()
        data = resp.json()
        data = data['statuses']
        if len(datas) == 0:
            datas = data
        else:
            datas.update(data)
    return datas

def clean_tweets(tweets):
    words = ['geo', 'coordinates', 'in_reply_to_user_id_str', 'in_reply_to_screen_name', 'in_reply_to_user_id', 'truncated', 'metadata',
    'in_reply_to_status_id', 'favorited', 'user', 'favorite_count', 'lang', 'id_str', 'contributors', 'source', 'id', 'in_reply_to_status_id_str',
    'place', 'possibly_sensitive', ]
    try:
        for tweet in tweets:
            for word in words:
                del(tweet[word])
            tweet['hashtags'] = tweet['entities']['hashtags']
            del(tweet['entities']['hashtags'])
    except KeyError:
        pass
    return tweets #sputarli con "" invece di ''

'''
def save_tweets(tweets):
    
    Parameters:
        tweets: a list of tweets to put in redis queue

    When the last tweet is pushed in queue, push a last final string
'''

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
    word = sys.argv[1][:500] if len(sys.argv[1] > 500) else sys.argv[1]
    tweets = get_tweets(word, token)
    #print('got {} tweets'.format(len(tweets)))
    print('cleaning tweets')
    tweets = clean_tweets(tweets)
