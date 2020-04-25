package io.adboss.platforms;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mybusiness.v4.MyBusiness;
import com.google.api.services.mybusiness.v4.model.Account;
import com.google.api.services.mybusiness.v4.model.BusinessHours;
import com.google.api.services.mybusiness.v4.model.Category;
import com.google.api.services.mybusiness.v4.model.ListAccountsResponse;
import com.google.api.services.mybusiness.v4.model.ListLocationsResponse;
import com.google.api.services.mybusiness.v4.model.Location;
import com.google.api.services.mybusiness.v4.model.PostalAddress;
import com.google.api.services.mybusiness.v4.model.SearchGoogleLocationsRequest;
import com.google.api.services.mybusiness.v4.model.SearchGoogleLocationsResponse;
import com.google.api.services.mybusiness.v4.model.TimePeriod;

import io.adboss.dataconnection.APIaccess;



public class GMB {
	Connection conn;
	private static String accountRafa = "accounts/103268190540206787281";
	//private static String accountJumberStores = "accounts/110386967263955010373";
	private static String accountName = accountRafa;
	private static final Logger log = Logger.getLogger(GMB.class.getName());
	protected Writer W = new StringWriter();
	static JsonFactory JSON_FACTORY = new JacksonFactory();
	private static MyBusiness mybusiness;
	
	public GMB() throws ClassNotFoundException, SQLException, ServletException, IOException {
		mybusiness = APIaccess.connectGMB();
	}
	
	/**
	 * Returns a list of accounts.
	 * @return List A list of accounts.
	 * @throws Exception
	 */
	public static List<Account> listAccounts() throws Exception {
		
		MyBusiness.Accounts.List accountsList = mybusiness.accounts().list();
		ListAccountsResponse response = accountsList.execute();
		List<Account> accounts = response.getAccounts();

	  for (Account account : accounts) {
	    System.out.println(account.toPrettyString());
	  }
	  return accounts;
	}
			
			
	/*
	* Returns all locations for the specified account.
	* @param accountName The account for which to return locations.
	* @returns List A list of all locations for the specified account.
	*/
	public static List<Location> listLocations(String accountName) throws Exception {
		
		com.google.api.services.mybusiness.v4.MyBusiness.Accounts.Locations.List locationsList =
				mybusiness.accounts().locations().list(accountName);
		ListLocationsResponse responses = locationsList.execute();  
		List<Location> locations = responses.getLocations();
		//locations = responses.getLocations(); //borrar si no estás probando
			  
		while (responses.getNextPageToken() != null){
			locationsList.setPageToken(responses.getNextPageToken());
			responses = locationsList.execute();
			locations.addAll(responses.getLocations());
		} 
			  
		return locations;
	}

	
	
	
	
	/**
	 * Gets the location object given the location name. Format of locationName is
	 * "locations/8039004741124002913"
	 * @param locationName String that defines the location
	 * @return Location Object
	 * @throws Exception
	 */
		
	public static Location getLocation(String locationName) throws Exception {
		
		String name = accountName + "/" + locationName;
		MyBusiness.Accounts.Locations.Get location =
				mybusiness.accounts().locations().get(name);
		Location response = location.execute();
		return response;
	}
	
	/**
	 * Update the phone number of the location
	 * @param phone
	 * @param locationName: location name with the format: locations/8039004741124002913
	 * @return true if the process went well and false if it occurred a problem
	 */
	
	public static boolean updatePrimaryPhone(String phone, String locationName) {
		boolean updated = false;
		
		try {
			String name = accountName + "/" + locationName;
			Location location = getLocation(locationName);
			location.setPrimaryPhone(phone);
			MyBusiness.Accounts.Locations.Patch updateLocation =
		            mybusiness.accounts().locations().patch(name, location);
		    updateLocation.execute();
		    
		} catch (Exception e) {
				
				e.printStackTrace();
		}
			
		return updated;	
	}
	
			
	/**
	 * Update the address of the location
	 * @param Line1: Street, number of the Street
	 * @param Line2: Rest of the details in order to complete the address
	 * @param locationName: location name with the format: locations/8039004741124002913
	 * @return true if the process went well and false if it occurred a problem
	 */
	
	public static boolean updateStreet(	
		String Line1, 
		String Line2, 
		String locationName) {
		
		boolean updated = false;
		
		try {
			String name = accountName + "/" + locationName;
			PostalAddress address = new PostalAddress();
			List<String> addressLines = new ArrayList<String>();
			addressLines.add(Line1);
			addressLines.add(Line2);
			address.setAddressLines(addressLines);
			
			Location location = getLocation(locationName);
			location.setAddress(address);
			
			MyBusiness.Accounts.Locations.Patch updateLocation =
		            mybusiness.accounts().locations().patch(name, location);
		    updateLocation.execute();
		    
		} catch (Exception e) {
				
			updated = false;
			e.printStackTrace();
				
		}
		updated = true;	
		return updated;
	}	
	
	/**
	 * Updates the postal code of the location
	 * @param postalCode
	 * @param locationName: location name with the format: locations/8039004741124002913
	 * @return true if the process went well and false if it occurred a problem
	 */
	
	public static boolean updatePostalCode(String postalCode, String locationName) {
		
		boolean updated = false;
		try {
			
			String name = accountName + "/" + locationName;
			PostalAddress address = new PostalAddress();
			address.setPostalCode(postalCode);				
			Location location = getLocation(locationName);
			location.setAddress(address);
			
			MyBusiness.Accounts.Locations.Patch updateLocation =
		            mybusiness.accounts().locations().patch(name, location);
		    updateLocation.execute();
		    
		} catch (Exception e) {
			updated = false;
			e.printStackTrace();
		}
		updated = true;	
		return updated;
	}	
	
	
	/**
	 * Update the city of the location
	 * @param city
	 * @param locationName: location name with the format: locations/8039004741124002913
	 * @return true if the process went well and false if it occurred a problem
	 */
	
	public static boolean updateCity(String city, String locationName) {
				
			boolean updated = false;
			try {
				
				String name = accountName + "/" + locationName;
				PostalAddress address = new PostalAddress();
				address.setLocality(city);
				Location location = getLocation(locationName);
				location.setAddress(address);
				
				MyBusiness.Accounts.Locations.Patch updateLocation =
			            mybusiness.accounts().locations().patch(name, location);
			    updateLocation.execute();
			    
			} catch (Exception e) {
				updated = false;	
				e.printStackTrace();
			}
			updated = true;	
			return updated;
		}	
			
	/** 
	 * Update the name of the business. Used to name the store. It is used to distinguish between
	 * stores
	 * @param locationTitle: name of the store
	 * @param locationName: location name with the format: locations/8039004741124002913
	 * @return true if the process went well and false if it occurred a problem
	 */
	
	public static boolean updateTitle(String locationTitle, String locationName) {
					
		boolean updated = false;
		try {
			String name = accountName + "/" + locationName;
			Location location = getLocation(locationName);
			location.setLocationName(locationTitle);
			MyBusiness.Accounts.Locations.Patch updateLocation =
					mybusiness.accounts().locations().patch(name, location);
			updateLocation.execute();
				    
		} catch (Exception e) {	
			updated = false;
			e.printStackTrace();
		}
		updated = true;
		return updated;
	}
	
	
	/** 
	 * Update the website of the business. 
	 * @param websiteUrl: website url as String
	 * @param locationName: location name with the format: locations/8039004741124002913
	 * @return true if the process went well and false if it occurred a problem
	 */
		
	public static boolean updateWebsite(String websiteUrl, String locationName) {
		
		boolean updated = false;
		try {
			String name = accountName + "/" + locationName;
			Location location = getLocation(locationName);
			location.setWebsiteUrl(websiteUrl);
				
			MyBusiness.Accounts.Locations.Patch updateLocation =
					mybusiness.accounts().locations().patch(name, location);
			updateLocation.execute();
			    
		} catch (Exception e) {
			updated = false;
			e.printStackTrace();
		}
		updated = true;
		return updated;
	}
	
	/**
	 * Creates a new location.
	 * @param accountName The name (resource path) of the account to create a
	 *   location for.
	 * @return Location The data for the new location.
	 * @throws Exception
	 */

	public static Location createLocation(String accountName) throws Exception {
	    
	    // Street address
	    List addressLines = new ArrayList();
	    addressLines.add("Romero Robledo, 1");
	    PostalAddress address = new PostalAddress()
	        .setAddressLines(addressLines)
	        .setLocality("Madrid")
	        .setAdministrativeArea("Madrid")
	        .setRegionCode("ES")
	        .setPostalCode("28008");

	    // Business hours
	    List periods = new ArrayList<>();
	    List<String> days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

	    for (String day : days) {
	      TimePeriod period = new TimePeriod()
	          .setOpenDay(day)
	          .setOpenTime("9:00")
	          .setCloseTime("17:00")
	          .setCloseDay(day);
	      periods.add(period);
	    }
	    BusinessHours businessHours = new BusinessHours()
	        .setPeriods(periods);
	    
	    Location location = new Location()
	        .setAddress(address)
	        .setRegularHours(businessHours)
	        .setLocationName("Google Sydney")
	        .setStoreCode("GOOG-SYD")
	        .setPrimaryPhone("902 93 74 40")
	        .setPrimaryCategory(new Category().setDisplayName("Software Company").setCategoryId("gcid:software_company"))
	        .setLanguageCode("es")
	        .setWebsiteUrl("https://www.google.com.au/");
	    	
	    
	    MyBusiness.Accounts.Locations.Create createLocation =
	        mybusiness
	        .accounts()
	        .locations()
	        .create(accountName, location);
	    createLocation.setRequestId("1a84939c-ab7d-4581-8930-ee35af6fefac");
	    createLocation.setValidateOnly(false);	
	    createLocation.setRequestId(UUID.randomUUID().toString());
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("X-GOOG-API-FORMAT-VERSION", 2);
	    createLocation.setRequestHeaders(headers);
	    
	    SearchGoogleLocationsRequest content = new SearchGoogleLocationsRequest();
	    content.setResultCount(5);
	    //content.setLocation(location);
	    content.setQuery("romero robledo madrid");
	    SearchGoogleLocationsResponse res = mybusiness.googleLocations().search(content).execute();
	    
	    
	    //Location createdLocation = createLocation.execute();

	    //System.out.printf("Created Location:\n%s", createdLocation);
	    //return createdLocation;
	    return null;
	  }
			

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		//String accountName = "accounts/111004137622243995793";
		
		//Location newLoc = createLocation(accountName);
		//System.out.println("NewLocation: " + newLoc.getLocationName());
		//List<Location> listLoc = listLocations(accountName);
		//System.out.println("LocationName: " + listLoc.get(1).getName());
		//System.out.println("Location: " + listLoc.get(2).getLocationName());
		GMB gmb = new GMB();
		List<Location> listLoc = gmb.listLocations("accounts/103268190540206787281");
		//List<Location> listLoc = gmb.listLocations(accountJumberStores);
		
		int len = listLoc.size();
		System.out.println(len);
		for (int i = 0; i < len; i++) {
			System.out.println("Location: " + listLoc.get(i).getLocationName());
			System.out.println("LocationId: " + listLoc.get(i).getName());
			
		}
	
		
		List<Account> acc = gmb.listAccounts();
		int len2 = acc.size();
		System.out.println(len2);
		for (int i = 0; i < len2; i++) {
			System.out.println("Account: " + acc.get(i).getAccountName());
			System.out.println("LocationId: " + acc.get(i).getAccountNumber());
			System.out.println("LocationId: " + acc.get(i).getName());
			System.out.println("LocationId: " + acc.get(i).getPermissionLevel());
			System.out.println("LocationId: " + acc.get(i).getRole());
		}
		
		//System.out.println(listAccounts().toString());
		
		/*
		MyBusiness mybusiness = APIaccess.Connect2GMB("/client_secrets.json");
		int i = 5;
		String name = listLoc.get(i).getName();
		FetchVerificationOptionsRequest content = new FetchVerificationOptionsRequest();
		content.setLanguageCode("es");
		FetchVerificationOptions req = mybusiness.accounts().locations().fetchVerificationOptions(name, content);
		FetchVerificationOptionsResponse res = req.execute();
		
		
		List<VerificationOption> methods = res.getOptions();
		Iterator<VerificationOption> iter = methods.iterator();
		while(iter.hasNext()) {
			VerificationOption method = iter.next();
			
			
		}
		VerifyLocationRequest ver = new VerifyLocationRequest();
		AddressInput addressInput = new AddressInput();
		String mailerContactName = "Pepe Flores";
		addressInput.setMailerContactName(mailerContactName);
		ver.setMethod("ADDRESS");
		ver.setAddressInput(addressInput);
		VerifyLocationResponse verRes = mybusiness.accounts().locations().verify(name, ver).execute();
		*/
	}

			
}
