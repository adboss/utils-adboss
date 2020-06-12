package io.adboss.platforms;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Permission;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import io.adboss.dataconnection.DB;
import io.adboss.utils.qreah;

public class FB {
	
	private static final Logger log = Logger.getLogger(FB.class.getName());
	qreah q = new qreah();
	
	public Facebook getFacebook(String username) throws ServletException, IOException, ClassNotFoundException, SQLException, FacebookException {
		DB db = new DB();
		String accessToken;
		accessToken = db.getATF(username);
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
		
		/*
		Iterator<Permission> iterator = facebook.getPermissions().iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			//log.info(element.toString());
		}
		*/
		return facebook;
		
	}
	
	
	

	public static void main(String[] args) {
		

	}

}
