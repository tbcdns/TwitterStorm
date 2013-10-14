package com.tchardonnens.TwitterStorm.utils;

import java.util.Random;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpPost;
import org.scribe.utils.OAuthEncoder;

public class OAuthConnection {
	String STREAMING_API_URL_FULL;
	String STREAMING_API_URL;
	String TRACK;

	String oauth_consumer_key;
	String oauth_token;
	String oauth_consumer_secret;
	String oauth_access_token_secret;
	String key;
	String oauth_signature_method = "HMAC-SHA1";
	String oauth_nonce = Nonce();
	String oauth_version = "1.0";

	Long oauth_timestamp = System.currentTimeMillis()/1000;
	
	public OAuthConnection(String url, String track, String cKey, String token, String cSecret, String accessTokenSecret){
		this.STREAMING_API_URL_FULL = url+"?track="+track;
		this.STREAMING_API_URL = url;
		this.TRACK = track;
		this.oauth_consumer_key = cKey;
		this.oauth_token = token;
		this.oauth_consumer_secret = cSecret;
		this.oauth_access_token_secret = accessTokenSecret;
		this.key = oauth_consumer_secret+"&"+oauth_access_token_secret;
	}
	 
	public HttpPost GetConnection(){
		HttpPost post = new HttpPost(STREAMING_API_URL_FULL);
		post.addHeader("Accept", "*/*");
		post.addHeader("Connection","close");
		post.addHeader("User-Agent", "OAuth gem v0.4.4");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Authorization", "OAuth oauth_consumer_key=\""+oauth_consumer_key+"\", oauth_nonce=\""+OAuthEncoder.encode(oauth_nonce)+"\", oauth_signature=\""+OAuthEncoder.encode(calculateRFC2104HMAC(Signature(),key))+"\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\""+oauth_timestamp+"\", oauth_token=\""+oauth_token+"\", oauth_version=\"1.0\"");
		post.addHeader("Host","stream.twitter.com");	
		return post;
	} 
	 
	private String Nonce(){
		byte[] r = new byte[32]; 
		Random rand = new Random();
		rand.nextBytes(r);
		String s = Base64.encodeBase64String(r);
		return s;
	}
	
	private String Signature(){
		String[] parameters = 
			{	OAuthEncoder.encode("oauth_consumer_key")+"="+OAuthEncoder.encode(oauth_consumer_key)+"&",
				OAuthEncoder.encode("oauth_nonce")+"="+OAuthEncoder.encode(oauth_nonce)+"&",
				OAuthEncoder.encode("oauth_signature_method")+"="+OAuthEncoder.encode("HMAC-SHA1")+"&",
				OAuthEncoder.encode("oauth_timestamp")+"="+OAuthEncoder.encode(oauth_timestamp.toString())+"&",
				OAuthEncoder.encode("oauth_token")+"="+OAuthEncoder.encode(oauth_token)+"&",
				OAuthEncoder.encode("oauth_version")+"="+OAuthEncoder.encode("1.0")+"&",
				OAuthEncoder.encode("track")+"="+OAuthEncoder.encode(TRACK)};
		
		String parameters_string="";
		for(int i=0;i<parameters.length;i++){
			parameters_string += parameters[i];
		}
			
		String sign = "POST&"+OAuthEncoder.encode(STREAMING_API_URL)+"&"+OAuthEncoder.encode(parameters_string);
		
		return sign;
	}
	
	private String calculateRFC2104HMAC(String data, String key){
		String result = "";
		try {
			String HMAC_SHA1_ALGORITHM = "HmacSHA1";
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			result = Base64.encodeBase64String(rawHmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
