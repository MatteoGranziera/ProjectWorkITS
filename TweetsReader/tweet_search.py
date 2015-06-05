#!/usr/bin/python3
'''
TODO:
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
    '''
    Parameters:
        logentries_token: logentries access token
    Returns:
        log: logger
    '''
    log = logging.getLogger('logentries')
    log.setLevel(logging.INFO)
    log.addHandler(LogentriesHandler(logentries_token))
    return log

def get_db_datas(choice):
    '''
    Parameters:
        choice: 'states' or 'languages'
    Returns:
        data: data we need
    '''
    conn = psycopg2.connect(user='projectwork', password='password!',
                            dbname='projectwork', host='54.72.51.122',
                            port=5432)
    curs = conn.cursor()
    if choice == 'states':
        curs.execute(
            'SELECT name, id_state_twitter FROM countries'
            )
        data = {}
        for chunk in curs.fetchall():
            data[chunk[0]] = chunk[1]
    '''
    elif choice == 'languages':
        data = []
        curs.execute(
            'SELECT name FROM languages'
            )
        data = curs.fetchall()
    '''
    conn.commit()
    curs.close()
    conn.close()
    return data
    
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
    states = get_db_datas('states')
    #languages = get_db_datas('languages')
    from languages import lang
    x = ''
    for l in lang:
        x += '"' + l + '"' + " OR "
    lang = x
    for state in states.values():
        resp = requests.get(
                            'https://api.twitter.com/1.1/search/tweets.json',
                            params={'q':'place:{} {}'.format(state, lang), 'count':100},
                            headers={'Authorization': 'Bearer {}'.format(token)}
                            )
        resp.raise_for_status()
        tweets = resp.json()
        tweets = clean_tweets(tweets['statuses'])
        log.info('saving tweets')
        save_tweets(tweets, conn)
    redis.Redis.rpush(conn, 'tweets', 'Finished')
    return

def clean_tweets(tweets):
    '''
    Parameters:
        tweets: list of tweets        
    Returns:
        tweets: list of cleaned tweets
    '''
    cleaned = []
    whitelist = ['text', 'created_at', 'retweeted', 'retweet_count',]
    '''
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
            try:
                if tweet['place']['country'] != None:
                    new['state'] = tweet['place']['country']
            except Exception:
                pass
        #print(new)
        cleaned.append(new)
    return cleaned
    #return tweets

def get_redis_connection():
    '''
    Parameters:
        None
    Returns:
        conn: redis connection resource
    '''
    host = '54.72.51.122'
    port = 6379
    db = 0
    POOL = redis.ConnectionPool(host = host, port = port, db = db)
    conn = redis.Redis(connection_pool=POOL)
    return conn

def save_tweets(tweets, conn):
    '''
    Parameters:
        tweets: a list of tweets to put in redis queue
    Returns:
        None
    '''
    for tweet in tweets:
        redis.Redis.rpush(conn, 'tweets', tweet)
    return

if __name__ == '__main__':
    import os
    import sys
    import logging
    from logentries import LogentriesHandler
    import psycopg2
    log = set_logger(os.getenv('PROJECTWORK_LOGENTRIES_TOKEN'))
    log.info('Started')
    log.info('getting bearer token')
    token = get_token(
        os.getenv('TWITTER_API_PKEY'),
        os.getenv('TWITTER_API_SECRET')
        )
    log.info('Getting redis connection')
    conn = get_redis_connection()
    log.info('Getting tweets')
    tweets = get_tweets(token, conn)   
    log.info('Finished')
    sys.exit(0)
