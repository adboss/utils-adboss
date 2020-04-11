package io.adboss.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vdurmont.emoji.EmojiParser;

import facebook4j.Application;
import facebook4j.Category;
import facebook4j.Place;
import facebook4j.Post;
import facebook4j.Privacy;
import facebook4j.ResponseList;
import facebook4j.Targeting;

import twitter4j.Status;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;


/*
 *  The goal of the Class is to provide some usefull methods of general purpose
 */ 

public class qreah {

	private static final Logger log = Logger.getLogger(qreah.class.getName());
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();
    
	public qreah() {
		
	}
	
	public String HTTP(String url) throws IOException {
		HttpRequestFactory requestFactory 
        = HTTP_TRANSPORT.createRequestFactory(
          (HttpRequest requestX) -> {
            requestX.setParser(new JsonObjectParser(JSON_FACTORY));
        });
		System.out.println(url);
		GenericUrl urlT = new GenericUrl(url);
		HttpRequest request = requestFactory.buildGetRequest(urlT);
		
		HttpResponse response = request.execute();
		String res = response.parseAsString();
		return res;
	}
	
	public String HTTP(String url, GoogleCredential credential) throws IOException {
		
		HttpRequestFactory requestFactory 
        = HTTP_TRANSPORT.createRequestFactory(credential);
		
		System.out.println(url);
		GenericUrl urlT = new GenericUrl(url);
		
		HttpRequest request = requestFactory.buildGetRequest(urlT);
		request.setRequestMethod("PUT");
		
		long len = 0;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength((long) 0);
		//headers.set("Content-Length", len);
		
		request.setHeaders(headers);
		HttpResponse response = request.execute();
		String res = response.parseAsString();
		return res;
	}
	
	/*
	 *   Goal: to send email from development project account (qhrear@gmail.com)
	 *   Input:
	 *   - String destinatario: email address you want to send email to
	 *   - String asunto: subject of the email
	 *   - String cuerpo: body of the email to be sent 
	 *   Output:
	 *   - None (an email is sent and can be seen in the development project account mailbox
	 */
	
	public void enviarMail(String destinatario, String asunto, String cuerpo) {
	    
	    //String remitente = "qhrear@gmail.com"; 
	    String remitente = "operations@grinboss.com";
	    String clave = "Fayo0173"; //Para la dirección nomcuenta@gmail.com

	    Properties props = System.getProperties();
	    	    
	    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
	    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
	    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
	    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google
	    props.put("mail.debug.auth", "true");
	    
	    Session session = Session.getInstance(props, new javax.mail.Authenticator() {

	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(remitente,clave);
	        }
	    });
	    
	    MimeMessage message = new MimeMessage(session);

	    try {
	        message.setFrom(new InternetAddress(remitente)); 
	        message.addRecipients(Message.RecipientType.TO, destinatario);//Se podrían añadir varios de la misma manera
	        message.setSubject(asunto);
	        message.setContent(cuerpo, "text/html");
	        Transport transport = session.getTransport("smtp");
	        transport.connect("smtp.gmail.com", remitente, clave);	        
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	    }
	    catch (MessagingException me) {
	        me.printStackTrace();   
	    }
	}
	
	/*
	 * 	TIME MANAGEMENT ***********************************************
	 */
	
	/*
	 *   Goal: to provide the actual hour from the Calendar
	 *   Input:
	 *   	- None
	 *   Output:
	 *   	- Hour at execution time
	 */
	
	public int hora() {
		
		Calendar calendario = new GregorianCalendar();
		int hora;
		hora =calendario.get(Calendar.HOUR_OF_DAY);
		calendario.get(Calendar.MINUTE);
		calendario.get(Calendar.SECOND);
		return hora;
	}
	
	/*
	 *   Goal: to provide the actual minute from the Calendar
	 *   Input:
	 *   	- None
	 *   Output:
	 *   	- Minute at execution time
	 */
	
	public int minutos() {
		
		Calendar calendario = new GregorianCalendar();
		int minutos;
		calendario.get(Calendar.HOUR_OF_DAY);
		minutos = calendario.get(Calendar.MINUTE);
		calendario.get(Calendar.SECOND);
		return minutos;
	}

	
	/*
	 *   Goal: to provide the actual second from the Calendar
	 *   Input:
	 *   	- None
	 *   Output:
	 *   	- Second at execution time
	 */
	
	public int segundos() {
	
	Calendar calendario = new GregorianCalendar();
	int segundos;
	calendario.get(Calendar.HOUR_OF_DAY);
	calendario.get(Calendar.MINUTE);
	segundos = calendario.get(Calendar.SECOND);
	return segundos;
	}
	
	public long getTimestamp() {
		Calendar cal = new GregorianCalendar();
		long date = cal.getTimeInMillis();
		return date;
	}
	
	public List<String> getDatesNowAndLessXDays(int days){
		List<String> dates = new ArrayList<String>();
		Date date = new java.util.Date();
		TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		Date date2 = calendar.getTime();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'").format(date);
		String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'").format(date2);
		dates.add(timeStamp);
		dates.add(timeStamp2);
		
		return dates;
	}
	
	public String addDays(String initDate, int days) throws ParseException{
		String finalDate = "";
		Date date  = new SimpleDateFormat("yyyy-MM-dd").parse(initDate);  
		TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);
		Date dateFinal = calendar.getTime();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(dateFinal);
		finalDate = timeStamp;
		
		return finalDate;
	}
	
	public List<String> getDateRange(String start, String end) throws ParseException {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = df.parse(start); 
		Date endDate = df.parse(end);
		
        List<String> ret = new ArrayList<String>();
        Date tmp = startDate;
        while(tmp.before(endDate) || tmp.equals(endDate)) {
        	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(tmp);
            ret.add(timeStamp);
            long ltime=tmp.getTime()+1*24*60*60*1000;
            tmp = new Date(ltime);
        }
        log.info(ret.toString());
        return ret;
    }
	
	
	
	/**
	 * Get the day of the month in String type
	 * 
	 * @param date
	 * @return String with the date of the month
	 */
	
	public String day(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return String.valueOf(day);
	}
	
	public String today() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		date = date.substring(0, 10);
		date = date.replace("/", "-");
		return date;
	}
	
	public String todayLess(int days) {
		String dateS = null;
		Date date = new java.util.Date();
		TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date date2 = calendar.getTime();
		String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'").format(date2);
		dateS = timeStamp2.substring(0, 10);
		return dateS;
	}
	
	/**
	 * Get the month of the date in String type
	 * 
	 * @param date
	 * @return String with the month of the year
	 */
	
	public String month(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		return String.valueOf(month);
	}
	
	/**
	 * Get the year of the date in String type
	 * 
	 * @param date
	 * @return String with the year
	 */
	
	public String year(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return String.valueOf(year);
	}
	
	
	
	
	/*  Goal: to provide the actual time(Hour, Minutes, Seconds) from the Calendar
	 *   Input:
	 *   	- None
	 *   Output:   	
	 *   - hh:mm:ss at execution time
	 */ 	
	
	public String horaTotal() {
		
		Calendar calendario = new GregorianCalendar();
		int hora, minutos, segundos;
		hora = calendario.get(Calendar.HOUR_OF_DAY);
		minutos = calendario.get(Calendar.MINUTE);
		segundos = calendario.get(Calendar.SECOND);
		return hora + ":" + segundos + ":" + minutos;
	}
	
	
	
	public List<String> get30LastDays() {
		List<String> result = new ArrayList<String>();
		List<String> datesEnd = getDatesNowAndLessXDays(-1);
		List<String> datesStart = getDatesNowAndLessXDays(-30);
		String endTime = datesEnd.get(1).substring(0, 10);
		String startTime = datesStart.get(1).substring(0, 10);
		result.add(startTime);
		result.add(endTime);
		return result;
	
	}
	
	/**
	 * I don't know why when you ask Facebook for date 2019-12-06, he will
	 * give you 2019-12-05. Google Insights has the restriction that has no 
	 * data for the last date
	 * @return list of dates with format 2019-12-31
	 */
	
	public List<String> get30LastDaysFB() {
		List<String> result = new ArrayList<String>();
		List<String> datesEnd = getDatesNowAndLessXDays(-1);
		List<String> datesStart = getDatesNowAndLessXDays(-30);
		String endTime = datesEnd.get(1).substring(0, 10);
		String startTime = datesStart.get(1).substring(0, 10);
		result.add(startTime);
		result.add(endTime);
		return result;
	
	}
	
	/**
	 * 	Compares a date string (date) in format "yyyy-mm-dd" with another fate in string
	 * 	at the same format (dateRef)
	 * @throws ParseException 
	 */
	
	public boolean isGreater(String date, String dateRef) throws ParseException {
		
		boolean result = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateDate = format.parse(date);
		Date dateRefDate = format.parse(dateRef);
		if(dateDate.after(dateRefDate)) {
		    result = true;
		}

		
		return result;
	}
	
	
	/*
	 * 	Change format from 'Sun Jan 26 19:06:57 +0000 2020' to '2020-01-26'
	 */
	
	public String changeDateFormat(String originDate) {
		String result = "";
		String day = originDate.substring(8, 10);
		String mounth = originDate.substring(4, 7);
		switch(mounth) {
		  case "Jan":
		    mounth = "01";
		    break;
		  case "Feb":
			    mounth = "02";
			    break;
		  case "Mar":
			    mounth = "03";
			    break;
		  case "Apr":
			    mounth = "04";
			    break;
		  case "May":
			    mounth = "05";
			    break;
		  case "Jun":
			    mounth = "06";
			    break;
		  case "Jul":
			    mounth = "07";
			    break;
		  case "Ago":
			    mounth = "08";
			    break;
		  case "Sep":
			    mounth = "09";
			    break;
		  case "Oct":
			    mounth = "10";
			    break;
		  case "Nov":
			    mounth = "11";
			    break;
		  case "Dec":
			    mounth = "12";
			    break;
		  
		  default:
			  mounth = "13";
		}
		String year = originDate.substring(26, 30);
		result = year + "-" + mounth + "-" + day;
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
	
	public JsonArray toJsonArray(ResponseList<Post> RespList) {
		
		
		JsonArray dao = new JsonArray();
		
		String Description = null;
		String Caption = null;
		String id = null;
		String message = null;
		String name = null;
		String parentId = null;
		String objectId = null;
		String statusType = null;
		String story = null;
		String type = null;
		
		Category From = null;
		Place place = null;
		Privacy privacy = null;
		Targeting targeting = null;
		boolean isPublished = false;
		Application application = null;
		
		URL source = null;
		URL urlFullPicture = null;
		URL urlIcon = null;
		URL urlLink = null;
		URL PermalinkUrl = null;
		URL urlPicture = null;
		
		Date createdTime = null;
		Date scheduledPublishTime = null;
		Date updatedTime = null;
		
		int sharesCount;
		
		
		int postArraySize = RespList.size();
		for (int i=0; i < postArraySize; i++) {
			JsonObject jObj = new JsonObject();
			//String 
			Description = RespList.get(i).getDescription();
			jObj.addProperty("Description", Description);
			log.info("Description: " + Description);
			
			Caption = RespList.get(i).getCaption();
			jObj.addProperty("Caption", Caption);
			
			id = RespList.get(i).getId();
			jObj.addProperty("id", id);
			
			message = RespList.get(i).getMessage();
			jObj.addProperty("message", message);
			
			jObj.addProperty("Platform", "Facebook");
			
			name = RespList.get(i).getName();
			jObj.addProperty("name", name);
			parentId = RespList.get(i).getParentId();
			jObj.addProperty("parentId", parentId);
			objectId = RespList.get(i).getObjectId();
			jObj.addProperty("objectId", objectId);
			statusType = RespList.get(i).getStatusType();
			jObj.addProperty("statusType", statusType);
			story = RespList.get(i).getStory();
			jObj.addProperty("story", story);
			type = RespList.get(i).getType();
			jObj.addProperty("type", type);
			
			//Date
			createdTime = RespList.get(i).getCreatedTime();
			addProperty(jObj, createdTime, "createdTime");
			
			scheduledPublishTime = RespList.get(i).getScheduledPublishTime();
			addProperty(jObj, scheduledPublishTime, "scheduledPublishTime");
			
			updatedTime = RespList.get(i).getUpdatedTime();
			addProperty(jObj, updatedTime, "updatedTime");
			
			//int
			//sharesCount = RespList.get(i).getSharesCount().intValue();
			//addProperty(jObj, sharesCount, "sharesCount", sharesCount);
			
			//Other
			From = RespList.get(i).getFrom();
			addProperty(jObj, From, "FromCategory");
			
			
			place = RespList.get(i).getPlace();
			addProperty(jObj, place, "place");
			
			
			privacy = RespList.get(i).getPrivacy();
			targeting = RespList.get(i).getTargeting();
			
			//isPublished = RespList.get(i).isPublished();
			//jObj.addProperty("isPublished", isPublished);
			
			application = RespList.get(i).getApplication();
			addProperty(jObj, application, "application");
			                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
			//Url
			source = RespList.get(i).getSource();
			addProperty(jObj, source, "source");
			
			urlFullPicture = RespList.get(i).getFullPicture();
			addProperty(jObj, source, "urlFullPicture");
			
			urlIcon = RespList.get(i).getIcon();
			addProperty(jObj, urlIcon, "urlIcon");
			
			urlLink = RespList.get(i).getLink();
			addProperty(jObj, urlLink, "urlLink");
			
			PermalinkUrl = RespList.get(i).getPermalinkUrl();
			addProperty(jObj, PermalinkUrl, "PermalinkUrl");
			
			urlPicture = RespList.get(i).getPicture();
			addProperty(jObj, urlPicture, "urlPicture");
			
			RespList.get(i).getActions(); //List<Action>
			RespList.get(i).getAttachments(); //List<Attachment>
			RespList.get(i).getComments(); //PagableList<Comment>
			RespList.get(i).getLikes();  //PagableList<Likes>
			RespList.get(i).getMessageTags(); //List<Tag>
			RespList.get(i).getProperties(); //Lis<Properties>
			RespList.get(i).getReactions(); //PagableList<Reaction>
			RespList.get(i).getStoryTags(); //Map<String,Tag[]>
			RespList.get(i).getTo(); //List<IdNameEntity>
			RespList.get(i).getWithTags(); //List<IdNameEntity>
			
			//log.info("Nuevo dao " + i + ": " + dao.set(i, jObj));
			//dao.set(i+1, jObj);
			
			dao.add(jObj); 		
				
			log.info(i + ": jObj :" + jObj.toString());
			log.info(i + " :" + dao.toString());
			
		}
		
		return dao;
	}
	
	
	public JsonArray toJsonArray(List<Status> statusList) {

		JsonArray dao = new JsonArray();
		Iterator<Status> iter = statusList.iterator();
		while (iter.hasNext()) {
			Status status = iter.next();
			JsonObject jObj = new JsonObject();
			String Id = String.valueOf(status.getId());
			jObj.addProperty("Id", Id);
			String Text = status.getText();
			jObj.addProperty("Texto", Text);
			dao.add(jObj); 
			String Plataforma = "Twitter";
			jObj.addProperty("Plataforma", Plataforma);
			dao.add(jObj); 
			String User = status.getUser().getName();
			jObj.addProperty("User", User);
			dao.add(jObj); 
			
		}
		return dao;
	}
	
	/**
	 * Translate an ArrayList to JsonArray. Used when you want to send
	 * the object to the Front End
	 * @param array: object from Array<String>
	 * @return JsonArray: [{'id': 'xxxx'}, {'id': 'yyyy'}, {'id': 'zzzz'}}
	 */
	
	public JsonArray toJsonArray(ArrayList<String> array) {

		JsonArray dao = new JsonArray();
		Iterator<String> iter = array.iterator();
		int i = 0;
		while (iter.hasNext()) {
			i++;
			String idPage = iter.next();
			JsonObject jObj = new JsonObject();
			jObj.addProperty("Id", idPage);
			dao.add(jObj); 
			
		}
		return dao;
	}
	
	public void  addProperty(JsonObject jObj, Object Obj, String name) {
		if (Obj!=null) {
			jObj.addProperty(name, Obj.toString());
		} else {
			jObj.addProperty(name, "");
		}
	}
	
		
	public void  addProperty(JsonObject jObj, Category Obj, String name) {
		if (Obj!=null) {
			jObj.addProperty(name + "Category", Obj.getCategory());
			jObj.addProperty(name + "Id", Obj.getId());
			jObj.addProperty(name + "Name", Obj.getName());
		} else {
			jObj.addProperty(name + "Category", "");
			jObj.addProperty(name + "Id", "");
			jObj.addProperty(name + "Name", "");
		}
	}
	
	
	
	public void  addProperty(JsonObject jObj, Place Obj, String name) {
		if (Obj!=null) {
			jObj.addProperty(name + "Id", Obj.getId());
			jObj.addProperty(name + "Name", Obj.getName());
			jObj.addProperty(name + "City", Obj.getLocation().getCity());
			jObj.addProperty(name + "Country", Obj.getLocation().getCountry());
			jObj.addProperty(name + "State", Obj.getLocation().getState());
			jObj.addProperty(name + "Street", Obj.getLocation().getStreet());
			jObj.addProperty(name + "LocationText", Obj.getLocation().getText());
			jObj.addProperty(name + "LocationZip", Obj.getLocation().getZip());
			
		} else {
			jObj.addProperty(name + "Id", "");
			jObj.addProperty(name + "Name", "");
			jObj.addProperty(name + "City", "");
			jObj.addProperty(name + "Country", "");
			jObj.addProperty(name + "State", "");
			jObj.addProperty(name + "Street", "");
			jObj.addProperty(name + "LocationText", "");
			jObj.addProperty(name + "LocationZip", "");
		}
	}
	
	public void  addProperty(JsonObject jObj, Object Obj, String name, int value) {
		if (Obj!=null) {
			jObj.addProperty(name, value);
		} else {
			jObj.addProperty(name, "");
		}
	}
	
	/**
	 * Transform the body content from a POST request in JSON format to an JSONOBject
	 * @param POST request
	 * @return the JSONObject that defines the JSON object sent
	 */
	
	public JSONObject getContentFromPost(HttpServletRequest request) {
		
		StringBuilder jsonBuff = new StringBuilder();
		String line = null;
		try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		        jsonBuff.append(line);
		} catch (Exception e) { /*error*/ }
		
		JSONObject json = new JSONObject(jsonBuff.toString());
		return json;
		
	}
	
	/**
	 * To get a post message with utf8 format
	 * @throws IOException 
	 */
	
	/*
	 *  Important: you have to use the method before any request.getParameter(whatever);
	 */
	
	
	public String postUTF8Message(HttpServletRequest request) throws IOException {
		
		StringBuilder buffer = new StringBuilder();
		InputStream inputStream = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream , StandardCharsets.UTF_8));
	    String line;
	    
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }
	    log.info(buffer.toString());
	    return buffer.toString();
	}
	
	
	
	public void writeFile(String content) {
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("pruebaZ.txt");
            pw = new PrintWriter(fichero);
            pw.println(content);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	
	public String deleteEmoticon(String text) {
		
		String out = EmojiParser.removeAllEmojis(text);
		return out;
	}
	
	/**
	 * Gets a text and gets the first word
	 * @param text where the word is held
	 * @return
	 */
	
	
	public String extractFirstWord(String text) {
		String out = "";
		int len = text.length();
		for (int i=0; i<len; i++) {
			String character = text.substring(i, i+1);
			if (character.equals(" ")) {
				break;
			} else {
				out = out + character;
			}
		}
		return out;
	}
	
	public String cleanString(String text) {
		String out = "";
		out = deleteEmoticon(text);
		return out;
	}
	
	
	
public static void main (String [ ] args) throws MessagingException, ParseException, UnsupportedEncodingException {
	/*
	qreah q = new qreah();
	List<String> days = q.get30LastDays();
	System.out.println(days.get(0));
	System.out.println(days.get(1));
	
	qreah q = new qreah();
	String start = "2019-10-01";
	String end = "2019-10-10";
	List<String> days = q.getDateRange(start, end);
	int len = days.size();
	for (int i=0; i < len; i++) {
		System.out.println(days.get(i));
	}
	*/
	
	/*
	qreah q = new qreah();
	String date = "Sun Apr 03 19:06:57 +0000 2020";
	String newDate = q.changeDateFormat(date);
	log.info("len: " + newDate.length());
	log.info("h" + newDate + "h");
	*/
	
	/*
	qreah q = new qreah();
	String dateRef = "2020-02-20";
	String date = "2020-03-15";
	log.info("is: " + q.isGreater(date, dateRef));
	*/
	
	qreah q = new qreah();
	String text = "INSERT INTO apiadbossDB.DBRegisteredPosts VALUES ('1241414133330960384', 'Twitter', 'RT @Nopanaden: 👉🏻 5000 'camas para pacientes #COVID19";
	
	
	
}





}
