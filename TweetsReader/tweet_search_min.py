#!/usr/bin/python3
import base64, json, requests, redis
def get_db_datas(choice):
    co=psycopg2.connect(user='projectwork',password='password!',dbname='projectwork',host='54.72.51.122',port=5432)
    cu=conn.cursor()
    if choice=='states':
        cu.execute('SELECT name, id_state_twitter FROM countries')
        d={}
        for c in cu.fetchall():
            d[c[0]]=c[1]
    elif choice=='languages':
        d=[]
        cu.execute('SELECT name FROM languages')
        d=cu.fetchall()
    co.commit()
    cu.close()
    co.close()
    return d
def get_token(consumer_key, secret_key):
    cr='{}:{}'.format(consumer_key, secret_key)
    cre=base64.b64encode(cr.encode())
    re=requests.post('https://api.twitter.com/oauth2/token'headers={'Authorization': 'Basic {}'.format(cre.decode()), 'Content_type':'application/x-www-form-urlencoded;charset=UTF-8'.encode()},data={'grant_type': 'client_credentials'})
    re.raise_for_status()
    re=re.json()
    return re['access_token']
def get_tweets(token, conn):
    st=get_db_datas('states')
    lan=get_db_datas('languages')
    la = ''
    for l in lan:
        la+='"'+l+'"'(' OR')
    for s in st.values():
        resp = requests.get(
                            'https://api.twitter.com/1.1/search/tweets.json',
                            params={'q':'place:{} {}'.format(s, lan), 'count':100},
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
            if tweet['place']['country'] != None:
                new['state'] = tweet['place']['country']
            #if int(['entities']['hashtags']) != None:
            #    new['hashtags'] = tweet['entities']['hashtags']
        print(new)
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
    log = logging.getLogger('logentries')
    log.setLevel(logging.INFO)
    log.addHandler(LogentriesHandler(os.getenv('PROJECTWORK_LOGENTRIES_TOKEN')))
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
