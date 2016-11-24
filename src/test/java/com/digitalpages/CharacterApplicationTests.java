package com.digitalpages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharacterApplicationTests {
	
	private static final String USER_AGENT = "";

	@Test
	public void shouldGenerateMD5() throws NoSuchAlgorithmException {
		int ts = 1;
		String publicKey = "8cd6fdc39c0d5e3479e9b35a3dd505e2";
		String privateKey = "c34a70f52d253bff491c4ba7e7794254dd118b3b";
		StringBuffer seed = new StringBuffer();
		seed.append(ts);
		seed.append(publicKey);
		seed.append(privateKey);
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
	    md.update(seed.toString().getBytes());
	    
	    byte byteData[] = md.digest();
	    
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	System.out.println("Digest(in hex format):: " + hexString.toString());
    	
    	Assert.assertTrue( hexString.toString().matches("[a-fA-F0-9]{32}"));
	}

	@Test
	public void shouldConnect() throws IOException, NoSuchAlgorithmException {
		Long ts = new Date().getTime();
		String publicKey = "8cd6fdc39c0d5e3479e9b35a3dd505e2";
		String privateKey = "c34a70f52d253bff491c4ba7e7794254dd118b3b";
		StringBuffer seed = new StringBuffer();
		seed.append(ts.toString());
		seed.append(privateKey);
		seed.append(publicKey);

		MessageDigest md = MessageDigest.getInstance("MD5");
		
	    md.update(seed.toString().getBytes());
	    
	  //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        
	    byte byteData[] = md.digest();

    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
		
		String url = "http://gateway.marvel.com:80/v1/public/characters?ts="+ts+"&apikey="+publicKey+"&hash="+hexString.toString();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        Assert.assertEquals(responseCode, 200 );
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());

	}

}
