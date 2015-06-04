#!/usr/bin/python3
'''
TODO:
    improve log system
    Replace states_id and languages lists with file read
    create new dict from tweets, no deleting useless keys
'''


'''
Module used to:
    connect with twitter APIs and get tweets
    clean tweets from it's useless parts
    push tweets in a Redis queue

Requires Twitter APP credentials stored in the following env vars:
  LOGENTRIES_TOKEN
  TWITTER_API_PKEY
  TWITTER_API_SECRET
'''
# standard library
import base64
import json

# 3rd party
import requests
import redis

def set_logger(logentries_token):
    log = logging.getLogger('logentries')
    log.setLevel(logging.INFO)
    log.addHandler(LogentriesHandler(logentries_token))
    return log

def get_token(consumer_key, secret_key):
    '''
    Parameters:
        consumer_key: public key of my Twitter applications
        secret_key: secret key of my Twitter applications
    Returns:
        a token we need to authenticate requests to Twitter APIs
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
    resp = resp.json()
    return resp['access_token']

def get_tweets(token, conn):
    '''
    Parameters:
        word: the query to make to search API
        token: the key we need to authenticate requests
    Returns:
      a list of tweets (dictionaries) as returned by twitter API
    '''
    x = 0
    for state in states_id.values():
        x += 1
        '''resp = requests.get(
                                    'https://api.twitter.com/1.1/search/tweets.json',
                                    params={'q':'place:{} {}'.format(state, languages), 'count':100},
                                    headers={'Authorization': 'Bearer {}'.format(token)}
                                    )'''
        resp = requests.get(
                        'https://api.twitter.com/1.1/search/tweets.json',
                        params={'q':'place:{} {}'.format(state, '#java OR #python'), 'count':100},
                        headers={'Authorization': 'Bearer {}'.format(token)}
                        )
        resp.raise_for_status()
        tweets = resp.json()
        log.info('cleaning tweets ' + str(x) + ' len: ' + str(len(tweets['statuses'])))
        tweets = clean_tweets(tweets['statuses'])
        log.info('saving tweets')
        save_tweets(tweets, conn)
    redis.Redis.rpush(conn, 'tweets', 'Finished')
    return

def clean_tweets(tweets):
    #keys not to cancel: text, created_at, place
    cleaned = []
    whitelist = ['text', 'created_at']
    words = ['metadata', 'id', 'favorite_count', 'possibly_sensitive', 'source', 'geo', 'in_reply_to_status_id_str', 
    'in_reply_to_status_id', 'in_reply_to_user_id', 'user', 'truncated', 'in_reply_to_user_id_str', 'in_reply_to_screen_name', 
    'contributors', 'lang', 'coordinates', 'entities', 'retweeted_status', 'id_str', 'favorited', 'place']
    
    for tweet in tweets:
        if tweet['place'] != None:
            tweet['state'] = tweet['place']['country']
        tweet['hashtags'] = tweet['entities']['hashtags']
        for word in words:
            try:
                del(tweet[word])
            except KeyError:
                pass
    '''
    for tweet in tweets:
        new = {}
        for word in whitelist:
            if tweet[word] != None:
                new[word] = tweet[word]
            if tweet['place']['country'] != None:
                new['state'] = tweet['place']['country']
            if int(['entities']['hashtags']) != None:
                new['hashtags'] = tweet['entities']['hashtags']
        #print(new)
        cleaned.append(new)
    return cleaned
    '''
    return tweets

def get_connection():
    #host = '54.72.51.122'
    host = '192.168.101.41'
    port = 6379
    db = 0
    POOL = redis.ConnectionPool(host = host, port = port, db = db)
    conn = redis.Redis(connection_pool=POOL)
    return conn

def save_tweets(tweets, conn):
    '''
    Parameters:
        tweets: a list of tweets to put in redis queue
    '''
    for tweet in tweets:
        redis.Redis.rpush(conn, 'tweets', tweet)
    return

if __name__ == '__main__':
    import os
    import sys
    import logging
    from logentries import LogentriesHandler
    from states import states_id
    from languages import languages
    log = set_logger(os.getenv('LOGENTRIES_TOKEN'))
    log.info('Started')
    log.info('getting bearer token')
    token = get_token(
        os.getenv('TWITTER_API_PKEY'),
        os.getenv('TWITTER_API_SECRET')
        )
    log.info('Getting tweets')
    conn = get_connection()
    tweets = get_tweets(token, conn)   
    log.info('Finished')
    sys.exit(0)
