package io.adboss.dataconnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


public class APIaccess {
	
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Logger log = Logger.getLogger(APIaccess.class.getName());
		
	
	

	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ServletException {
		

	}

}
