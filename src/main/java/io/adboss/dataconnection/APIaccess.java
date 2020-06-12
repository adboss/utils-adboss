package io.adboss.dataconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Collections;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mybusiness.v4.MyBusiness;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;


public class APIaccess {
	
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Logger log = Logger.getLogger(APIaccess.class.getName());
		
	
	public static MyBusiness connectGMB() throws IOException {
		String CredentialsFile = "/cs.json";
		InputStream credentialsFile = APIaccess.class.getResourceAsStream(CredentialsFile);
		String scopes = "https://www.googleapis.com/auth/plus.business.manage";
		GoogleCredentials credentials = GoogleCredentials
				.fromStream(credentialsFile)
				.createScoped(Collections.singleton(scopes));
		credentials.refreshIfExpired();
		AccessToken token = credentials.refreshAccessToken();
		System.out.print(token);
		HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
		MyBusiness myBiz = new MyBusiness
				.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
				.setApplicationName("GMB").build();
		return myBiz;
	}
	
	
	public static MyBusiness connectGMBOauth20() throws IOException {
		String CredentialsFile = "/cs.json";
		InputStream credentialsFile = APIaccess.class.getResourceAsStream(CredentialsFile);
		String scopes = "https://www.googleapis.com/auth/plus.business.manage";
		GoogleCredentials credentials = GoogleCredentials
				.fromStream(credentialsFile)
				.createScoped(Collections.singleton(scopes));
		credentials.refreshIfExpired();
		AccessToken token = credentials.refreshAccessToken();
		System.out.print(token);
		GoogleCredentials credentials2 = new GoogleCredentials(token);
		HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials2);
		MyBusiness myBiz = new MyBusiness
				.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
				.setApplicationName("GMB").build();
		return myBiz;
	}
	
	
	public static MyBusiness connectGMBManual() throws ClassNotFoundException, SQLException, ServletException, IOException {
		
		String CLIENT_ID = "734812941405-hmq8vqivn22t5r9cs9711n5mp71sth24.apps.googleusercontent.com"; 
		String CLIENT_SECRET = "ATcIzGqaCc1CtWgStwjLvRGK"; 
		String REFRESH_TOKEN = "1//03sxa_2a__CTXCgYIARAAGAMSNwF-L9IrnV881EmcDAyfI9Q-6ijzJ6iJLs_Ekt6f9cxnZ2UZUgb9k-nc9zfumtnjvdQjKRVDdPI"; 
    	
    	GoogleCredential refreshTokenCredential = new GoogleCredential.Builder()
    			.setJsonFactory(JSON_FACTORY)
    			.setTransport(HTTP_TRANSPORT)
    			
    			.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
    			.build()
    			.setRefreshToken(REFRESH_TOKEN);
    	refreshTokenCredential.refreshToken(); //do not forget to call this method
    	String newAccessToken = refreshTokenCredential.getAccessToken();
    	
    	
    	GoogleCredential credential = new GoogleCredential()
    			.setAccessToken(newAccessToken);
    	MyBusiness myBiz = new MyBusiness.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName("GMB").build();
    	
    	return myBiz;
	}
	
	public static MyBusiness connectGMBOld() throws ClassNotFoundException, SQLException, ServletException, IOException {
		DB db = new DB();
		JSONObject cred = db.getOAUTH20Credentials("GMB");
		
		String CLIENT_ID = cred.getString("CLIENT_ID"); 
		String CLIENT_SECRET = cred.getString("CLIENT_SECRET"); 
		String REFRESH_TOKEN = cred.getString("REFRESH_TOKEN"); 
    	
    	GoogleCredential refreshTokenCredential = new GoogleCredential.Builder()
    			.setJsonFactory(JSON_FACTORY)
    			.setTransport(HTTP_TRANSPORT)
    			
    			.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
    			.build()
    			.setRefreshToken(REFRESH_TOKEN);
    	refreshTokenCredential.refreshToken(); //do not forget to call this method
    	String newAccessToken = refreshTokenCredential.getAccessToken();
    	
    	
    	GoogleCredential credential = new GoogleCredential()
    			.setAccessToken(newAccessToken);
    	MyBusiness myBiz = new MyBusiness.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName("GMB").build();
    	
    	return myBiz;
	}

	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ServletException {
		connectGMBManual();

	}

}
