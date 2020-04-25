package io.adboss.platforms;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.RawAPIResponse;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
import io.adboss.dataconnection.DB;
import io.adboss.utils.qreah;


public class FBPage {

	private static final Logger log = Logger.getLogger(FBPage.class.getName());
	
	private Facebook facebook;
	private String idPage;
	private String ATPage;
	
	
	private String name;
	
	// category: 2500  
	// category_enum: LOCAL
	// Description: Local Business
	private String category_enum;
	
	
	private String about;
	private String picture;		  // e.g.: https://marketboss-201812.appspot.com/images/cover.jpg
	private String cover_photo;   //e.g.: {url: 'https://marketboss-201812.appspot.com/images/facebook-cover-photo.jpg'}
	private String phone;
	private String email;		//e.g.: ['rafael@marketboss.online']
	private String website;
	private JSONObject location;	//e.g.: {'city': 'Madrid', 'country': 'ES', 'state': 'MA', 'street': 'C/Romero Robledo, 1', 'zip': '28008' }
	private String storeCode;
	private String City;
	private String Country;
	private String State;
	private String Street;
	private String ZipCode;	
	
	
	public FBPage() {
		
	}
	
	
	/**
	 * It is a constructor that identifies the Page in a light mode: idPage and
	 * facebook object asociated. It is light because you don't download all the
	 * varibles of the page as location, phone, email,... You have to use
	 * method getPage()
	 * 
	 */
	
	public FBPage(String username) throws JSONException, ClassNotFoundException, ServletException, IOException, SQLException, FacebookException {
		FB fb = new FB();
		facebook = fb.getFacebook(username);
		idPage = getIdPage(username);
		ATPage = getATPage();		
	}
	
	/**
	 * This constructor is used so you don't have to make an API call
	 * to Facebook. That way you won't consume unnecesary calls
	 * 
	 * @param facebookIn Facebook Object of the user
	 * @param username User, that way you can directly go to DB and get the PageId
	 * @throws ClassNotFoundException ok
	 * @throws ServletException ok
	 * @throws IOException ok
	 * @throws SQLException ok
	 * @throws FacebookException ok
	 * @throws JSONException ok
	 */
	
	public FBPage(Facebook facebookIn, String username) throws ClassNotFoundException, ServletException, IOException, SQLException, FacebookException, JSONException {
		facebook = facebookIn;
		idPage = getIdPage(username);
		ATPage = getATPage();	
	}
	
	
	/**
	 * Returns the name of the Fabook Page of the client
	 * 
	 * @return Name of the Facebook page
	 * @ann MyAnnotation  There's a reason
	 */
	
	public String getName() {
		String Name = "";
		if (name!=null) {
			Name = name;
		}
		return Name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean updateName() throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("name", getName());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public String getCategory_enum() {
		String Category = "";
		if (category_enum!=null) {
			Category = category_enum;
		}
		return Category;
	}
	public void setCategory_enum(String category_enum) {
		this.category_enum = category_enum;
	}
	
	
	// Category update uses 'category_list (e.g.: category_list=['2500']) instead that
	// category_enum
	public boolean updateCategory(String list) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("category_list", list);
		params.put("access_token", ATPage);
		qreah q = new qreah();
		q.enviarMail("qreahme@gmail.com", "updateCategory", ATPage + " | category_enum: " + getCategory_enum() + " | id: " + idPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public String getAbout() {
		String About = "";
		if (about!=null) {
			About = about;
		}
		return About;
	}
	public void setAbout(String about) {
		this.about = about;
		
	}
	
	public boolean updateAbout(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("about", getAbout());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getCover_photo() {
		return "{url: '" + cover_photo + "'}";
		
	}
	public void setCover_photo(String cover_photo) {
		this.cover_photo = cover_photo;
	}
	public String getPhone() {
		String Phone = "";
		if (phone!=null) {
			Phone = phone;
		}
		return Phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public boolean updatePhone(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("phone", getPhone());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public boolean updatePhone(Facebook facebook, String idPage, String phone) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public String getEmail() {
		String Email = "";
		if (email!=null) {
			Email = email;
		}
		return Email;
	}
	public void setEmail(String eMail) throws JSONException {
		this.email = eMail;
	}
	
	public boolean updateEmails(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		// Only just one email admitted
		params.put("emails", "['" + getEmail() + "']");
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public boolean updateEmail(Facebook facebook, String idPage, String email) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		// Only just one email admitted
		params.put("emails", "['" + email + "']");
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	
	public String getWebsite() {
		String Website = "";
		if (website!=null) {
			Website = website;
		}
		return Website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public boolean updateWebsite(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("website", getWebsite());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public JSONObject getLocation() throws JSONException {
		JSONObject json = new JSONObject();
		if (location!=null) {
			json = location;
		} else {
			json.put("city", "");
			json.put("country", "");
			json.put("state", "");
			json.put("street", "");
		}
		return json;
	}
	public void setLocation(JSONObject fbPage) {
		this.location = fbPage;
	}
	
	public boolean updateLocation(Facebook facebook, String idPage, String location) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("location", location);
		params.put("access_token", ATPage);
		
		qreah q = new qreah();
		q.enviarMail("qhreame@gmail.com", "Location", params.toString());
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public boolean updateCity(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		String city2 = getCity();
		
		params.put("city", city2);
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public boolean updateCountry(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("country", getCountry());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public boolean updateState(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("state", getState());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public boolean updateStreet(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		params.put("street", getStreet());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	
	public String getCity() throws JSONException {
		String city = "";
		if (City!=null) {
			city = City;
		}
		 
		return city;
	}
	
	public void setCity(String city) throws JSONException {
		
		City = city;
	}
	
	public String getCountry() throws JSONException {
		String country = "";
		if (Country!=null) {
			country = Country;
		}
		return country;
	}
	
	public void setCountry(String country) throws JSONException {
		Country = country;
	}
	
	public String getState() throws JSONException {
		String state = "";
		if (State!=null) {
			state = State;
		}
		return state;
	}
	
	public void setState(String state) throws JSONException {
		State = state;
	}
	
	public String getStreet() throws JSONException {
		String street = "";
		if (Street!=null) {
			street = Street;
		}
		return street;
	}
	
	public void setZipCode(String zipcode) throws JSONException {
		ZipCode = zipcode;
	}
	
	public String getZipCode() throws JSONException {
		String zipcode = "";
		if (ZipCode!=null) {
			zipcode = ZipCode;
		}
		return zipcode;
	}
	
	public void setStreet(String street) throws JSONException {
		Street = street;
	}
	
	public String getStoreCode() throws JSONException {
		return this.storeCode;
	}
	
	public void setStoreCode() throws JSONException {
	
		Random rand = new Random();
		int r1 = rand.nextInt((9 - 0) + 1) + 0;
		int r2 = rand.nextInt((9 - 0) + 1) + 0;
		int r3 = rand.nextInt((9 - 0) + 1) + 0;
		int r4 = rand.nextInt((9 - 0) + 1) + 0;
		int r5 = rand.nextInt((9 - 0) + 1) + 0;
		this.storeCode = "MB-" + r1 + r2 + r3 + r4 + r5;
		
	}
	
	
	public String getIdPage(String username) throws ClassNotFoundException, ServletException, IOException, SQLException {
		String idPage = "";
		DB db = new DB();
		idPage = db.getIdFBPage(username);
		return idPage;
	}
	
	public void setIdPage(String username, String idPage) throws ClassNotFoundException, SQLException, ServletException, IOException {
		DB db = new DB();
		db.setFBIdPage(username, idPage);
	}
	
	
	public String getATPage() throws FacebookException, JSONException {
		
		RawAPIResponse resFB = facebook.callGetAPI("me/accounts");
		JSONArray data;
		String access_token = null;
		data = resFB.asJSONObject().getJSONArray("data");
		int size = data.length();
		for (int i=0; i<size; i++) {
			JSONObject page = data.getJSONObject(i);
			String id = page.getString("id");
			if (id.equals(idPage)) {
				access_token = page.getString("access_token");
			}
			
		}
		
		return access_token;
	}
	
	
	public FBPage getPage() throws FacebookException, JSONException {
		
		try {
			RawAPIResponse resPage = facebook.callGetAPI(idPage + "?fields=name,about,picture,phone,emails,website,location,genre&access_token=" + ATPage);
			JSONObject jsonObjectPage = resPage.asJSONObject();
			
			name = jsonObjectPage.getString("name");
			about = jsonObjectPage.getString("about");
			picture = jsonObjectPage.getString("picture");
			phone = jsonObjectPage.getString("phone");
			email = jsonObjectPage.getJSONArray("emails").getString(0);	
			website = jsonObjectPage.getString("website");
			location = jsonObjectPage.getJSONObject("location");
			storeCode = jsonObjectPage.getString("genre");
			
			this.setName(name);
			this.setAbout(about);
			this.setPicture(picture);
			this.setPhone(phone);
			this.setEmail(email);
			this.setStoreCode();
			this.setCity(location.getString("city"));
			this.setCountry(location.getString("country"));
			try{ this.setState(location.getString("state"));} catch (JSONException e){this.setState("");}
			this.setStreet(location.getString("street"));
			this.setZipCode(location.getString("zip"));
			
		} catch (JSONException e) {
			
		} 
		return this;
	}
	
	public boolean updateStoreCode() throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		String store_code = getStoreCode();
		params.put("genre", store_code);
		params.put("access_token", ATPage);
		qreah q = new qreah();
		q.enviarMail("qreahme@gmail.com", "updateStoreCode", "store_code: " + store_code + " | atpage: " + ATPage + " | idPage: " + idPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	
	/**
	 * 	Deprecated: We are not allowed to create Pages
	 * 
	 * @param facebook: facebook object
	 * @param ATF: access token for the user
	 * @return Id Page
	 */
	
	
	public String createPage(Facebook facebook, String ATF) throws FacebookException, JSONException {
		String idPage = null;
		
		setStoreCode();
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", getName());
		params.put("category_enum", getCategory_enum());
		params.put("about", getAbout());
		params.put("picture", getPicture());
		params.put("location", getLocation().toString());
		params.put("cover_photo", getCover_photo());
		params.put("phone", getPhone());
		params.put("website", getWebsite());
		//String location = getLocation().toString();
		
		//params.put("location", location);
		params.put("access_token", ATF);
		
		
		RawAPIResponse res = facebook.callPostAPI("me/accounts", params);
		
		idPage = res.asJSONObject().getString("id");
		String atpage = getATPage();
		qreah q = new qreah();
		q.enviarMail("qreahme@gmail.com", "location", "location: " + location + " | idPage: " + idPage + " | atpage: " + atpage);
		//	Update category_list in Graph Explorer: 1874557059509867?category_list=['2500']
		//	We choose Local Business as Category. This may change but for now is the only
		//	category that we are setting
		updateCategory("['2500']");
		updateEmails(facebook, idPage);
		updateStoreCode();
		String Location = "{'city': '" + getCity() + "', 'country': '" + getCountry() + "', 'state': '" + getState() + "', 'street': '" + getStreet() + "', 'zip': '" + getZipCode() + "'}";
		updateLocation(facebook, idPage, Location);
		
		return idPage;
	}
	
	
	public boolean updatePage() throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		//params.put("name", getName());
		
		//params.put("category_enum", getCategory_enum());
		//params.put("about", "Hola");
		
		//params.put("picture", getPicture());
		//log.info("picture: " + getPicture());
		//params.put("cover_photo", getCover_photo());
		//params.put("phone", "915433838");
		//log.info("phone: " + getPhone());
		//params.put("emails", getEmails().toString());
		//log.info("emails: " + getEmails().toString());
		params.put("website", "fontaneros.grinboss.com");
		//log.info("website: " + getWebsite());
		//params.put("location", getLocation().toString());
		//log.info("location: " + getLocation().toString());
		params.put("access_token", ATPage);
		
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	
	public boolean updatePage(Facebook facebook, String idPage) throws FacebookException, JSONException {
		boolean updated = false;
		Map<String, String> params = new HashMap<String, String>();
		params = new HashMap<String, String>();
		//params.put("name", getName());
		//params.put("category_enum", getCategory_enum());
		params.put("about", getAbout());
		//params.put("picture", getPicture());
		//params.put("cover_photo", getCover_photo());
		params.put("phone", getPhone());
		params.put("emails", "['" + getEmail() + "']");
		params.put("website", getWebsite());
		//params.put("location", getLocation().toString());
		params.put("access_token", ATPage);
		updated = (boolean) facebook.callPostAPI(idPage, params).asJSONObject().get("success");
		
		return updated;
	}
	
	public void setVariables(JSONObject fbPage, String username) throws JSONException {
		// Setup  Page variables
		String cover = "https://dev.adboss.io/images/fbcover.jpg";
		String fbImage = "https://dev.adboss.io/images/fbimage.png";
		this.setName(fbPage.getString("PageName"));
		this.setCategory_enum("Business Service");
		this.setAbout(fbPage.getString("Description"));
		this.setPhone(fbPage.getString("PrimaryPhone"));
		this.setWebsite(fbPage.getString("WebSiteUrl"));
		this.setEmail(username);
		this.setPicture("https://dev.adboss.io/images/fbimage.png");
		this.setCover_photo("https://dev.adboss.io/images/fbcover.jpg");
		this.setStoreCode();
		this.setCity(fbPage.getString("City"));
		this.setCountry(fbPage.getString("Country"));
		this.setState(fbPage.getString("State"));
		this.setStreet(fbPage.getString("Street"));
		this.setZipCode(fbPage.getString("Zip"));
		
		JSONObject locationJson = new JSONObject();
		locationJson.put("city", this.getCity());
		locationJson.put("street", this.getStreet());
		locationJson.put("state", this.getState());
		locationJson.put("country", this.getCountry());
		locationJson.put("zip", this.getZipCode());
		//locationJson.put("picture", page.getPicture());
		this.setLocation(locationJson);
	}
	
	
	@SuppressWarnings("null")
	public ArrayList mbPages() throws FacebookException {
		ArrayList<String> pages = new ArrayList<String>();
		String storeCode = null;
		String check = "MB-";
		
		// Get all the Pages from the facebook user
		RawAPIResponse resFB = facebook.callGetAPI("me/accounts");
		
		JSONArray data;
		try {
			data = resFB.asJSONObject().getJSONArray("data");
		
		int size = data.length();
		
		
		for (int i=0; i<size; i++) {
			JSONObject page = data.getJSONObject(i);
			String id = page.getString("id");
			String ATPage = page.getString("access_token");
			
			
			// Check that it is a marketBoss page
			RawAPIResponse resPage = facebook.callGetAPI(id + "?fields=genre&access_token=" + ATPage);
			try {
				storeCode = resPage.asJSONObject().getString("genre");
				
				if (storeCode.startsWith(check)) {
					pages.add(id);
					
				}	
			} catch (JSONException e) {
				
			} 
		}
		} catch (JSONException e) {
			
		} 
		
		return pages;
		
	}
	
	
	

	
	/**
	 * Used when you want to know how many pages has the user. In the App
	 * he will be using just one.
	 * 
	 * @return List of Pages that belong to the facebook User
	 * @throws FacebookException
	 */
	
	public JSONArray listPages() throws FacebookException {
		
		JSONArray pages = new JSONArray();
		// Get all the Pages from the facebook user
		RawAPIResponse resFB = facebook.callGetAPI("me/accounts");
		JSONArray data;
		try {
			data = resFB.asJSONObject().getJSONArray("data");
			int size = data.length();
			for (int i=0; i<size; i++) {
				JSONObject page = data.getJSONObject(i);
				String idPage = page.getString("id");
				String ATPage = page.getString("access_token");
				String name = page.getString("name");
				JSONObject param = new JSONObject();
				param.put("id", idPage);
				param.put("ATPage", ATPage);
				param.put("name", name);
				pages.put(param);
				
			}
		} catch (JSONException e) {
			
		} 
		
		return pages;
		
	}
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		String name = "Grinboss1234";
		int lenName = name.length();
		int code = Integer.parseInt(name.substring(8, lenName));
		System.out.println(code);
		System.out.println(name.substring(0, 8));
	}
	
}
