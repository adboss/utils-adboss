package io.adboss.platforms;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import facebook4j.FacebookException;
import io.adboss.dataconnection.DB;
import io.adboss.utils.qreah;

public class TW {

	private static final Logger log = Logger.getLogger(TW.class.getName());
	static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static JsonFactory JSON_FACTORY = new JacksonFactory();
    static String oauth_consumer_key = "uYWoAOrl8RWmtDMcufbfMYOpA";
    static String oauth_consumer_secret = "RNISGMpYEObUORWuB0Eqsr3tu6k0zOcVNGi16A9c43KYCdtM7S";
    
	
	
	
	public boolean setName(String username, String TWName) throws ClassNotFoundException, SQLException, ServletException, IOException {
		boolean out = false;
		DB db = new DB();
		out = db.setTWUserName(username, TWName);
		return out;
	}
	
	
	
	/**
	 *  Twitter Ads API
	 *  This function is used when the user connects to Twitter in the App,
	 *  not when you need the id for a Twitter Ads API
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws GeneralSecurityException 
	 * 
	 */
	
	public String getTWidAd(String username) throws ClassNotFoundException, SQLException, ServletException, IOException, GeneralSecurityException {
		String TWidAd = null;
		qreah q = new qreah();
		DB db = new DB();
		String ATT = db.getATT(username);
		String ATTSecret = db.getATTSecret(username);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		Twitter twitter =
			     new TwitterFactory(cb.build()).getInstance();
		String consumerKey = twitter.getConfiguration().getOAuthConsumerKey();
		String consumerSecret = twitter.getConfiguration().getOAuthConsumerSecret();		
		
		HttpRequestFactory requestFactory 
	    = HTTP_TRANSPORT.createRequestFactory(
	      (HttpRequest requestX) -> {
	        requestX.setParser(new JsonObjectParser(JSON_FACTORY));
	    });		
		String twUrl = "https://ads-api.twitter.com/5/accounts";
		
		OAuthHmacSigner oauth1 = new OAuthHmacSigner();
		oauth1.clientSharedSecret = consumerSecret;
		oauth1.tokenSharedSecret = ATTSecret;
		OAuthParameters param = new OAuthParameters();
		param.consumerKey = consumerKey;
		param.nonce = "iOg6iwbnbwv";
		param.signatureMethod = "HMAC-SHA1";
		param.signer = oauth1;
		param.timestamp = String.valueOf(q.getTimestamp()).substring(0, 10);
		param.token = ATT;
		param.version = "1.0";
		GenericUrl gURL = new GenericUrl(twUrl);
		
		param.computeSignature("GET", gURL);
		
		// Parameters
		log.info("username: " + username);
		log.info("consumerSecret: " + consumerSecret);
		log.info("ATTSecret: " + ATTSecret);
		log.info("consumerKey: " + consumerKey);
		log.info("nonce: " + "iOg6iwbnbwv");
		log.info("signatureMethod: " + "HMAC-SHA1");
		log.info("signer: " + oauth1);
		log.info("timestamp: " + String.valueOf(q.getTimestamp()).substring(0, 10));
		log.info("token: " + ATT);
		log.info("version: " + "1.0");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAuthorization(param.getAuthorizationHeader());
				
		HttpRequest request = requestFactory.buildGetRequest(gURL);
		request.setHeaders(headers);
		
		log.info("Other: " + request.getHeaders().getAuthorization());
		log.info(request.execute().parseAsString());
		
		/*
		log.info(request.execute().getRequest().getUrl().getRawPath());
		log.info(request.getUrl().getHost());
		log.info(request.getUrl().getRawPath());
		log.info(request.getUrl().getScheme());
		log.info(request.getUrl().getUserInfo());
		*/
		
		log.info(request.getUrl().toString());
		log.info(request.getHeaders().getAuthenticate());
		
		
		JSONObject response = new JSONObject(request.execute().parseAsString());
		JSONObject id = (JSONObject) response.getJSONArray("data").get(0);
		TWidAd = id.getString("id");
		log.info(TWidAd);
		return TWidAd;
	}
	
	public HttpRequest getRestResponse(String username, String twUrl) throws ClassNotFoundException, SQLException, ServletException, IOException, GeneralSecurityException {
		
		qreah q = new qreah();
		
		DB db = new DB();
		String ATT = db.getATT(username);
		String ATTSecret = db.getATTSecret(username);
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		Twitter twitter =
			     new TwitterFactory(cb.build()).getInstance();
		String consumerKey = twitter.getConfiguration().getOAuthConsumerKey();
		String consumerSecret = twitter.getConfiguration().getOAuthConsumerSecret();		
		
		HttpRequestFactory requestFactory 
	    = HTTP_TRANSPORT.createRequestFactory(
	      (HttpRequest requestX) -> {
	        requestX.setParser(new JsonObjectParser(JSON_FACTORY));
	    });		
				
		OAuthHmacSigner oauth1 = new OAuthHmacSigner();
		oauth1.clientSharedSecret = consumerSecret;
		oauth1.tokenSharedSecret = ATTSecret;
		OAuthParameters param = new OAuthParameters();
		param.consumerKey = consumerKey;
		param.nonce = "iOg6iwbnbwv";
		param.signatureMethod = "HMAC-SHA1";
		param.signer = oauth1;
		param.timestamp = String.valueOf(q.getTimestamp()).substring(0, 10);		//"1561820608";
		
		param.token = ATT;
		param.version = "1.0";
		GenericUrl gURL = new GenericUrl(twUrl);
		param.computeSignature("GET", gURL);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAuthorization(param.getAuthorizationHeader());
		System.out.println("headers: " + headers.getAuthorization());
		
		HttpRequest request = requestFactory.buildGetRequest(gURL);
		request.setHeaders(headers);
		
		return request;
	}
	
	public Twitter getTwitterClass(String username) throws ClassNotFoundException, ServletException, IOException, SQLException {
		Twitter twitter = new TwitterFactory().getInstance();
		DB db = new DB();
		db.ConnectDB();
		String ATT = db.getATT(username); 
		String ATTSecret = db.getATTSecret(username); 
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthAccessToken(ATT);
		cb.setOAuthAccessTokenSecret(ATTSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance(); 
		return twitter;
	}
	
	public String getHashtag(String username) throws ClassNotFoundException, ServletException, IOException, SQLException, IllegalStateException, TwitterException {
		String output = "error";
		Twitter tw = getTwitterClass(username);
		output = "@" + tw.getAccountSettings().getScreenName();
		return output;
	}
	
	public class GraphUrl extends GenericUrl {
		 
	    public GraphUrl(String encodedUrl) {
	        super(encodedUrl);
	    }	  
	}
	
	
public static void main(String[] args) throws FacebookException, ClassNotFoundException, ServletException, IOException, SQLException, JSONException, GeneralSecurityException, TwitterException {
	/*
	String username = "rafael@adarga.org";
	String ATT = "1397951737-uNGxkQUKhkBvGBGuW2cwI3kSPYnurpLjYh6DPjt";
	String ATTSecret = "PEtshNffMg1cD2PSPV2OSaVGnjg6in8RClPz9RigqHdne";
	TW tw = new TW();
	PostsList p = tw.getTWPosts(username);
	System.out.println(p.getString());
	String twUrl = "https://ads-api.twitter.com/5/accounts";
	HttpRequest req = tw.getRestResponse(username, twUrl);
	HttpResponse res = req.execute();
	System.out.println(res.parseAsString());
	
	
	ConfigurationBuilder cb = new ConfigurationBuilder();
	Twitter twitter =
		     new TwitterFactory(cb.build()).getInstance();
	
	ConfigurationBuilder cb1 = new ConfigurationBuilder();
	cb1.setOAuthAccessToken(ATT);
	cb1.setOAuthAccessTokenSecret(ATTSecret);
	TwitterFactory tf = new TwitterFactory(cb1.build());
	twitter = tf.getInstance(); 
	
	System.out.println(twitter.getOAuthAccessToken().getToken());
	System.out.println(twitter.getOAuthAccessToken().getTokenSecret());
	System.out.println(twitter.getAPIConfiguration().getAccessLevel());
	System.out.println(twitter.getAPIConfiguration().getRateLimitStatus());
	*/
	
	TW tw = new TW();
	
	log.info(tw.getHashtag("rafael@adarga.org"));
	
}





	
}
