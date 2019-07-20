package com.appspot.guguruchan;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class AccessTokenUtility {
    public static void main(String args[]) throws Exception {
        // このファクトリインスタンスは再利用可能でスレッドセーフです
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer("コンシューマキーを指定", "コンシューマシークレットを指定");
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("次のURLを開いて認可を行ってください。");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("認可後、表示されたPINを入力してください[PIN]:");
            String pin = br.readLine();
            try {
                if (pin.length() > 0) {
                    accessToken = twitter
                            .getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("アクセストークンを取得できません");
                } else {
                    te.printStackTrace();
                }
            }
        }
        storeAccessToken(twitter.verifyCredentials().getId(), accessToken);
        System.exit(0);
    }

    private static void storeAccessToken(long useId, AccessToken accessToken) {
        System.out.println(accessToken.getToken());
        System.out.println(accessToken.getTokenSecret());
    }
}