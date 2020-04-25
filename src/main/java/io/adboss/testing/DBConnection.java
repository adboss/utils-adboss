package io.adboss.testing;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import io.adboss.dataconnection.DB;

public class DBConnection {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ServletException, IOException {
		DB db = new DB();
		String ATT = db.getATT("rafael@adarga.org");
		System.out.print(ATT);

	}

}
