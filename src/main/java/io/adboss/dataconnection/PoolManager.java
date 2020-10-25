package io.adboss.dataconnection;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.adboss.utils.qreah;

public class PoolManager {
	
	  private static final Logger log = Logger.getLogger(PoolManager.class.getName());
	  private static String CLOUD_SQL_CONNECTION_NAME = "";
	  private static String DB_USER = "";
	  private static String DB_PASS = "";
	  private static String DB_NAME = "";
	
	public void startCredentials() throws IOException {
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader()
				.getResourceAsStream("names.sec"));
		CLOUD_SQL_CONNECTION_NAME = properties.getProperty("database"); 
		DB_USER = properties.getProperty("user"); 
		DB_PASS = properties.getProperty("pass"); 
		DB_NAME = properties.getProperty("name"); 
	}
	
	@SuppressFBWarnings(
		      value = "USBR_UNNECESSARY_STORE_BEFORE_RETURN",
		      justification = "Necessary for sample region tag.")
		  private HikariDataSource createConnectionPool() throws IOException {
			  
			  startCredentials();
			  
		    // [START cloud_sql_mysql_servlet_create]
		    // The configuration object specifies behaviors for the connection pool.
		    HikariConfig config = new HikariConfig();

		    // Configure which instance and what database user to connect with.
		    
		    //config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
		    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		    config.setJdbcUrl("jdbc:mysql://google/" + DB_NAME);
		    config.setUsername(DB_USER); // e.g. "root", "postgres"
		    config.setPassword(DB_PASS); // e.g. "my-password"

		    // For Java users, the Cloud SQL JDBC Socket Factory can provide authenticated connections.
		    // See https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory for details.
		    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
		    config.addDataSourceProperty("cloudSqlInstance", CLOUD_SQL_CONNECTION_NAME);

		    // ... Specify additional connection properties here.
		    // [START_EXCLUDE]

		    // [START cloud_sql_mysql_servlet_limit]
		    // maximumPoolSize limits the total number of concurrent connections this pool will keep. Ideal
		    // values for this setting are highly variable on app design, infrastructure, and database.
		    config.setMaximumPoolSize(10);
		    // minimumIdle is the minimum number of idle connections Hikari maintains in the pool.
		    // Additional connections will be established to meet this value unless the pool is full.
		    config.setMinimumIdle(0);
		    // [END cloud_sql_mysql_servlet_limit]

		    // [START cloud_sql_mysql_servlet_timeout]
		    // setConnectionTimeout is the maximum number of milliseconds to wait for a connection checkout.
		    // Any attempt to retrieve a connection from this pool that exceeds the set limit will throw an
		    // SQLException.
		    config.setConnectionTimeout(10000); // 10 seconds
		    // idleTimeout is the maximum amount of time a connection can sit in the pool. Connections that
		    // sit idle for this many milliseconds are retried if minimumIdle is exceeded.
		    config.setIdleTimeout(300000); // 5 minutes
		    // [END cloud_sql_mysql_servlet_timeout]

		    // [START cloud_sql_mysql_servlet_backoff]
		    // Hikari automatically delays between failed connection attempts, eventually reaching a
		    // maximum delay of `connectionTimeout / 2` between attempts.
		    // [END cloud_sql_mysql_servlet_backoff]

		    // [START cloud_sql_mysql_servlet_lifetime]
		    // maxLifetime is the maximum possible lifetime of a connection in the pool. Connections that
		    // live longer than this many milliseconds will be closed and reestablished between uses. This
		    // value should be several minutes shorter than the database's timeout value to avoid unexpected
		    // terminations.
		    
		    config.setMaxLifetime(5400000); // 90 minutes
		    //config.setMaxLifetime(60000); // 1 minute
		    
		    // [END cloud_sql_mysql_servlet_lifetime]

		    // [END_EXCLUDE]

		    // Initialize the connection pool using the configuration object.
		    HikariDataSource pool = new HikariDataSource(config);
		    // [END cloud_sql_mysql_servlet_create]
		    return pool;
		  }

	
	public HikariDataSource openPool(HttpServletRequest request) throws IOException {
		try {
    		startCredentials();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	HikariDataSource pool = (HikariDataSource) request.getServletContext().getAttribute("my-pool");
        if (pool == null) {
          try {
    		pool = createConnectionPool();
    		
    		log.info("pool open");
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}
          request.getServletContext().setAttribute("my-pool", pool);
        } else {
        	log.info("pool already open");
        	log.info(pool.getPoolName());
        }
	    
	    return pool;
	}
	
	public HikariDataSource getPool(HttpServletRequest request) throws IOException {
		HikariDataSource pool = (HikariDataSource) request.getServletContext().getAttribute("my-pool");
        if (pool == null) {
          try {
    		pool = createConnectionPool();
    		
    		log.info("pool open");
    		} catch (IOException e) {
    			
    			e.printStackTrace();
    		}
          request.getServletContext().setAttribute("my-pool", pool);
        } else {
        	log.info("pool already open");
        	log.info(pool.getPoolName());
        }
	    
		return pool;
	}
	
	public void closePool(HikariDataSource pool, HttpServletRequest request) {
		if (pool != null) {
            log.info("pool closed");
            log.info(pool.getPoolName());
            pool.close();
            request.getServletContext().setAttribute("my-pool", null);
        } else {
        	log.info("pool is null");
        }
	}
	
	public Cron cronInit(HttpServletRequest request, Double limit) {
		Cron cronFinal = new Cron();
		boolean first = false;
		boolean finished = false;
		qreah q = new qreah();
		request.getServletContext().setAttribute("limit", limit);
		String date = (String) request.getServletContext().getAttribute("date");
        Double counter = (Double) request.getServletContext().getAttribute("counter");
        if (counter == null) {
        	
        } 
        
        if ((date == null)) {
        	first = true;
        	date = q.today();
        	counter = 0.0;
        }
        
        if (!date.equals(q.today())) {
        	first = true;
        	date = q.today();
        	counter = 0.0;
    	}
        
        if (counter == limit) {
        	finished = true;
        }
        
        cronFinal.setFinished(finished);
        cronFinal.setFirst(first);
        cronFinal.setCounter(counter);
        
        return cronFinal;
	}
	
	
	public void cronEnd(Cron cron, HttpServletRequest request, HikariDataSource pool) {
		qreah q = new qreah();
		Double counter = cron.getCounter();
		if (counter == 3) {		
			Double finalCounter = 3.0;
			request.getServletContext().setAttribute("counter", finalCounter);
			closePool(pool, request);
		} else {
			counter = counter + 1;
			request.getServletContext().setAttribute("counter", counter);
		}
		request.getServletContext().setAttribute("date", q.today());
	}
	

	public static void main(String[] args) {
		

	}
	
	public class Cron {
		public boolean first;
		public boolean finished;
		public Double counter;
		
		public boolean isFirst() {
			return first;
		}
		public void setFirst(boolean first) {
			this.first = first;
		}
		public boolean isFinished() {
			return finished;
		}
		public void setFinished(boolean finished) {
			this.finished = finished;
		}
		public Double getCounter() {
			return counter;
		}
		public void setCounter(Double counter) {
			this.counter = counter;
		}
		
		
		
		
		
	}

}


