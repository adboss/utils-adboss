package io.adboss.dataconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.logging.Logger;

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

	
	public static void main(String[] args) throws IOException {
		connectGMB();

	}

}
