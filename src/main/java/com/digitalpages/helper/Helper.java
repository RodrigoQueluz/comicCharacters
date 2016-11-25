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

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.digitalpages.dao.ComicCharacterDAOImpl;
import com.digitalpages.dao.ComicDAOImpl;
import com.digitalpages.dao.ComicsCharactersDAOImpl;
import com.digitalpages.model.Comic;
import com.digitalpages.model.ComicCharacter;
import com.digitalpages.model.ComicsCharacters;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Helper {

	private static final String USER_AGENT = "";
	private Long ts;
	private String publicKey;
	private String privateKey;
	private String hash;
	private String params;

	ComicCharacterDAOImpl characterComicDAO = new ComicCharacterDAOImpl();
	ComicDAOImpl comicDAO = new ComicDAOImpl();
	ComicsCharactersDAOImpl charactersComicsDAO = new ComicsCharactersDAOImpl();
	
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
//		initiateDB();
		this.ts = ts;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		
		this.hash = this.generateMD5();
		this.params = "ts="+ts+"&apikey="+publicKey+"&hash="+hash;
		
		String url = "http://gateway.marvel.com:80/v1/public/characters?"+params;

		JsonElement root = enviaRequest(url);
        
        JsonArray results = root.getAsJsonObject().get("data").getAsJsonObject().get("results").getAsJsonArray();
       
        for(int i=0;i<results.size();i++){
        	JsonElement element = results.get(i);
        	ComicCharacter character = proccesCharacter(element);
        	JsonArray comics = element.getAsJsonObject().get("comics").getAsJsonObject().get("items").getAsJsonArray();
            for(int j=0;j<comics.size();j++){
            	JsonElement elementComic = comics.get(j);
            	Comic comic = proccessComic(elementComic);
            	ComicsCharacters cc = new ComicsCharacters();
            	cc.setCharacter(character);
            	cc.setComic(comic);
            	comicDAO.save(comic);
            	charactersComicsDAO.save(cc);
            }
        }
	}
	
	private ComicCharacter proccesCharacter(JsonElement element) throws ParseException {
		ComicCharacter character = new ComicCharacter();
    	character.setDescription(element.getAsJsonObject().get("description").getAsString());
    	character.setName(element.getAsJsonObject().get("name").getAsString());
    	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    	Date date = df1.parse(element.getAsJsonObject().get("modified").getAsString());
    	character.setLastUpdate(date);
    	//"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784","extension":"jpg"
    	StringBuffer thumbnail = new StringBuffer();
    	thumbnail.append(element.getAsJsonObject().get("thumbnail").getAsJsonObject().get("path").getAsString());
    	thumbnail.append(".");
    	thumbnail.append(element.getAsJsonObject().get("thumbnail").getAsJsonObject().get("extension").getAsString());
    	character.setThumb(thumbnail.toString());
    	
    	characterComicDAO.save(character);
    	
    	return character;
	}

	private JsonElement enviaRequest(String url) throws IOException {
		StringBuilder result = new StringBuilder();
		
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

        return new JsonParser().parse(result.toString());
	}

	private Comic proccessComic(JsonElement elementComic) throws IOException {
		Comic comic = new Comic();
		
    	String url = elementComic.getAsJsonObject().get("resourceURI").getAsString()+"?"+params;

    	JsonElement root = enviaRequest(url);
		JsonObject dataElement = root.getAsJsonObject().get("data").getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
    	
		String title = dataElement.getAsJsonObject().get("title").getAsString();
    	String issue = dataElement.getAsJsonObject().get("issueNumber").getAsString();
    	comic.setTitle(title);
    	comic.setIssue(issue);
		comic.setDescription(dataElement.get("description").isJsonNull() ? "" : dataElement.get("description").getAsString());
    	StringBuffer thumbnail = new StringBuffer();
    	thumbnail.append(dataElement.getAsJsonObject().get("thumbnail").getAsJsonObject().get("path").getAsString());
    	thumbnail.append(".");
    	thumbnail.append(dataElement.getAsJsonObject().get("thumbnail").getAsJsonObject().get("extension").getAsString());
    	comic.setThumb(thumbnail.toString());
    	return comic;
	}

	public void initiateDB(){
		new EmbeddedDatabaseBuilder()
	    		.setName("database")
	    		.setType(EmbeddedDatabaseType.HSQL)
	    		.addScript("db/sql/create-db.sql")
	    		.build();
    }

}
