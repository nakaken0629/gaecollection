package com.appspot.guguruchan;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public final class TwitterHelper {
    private static final String CONSUMER_KEY = "コンシューマキーを指定";
    private static final String CONSUMER_SECRET = "コンシューマシークレットを指定";
    private static final String ACCESS_TOKEN = "アクセストークンを指定";
    private static final String ACCESS_TOKEN_SECRET = "アクセストークンシークレットを指定";
    private static final Logger logger = Logger.getLogger(TwitterHelper.class
            .getName());
    private static final TwitterFactory tf = new TwitterFactory();

    private TwitterHelper() { /* nop */}

    /* アクセストークンで認可されたTwitterオブジェクトを取得する */
    private static Twitter getTwitter() {
        Twitter twitter = tf.getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        twitter.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN,
                ACCESS_TOKEN_SECRET));
        return twitter;
    }

    /* タイムラインにツイートを投稿する */
    public static void tweet(String message) throws TwitterException {
        Twitter twitter = getTwitter();
        twitter.updateStatus(message);
    }

    /* フォロー返しをする */
    public static void refollow() throws TwitterException {
        Twitter twitter = getTwitter();
        /* フォローアカウント一覧を取得する */
        long[] friendsId = twitter.getFriendsIDs(1).getIDs();
        /* Arrays.binarySearchメソッドを使うために、あらかじめ配列をソートする */
        Arrays.sort(friendsId);
        /* フォロワーを1人ずつ確認する */
        for (long followerId : twitter.getFollowersIDs(1).getIDs()) {
            if (Arrays.binarySearch(friendsId, followerId) < 0) {
                try {
                    /* フォローしていなければフォローする */
                    twitter.createFriendship(followerId);
                } catch (TwitterException e) {
                    /* フォローできない人がいても、警告ログを出して処理を継続する */
                    if (logger.isLoggable(Level.WARNING)) {
                        logger.warning(e.toString());
                    }
                }
            }
        }
    }

    /* フォローを外す */
    public static void unfollow() throws TwitterException {
        Twitter twitter = getTwitter();
        /* フォロワーアカウント一覧を取得する */
        long[] followersId = twitter.getFollowersIDs(1).getIDs();
        /* Arrays.binarySearchメソッドを使うために、あらかじめ配列をソートする */
        Arrays.sort(followersId);
        /* フォローを1人ずつ確認する */
        for (long friendId : twitter.getFriendsIDs(1).getIDs()) {
            if (Arrays.binarySearch(followersId, friendId) < 0) {
                try {
                    twitter.destroyFriendship(friendId);
                } catch (TwitterException e) {
                    /* フォローを外せない人がいても、警告ログを出して処理を継続する */
                    if (logger.isLoggable(Level.WARNING)) {
                        logger.warning(e.toString());
                    }
                }
            }
        }
    }

    /* ステータスを取得する */
    public static List<Status> getStatuses() throws TwitterException {
        Twitter twitter = getTwitter();
        return twitter.getHomeTimeline();
    }

    /* アカウントの表示名を取得する */
    public static String getScreenName() throws TwitterException {
        Twitter twitter = getTwitter();
        return twitter.getScreenName();
    }
}
