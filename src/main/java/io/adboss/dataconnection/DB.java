package io.adboss.dataconnection;


import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.json.JSONObject;

import com.google.apphosting.api.ApiProxy;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



/*
 *  Goal: to manage the interface with the databases
 */
public class DB {

		private Connection conn;
		private String DB = "apiadbossDB.Users";
		private String FBPages = "apiadbossDB.fbpages";
		private String DBVariables = "apiadbossDB.cv_variables";
		private String DBOAUTH ="apiadbossDB.oauth20";
		private String locationTable = "apiadbossDB.locations";
		private static final Logger log = Logger.getLogger(DB.class.getName());
		String dataBase = "apiadbossDB";
		String instanceConnectionName = "";
		String roorPass = "rootadboss2018";
		
		public DB() throws ClassNotFoundException, ServletException, IOException, SQLException {
			//conn = ConnectDB();
			//instanceConnectionName = getInstanceConnectionName();
		}
		
		
		
		/*
		 *  Goal: to connect to a database
		 *  Input:
		 *  	- None. A database is specified inside the method
		 *  Output:
		 *  	- Connection. A Connection is activated
		 */
		
		public Connection ConnectDB() throws ServletException, ClassNotFoundException, IOException, SQLException {
			String url;
			ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
			
			// If your are not at App Engine environment, you work al localhost and
			// your driver url is different	
			InetAddress iAddress = InetAddress.getLocalHost();
	        String hostName = iAddress.getHostName();
	        
			if (hostName.equals("localhost")) {  
				Map<String,Object> attr = env.getAttributes();
			      String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");
			      url = hostname.contains("localhost:")
			          ? System.getProperty("cloudsql-local") : System.getProperty("cloudsql");
			      url = "jdbc:google:mysql://" + instanceConnectionName + "/" + dataBase + "?user=root&password=" + roorPass;
			      
				  
			
			// If you are on App Engine environment
			} else {   
		      
			  
			  Properties properties = new Properties();
				String PropiedadesConexion = "/resources/config.properties";
				//properties.load(getClass().getClassLoader().getResourceAsStream(PropiedadesConexion));
		        //url = properties.getProperty("sqlUrl");
				url = "jdbc:mysql://google/" + dataBase + "?cloudSqlInstance=" + instanceConnectionName + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=" + roorPass + "&useSSL=false";
				//url = "jdbc:mysql://google/gmb_db?cloudSqlInstance=mov-prod3:europe-west1:mov-gmb&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=FAYO0173&useSSL=false"; 
				
		    	
			}     
		      
			try {  
		    	
				//if (url == null)
				//	url = "jdbc:mysql://google/MB?cloudSqlInstance=marketboss-201812:europe-west1:marketboss&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=QuiqueRafa&useSSL=false";
				
		    	Class.forName("com.mysql.cj.jdbc.Driver");
		    	
		    	conn = DriverManager.getConnection(url); 
		        return conn;
		      } catch (SQLException e) {
		    	
		        throw new ServletException("Unable to connect to Cloud SQL", e);
		      }

			
			}
		
		public String getInstanceConnectionName() throws IOException {
			Properties properties = new Properties();
			properties.load(getClass()	
					.getClassLoader()
					.getResourceAsStream("names.sec"));
			return properties.getProperty("database");
		}

		   
		/*
		 *  Goal: to connect to a database in a servlet context
		 *  Input:
		 *  	- String PropiedadesConexion: jdbc url
		 *  	- ServletContext contexto: Servlet context
		 *  Output:
		 *  	- Connection. A Connection is activated
		 */
		
		
		public Connection ConnectDB(String PropiedadesConexion, ServletContext contexto) {
			//Método cuyo objetivo es conectarse a una base de datos desde un servlet
			
			String url = "";
		      
		    Properties properties = new Properties();
		    try {
		        properties.load(contexto.getResourceAsStream(PropiedadesConexion));
		        url = properties.getProperty("sqlUrl");
		        System.out.println("url: " + url);
		    } catch (IOException e) {
		    	System.out.println("No hay configuración del JDBC en el fichero de propiedades");
		        System.out.println(e);
		        
		    }

		    System.out.println("connecting to: " + url);
		    try {
		        Class.forName("com.mysql.cj.jdbc.Driver");
		        System.out.println("url: " + url);
		        conn = DriverManager.getConnection(url); 
		        
				  
		    } catch (ClassNotFoundException e) {
		    	System.out.println("Error loading JDBC Driver");
		        System.out.println(e);
		        
		    } catch (SQLException e) {
		    	System.out.println("Unable to connect to PostGre");
		        System.out.println(e);  
		        
		    }
		    return conn;
		}
		
		
		/*
		 *  Goal: to execute a SELECT statement given a Connection (conn)
		 *  Input:
		 *  	- String SQL: Select SQL Statement
		 *  	- Connection conn: Connection accessing to a database
		 *  Output:
		 *  	- Resultset: Object with the results from the SQL Statement
		 */
		
		public ResultSet ExecuteSELECT(String SQL, Connection conn) throws SQLException {
			
			if (conn == null) {
				
				return null;
			} else {
				Statement secuencia = conn.createStatement(); 
				ResultSet rs = (ResultSet) secuencia.executeQuery(SQL);
				return rs;
			}
		}
		
		public ResultSet ExecuteSELECT(String SQL) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = ConnectDB();
			if (conn == null) {
				
				return null;
			} else {
				Statement secuencia = conn.createStatement(); 
				ResultSet rs = (ResultSet) secuencia.executeQuery(SQL);
				return rs;
			}
		}
		
		
		/*
		 *  Goal: to execute a INSERT statement given a Connection (conn)
		 *  Input:
		 *  	- String SQL: Select SQL Statement
		 *  	- Connection conn: Connection accessing to a database
		 *  Output:
		 *  	- None. Some registers are added to the table
		 */
		
		public boolean Execute(String SQL, Connection conn)  {
			boolean executed = false;
			Statement secuencia;
			
			try {
				secuencia = conn.createStatement();
				secuencia.executeUpdate(SQL);
				executed = true;
			} catch (SQLException e) {
				executed = false;
				log.info("Ha dado error");
				e.printStackTrace();
			} 
			return executed;
		}
		
		
		public boolean Execute(String SQL) throws SQLException, ClassNotFoundException, ServletException, IOException  {
			boolean executed = false;
			conn = ConnectDB();
			Statement secuencia;
			
			
			try {
				secuencia = conn.createStatement();
				secuencia.executeUpdate(SQL);
				executed = true;
			} catch (SQLException e) {
				executed = false;
				log.info("Ha dado error");
				e.printStackTrace();
			} 
			return executed;
		}
		
		public void createUser(String User, String Pass, String Company, Connection conn) throws SQLException {
			
			String UserInsert = "insert into " + DB + " (User, Pass, Company) VALUES ('" + User + "', '" + Pass + "', '" + Company + "')";
			Execute(UserInsert, conn);
		}
		
		public boolean checkIfSameWebCompanyName(String WebCompanyName) throws SQLException {
			boolean result = false;
			String SQL = "SELECT WebName FROM " + DB;
			
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String webName = "";
			while (rs.next()) {
				webName = rs.getString("WebName");
				
				if (webName != null) {
					if (webName.equals(WebCompanyName)) {
						result = true;
					}
					
				}
				
			}
				
			return result;
				
		}
		
		public String getWebName(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT WebName FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String webName = "";
			String webNameComplete = "";
			while (rs.next()) {
				webName = rs.getString("WebName").toLowerCase();
			}
			webNameComplete = "http://" + webName;
			conn.close();		
			return webNameComplete;
		}
		
		
		public boolean setWebName(String username, String WebName) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET WebName = '" + WebName.toLowerCase() +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		
		/*
		 *  Goal: to know if an user exists in order not to add up a new one if already exists
		 *  Input:
		 *  	- String user: username
		 *  	- Connection conn: Connection accessing to a database
		 *  	
		 *  Output:
		 *  	- boolean. if the user exists, true; if not, false
		 */
		
		public boolean UserExists(String user, Connection conn) throws SQLException {
			
			String SQL = "SELECT User FROM " + DB +  " WHERE User = '" + user + "'";
			
			System.out.println(SQL);
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String userInDB = "";
			while (rs.next()) {
				userInDB = rs.getString("User");
			}
			if (userInDB.equals(user)) {
				
				return true;
			} else {

				
				return false;
			}
	
			
			
		}
		
		
		/*	Goal: Check if the user and password that the user provides is corrrect
		 * 	Input:
		 * 		- User: User provided by the user who wants to login in
		 * 		- Password: Password provided by the user who wants to login in
		 * 		- Conn: Connection with the SQL database
		 * 		
		 * 	Output
		 * 		- boolean: true if user and password are correct, false otherwise
		 */
		
		public boolean checkUserPass(String user, String Password, Connection conn) throws SQLException {
			
			String userInTable = "SELECT User, Pass FROM " + DB +  " WHERE User = '" + user + "' AND Pass = '" + Password + "'";
			
			ResultSet rs = ExecuteSELECT(userInTable, conn);
			String userInDB = "";
			String PassInDB = "";
			boolean check = false;
			while (rs.next()) {
				userInDB = rs.getString("User");
				PassInDB = rs.getString("Pass");
			
				if (userInDB.equals(user)) {
					if (PassInDB.equals(Password)) {	
						return true;
					}
				} 
			}
			
			return check;
			
			
		}


		public String getATT(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT ATT FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String ATT = "";
			while (rs.next()) {
				ATT = rs.getString("ATT");
			}
			conn.close();		
			return ATT;
		}
		
		public boolean setATT(String username, String ATT) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET ATT = '" + ATT +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		
		public String getATTSecret(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT ATTSecret FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String ATTSecret = "";
			while (rs.next()) {
				ATTSecret = rs.getString("ATTSecret");
			}
			conn.close();		
			return ATTSecret;
		}
		
		
		public boolean setATTSecret(String username, String ATTSecret) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET ATTSecret= '" + ATTSecret +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		
		public String getATF(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT ATF FROM " + DB +  " WHERE User = '" + username + "'";
			
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String ATF = "";
			while (rs.next()) {
				ATF = rs.getString("ATF");
			}
			conn.close();		
			return ATF;
		}
		
		public boolean setATF(String username, String ATF) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET ATF = '" + ATF +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			
			return result;
		}
		
		
		public String getIdFBPage(String username) throws ClassNotFoundException, ServletException, IOException, SQLException {
			String result = "";
			conn = this.ConnectDB();
			String SQL = "SELECT idPage FROM " + FBPages +  " WHERE username = '" + username + "'";
			
			ResultSet rs = ExecuteSELECT(SQL, conn);
			
			while (rs.next()) {
				result = rs.getString("idPage");
			}
			conn.close();	
			return result;
		}
		
		
		public String getNameFBPage(String username) throws ClassNotFoundException, ServletException, IOException, SQLException {
			String result = "";
			conn = this.ConnectDB();
			String SQL = "SELECT namePage FROM " + FBPages +  " WHERE username = '" + username + "'";
			
			ResultSet rs = ExecuteSELECT(SQL, conn);
			
			while (rs.next()) {
				result = rs.getString("namePage");
			}
			conn.close();	
			return result;
			
			
		}
		
		
		
		public String getATG(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT ATG FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String ATG = "";
			while (rs.next()) {
				ATG = rs.getString("ATG");
			}
			conn.close();		
			return ATG;
		}
		
		public boolean setATG(String username, String ATG) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET ATG = '" + ATG +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		
		public String getTWUserName(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT TWUserName FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String TWUserName = "";
			while (rs.next()) {
				TWUserName = rs.getString("TWUserName");
			}
			conn.close();		
			return TWUserName;
		}
		
		public String getFBUserName(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT FBUserName FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String FBUserName = "";
			while (rs.next()) {
				FBUserName = rs.getString("FBUserName");
			}
			conn.close();		
			return FBUserName;
		}
		
		
		public String getGOUserName(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String SQL = "SELECT GOUserName FROM " + DB +  " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String GOUserName = "";
			while (rs.next()) {
				GOUserName = rs.getString("GOUserName");
			}
			conn.close();		
			return GOUserName;
		}
		
		
		public boolean setTWUserName(String username, String TWUsername) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET TWUsername = '" + TWUsername +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		public boolean setFBUserName(String username, String FBUsername) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET FBUsername = '" + FBUsername +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		
		public boolean setGOUserName(String username, String GOUsername) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			boolean result = false;
			String SQL = "UPDATE " + DB + " SET GOUsername = '" + GOUsername +  "' WHERE User = '" + username + "'";
			
			conn = ConnectDB();
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		
		public boolean disconnectGMB(String username) throws ClassNotFoundException, ServletException, IOException, SQLException {
			boolean result = false;
			conn = this.ConnectDB();
			String SQL = "UPDATE " + DB + " SET ATG = null WHERE User = '" + username + "'";
			Execute(SQL, conn);
			SQL = "DELETE FROM " + locationTable + " WHERE username = '" + username + "'";
			Execute(SQL, conn);
			conn.close();
			result = true;
			return result;
		}
		

		public JsonArray toJsonArray(ResultSet rs) throws SQLException {
			JsonObject jObj = new JsonObject();
			JsonArray dao = new JsonArray();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
			        String columnName = rsmd.getColumnName(i);
			        String columnValue = rs.getString(i);
			        jObj.addProperty(columnName, columnValue);
			    }
				dao.add(jObj); 
				
			}
			return dao;
		}
		
		

		public String getGMBLocation(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = this.ConnectDB();
			String idLocal = null;
			String SQL = "SELECT idLocal FROM " + locationTable + " WHERE username = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL, conn);
			while (rs.next()) {
				idLocal = rs.getString("idLocal");
			}
			conn.close();
			return idLocal;
		}



		public String getFBPage(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			String result = "";
			String SQL = "SELECT idPage FROM apiadbossDB.fbpages WHERE username = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL);
			
			while (rs.next()) {
				result = rs.getString("idPage");
			}
			
			return result;
		}
		
		/**
		 * Set in the database the id of the facebook page
		 * It supposes that there is only one facebook page
		 * @return false if there has been an error and it's been imposible to set the facebook
		 * id page and true if everthing went ok and the facebook page has been set
		 */
		
		public boolean setFBIdPage(String username, String idPage) throws SQLException, ClassNotFoundException, ServletException, IOException {
			boolean result = false;
			String SQL = "SELECT idPage FROM apiadbossDB.fbpages WHERE username = '" + username + "'";
			
			ResultSet rs = ExecuteSELECT(SQL);
			
			if (rs.next()) {
				SQL = "UPDATE apiadbossDB.fbpages SET idPage = '" + idPage + "' WHERE username = '" + username + "'";
				
				Execute(SQL);
			} else {
				SQL = "INSERT INTO apiadbossDB.fbpages (username, idPage, namePage) VALUES ('" + username + "', '" + idPage + "')";
				
				Execute(SQL);
			}
			result = true;
			
			return result;
		}
		
		
		public String getTWidAd(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			String result = "";
			String SQL = "SELECT TWidAd FROM " + DB + " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL);
			
			while (rs.next()) {
				result = rs.getString("TWidAd");
			}
			return result;
		}

		
		public String getCompanyName(String username) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			String result = "";
			String SQL = "SELECT Company FROM " + DB + " WHERE User = '" + username + "'";
			ResultSet rs = ExecuteSELECT(SQL);
			
			while (rs.next()) {
				result = rs.getString("Company");
			}
			return result;
		}


		
		public boolean setTWidAd(String username, String TWidAd) throws ClassNotFoundException, SQLException, ServletException, IOException {
			boolean result = false;
			String SQL = "SELECT TWidAd FROM " + DB + " WHERE User = '" + username + "'";
			
			ResultSet rs = ExecuteSELECT(SQL);
			
			if (rs.next()) {
				SQL = "UPDATE " + DB + " SET TWidAd = '" + TWidAd + "' WHERE User = '" + username + "'";
				Execute(SQL);
			} else {
				SQL = "INSERT INTO " + DB + " (User, TWidAd) VALUES ('" + username + "', '" + TWidAd + "')";
				Execute(SQL);
			}
			result = true;
			
			return result;
		}
		
		public String getUserFromDomain(String domain) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			String result = "";
			String SQL = "SELECT User FROM " + DB + " WHERE WebName = '" + domain + "'";
			ResultSet rs = ExecuteSELECT(SQL);
			
			while (rs.next()) {
				result = rs.getString("User");
			}
			return result;
		}
		
		public JSONObject getOAUTH20Credentials(String SERVICE) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			conn = ConnectDB();
			JSONObject json = new JSONObject();
			String SQL = "SELECT SERVICE, CLIENT_ID, CLIENT_SECRET, OAUTH_SCOPE, REDIRECT_URI, REFRESH_TOKEN FROM " + DBOAUTH +  " WHERE"
					+ " SERVICE = '" + SERVICE + "'"
					;
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String output = "";
			String outputValue = "";
			while (rs.next()) {
				json.put("SERVICE", rs.getString("SERVICE"));
				json.put("CLIENT_ID", rs.getString("CLIENT_ID"));
				json.put("CLIENT_SECRET", rs.getString("CLIENT_SECRET"));
				json.put("OAUTH_SCOPE", rs.getString("OAUTH_SCOPE"));
				json.put("REDIRECT_URI", rs.getString("REDIRECT_URI"));
				json.put("REFRESH_TOKEN", rs.getString("REFRESH_TOKEN"));
			}
			conn.close();		
			return json;
		}
		
		public boolean setOAUTH20Credentials(
				String SERVICE,
				String CLIENT_ID, 
				String CLIENT_SECRET,
				String OAUTH_SCOPE,
				String REDIRECT_URI,
				String REFRESH_TOKEN
				) throws SQLException, ClassNotFoundException, ServletException, IOException {
			conn = ConnectDB();
			boolean result = false;
			String SQL = "SELECT CLIENT_ID FROM " + DBOAUTH + " WHERE SERVICE = '" + SERVICE + "'";
			
			ResultSet rs = ExecuteSELECT(SQL);
			
			if (rs.next()) {
				SQL = "UPDATE " + DBOAUTH + " SET "
						+ "CLIENT_ID = '" + CLIENT_ID + "' "
						+ ", CLIENT_SECRET = '" + CLIENT_SECRET + "' "
						+ ", OAUTH_SCOPE = '" + OAUTH_SCOPE + "' "
						+ ", REDIRECT_URI = '" + REDIRECT_URI + "' "
						+ ", REFRESH_TOKEN = '" + REFRESH_TOKEN + "' "
						+ "WHERE SERVICE = '" + SERVICE + "'";
				log.info(SQL);
				Execute(SQL);
			} else {
				SQL = "INSERT INTO " + DBOAUTH 
						+ " (SERVICE, CLIENT_ID, CLIENT_SECRET, OAUTH_SCOPE, REDIRECT_URI, REFRESH_TOKEN) VALUES ('" 
						+ SERVICE + "', '" 
						+ CLIENT_ID + "', '" 
						+ CLIENT_SECRET + "', '" 
						+ OAUTH_SCOPE + "', '" 
						+ REDIRECT_URI + "', '" 
						+ REFRESH_TOKEN + "')";
				Execute(SQL);
			}
			result = true;
			
			return result;
		}
		
		public JSONObject getOAUTH20(String SERVICE) throws SQLException, ClassNotFoundException, ServletException, IOException {
			
			conn = this.ConnectDB();
			JSONObject json = new JSONObject();
			String SQL = "SELECT SERVICE, CLIENT_ID, CLIENT_SECRET, OAUTH_SCOPE, REDIRECT_URI, REFRESH_TOKEN FROM " + DBOAUTH +  " WHERE"
					+ " SERVICE = '" + SERVICE + "'"
					;
			ResultSet rs = ExecuteSELECT(SQL, conn);
			String output = "";
			String outputValue = "";
			while (rs.next()) {
				json.put("SERVICE", rs.getString("SERVICE"));
				json.put("CLIENT_ID", rs.getString("CLIENT_ID"));
				json.put("CLIENT_SECRET", rs.getString("CLIENT_SECRET"));
				json.put("OAUTH_SCOPE", rs.getString("OAUTH_SCOPE"));
				json.put("REDIRECT_URI", rs.getString("REDIRECT_URI"));
				json.put("REFRESH_TOKEN", rs.getString("REFRESH_TOKEN"));
			}
			conn.close();		
			return json;
		}
		
		public void close() throws SQLException {
			conn.close();
		}
		
		public static void main(String[] args) throws Exception {
			DB db = new DB();
			log.info(db.getInstanceConnectionName());
		}
			
		
	}


