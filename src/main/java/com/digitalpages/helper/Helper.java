package com.digitalpages.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.digitalpages.dao.ComicCharacterDAOImpl;
import com.digitalpages.model.ComicCharacter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Helper {

	private static final String USER_AGENT = "";
	private Long ts;
	private String publicKey;
	private String privateKey;

	public String generateMD5() {
		StringBuffer hexString = new StringBuffer();
		StringBuffer seed = new StringBuffer();
		seed.append(ts.toString());
		seed.append(privateKey);
		seed.append(publicKey);

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(seed.toString().getBytes());
			// convert the byte to hex format method 2
			byte byteData[] = md.digest();

			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hexString.toString();
	}
	
	public void sendRequest(Long ts, String publicKey, String privateKey) throws IOException, ParseException{
		initiateDB();
		this.ts = ts;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		
		ComicCharacterDAOImpl characterComicDAO = new ComicCharacterDAOImpl();
		
		String hash = this.generateMD5();
		
        StringBuilder result = new StringBuilder();
		
		String url = "http://gateway.marvel.com:80/v1/public/characters?ts="+ts+"&apikey="+publicKey+"&hash="+hash;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream in = new BufferedInputStream(con.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line+"\n");
            }

        }
        
        System.out.println(result.toString());

        JsonElement root = new JsonParser().parse(result.toString());
        
        JsonArray results = root.getAsJsonObject().get("data").getAsJsonObject().get("results").getAsJsonArray();
       
        for(int i=0;i<results.size();i++){
        	JsonElement element = results.get(i);
        	ComicCharacter character = new ComicCharacter();
        	character.setDescription(element.getAsJsonObject().get("description").getAsString());
        	character.setName(element.getAsJsonObject().get("name").getAsString());
        	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        	Date date = df1.parse(element.getAsJsonObject().get("modified").getAsString());
        	character.setLastUpdate(date);
        	
        	characterComicDAO.save(character);

        }
        
	}
	public void initiateDB(){
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
	    		.setName("database")
	    		.setType(EmbeddedDatabaseType.HSQL)
	    		.addScript("db/sql/create-db.sql")
	    		.build();
    }

}
