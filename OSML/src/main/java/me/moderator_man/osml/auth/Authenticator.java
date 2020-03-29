package me.moderator_man.osml.auth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import org.json.JSONObject;

public class Authenticator
{
	private boolean authenticated;
	private String sessionId;
	
	public Authenticator()
	{
		authenticated = false;
		sessionId = "";
	}
	
	public void tryAuth(String username, String password)
	{
		JSONObject obj = new JSONObject(get(String.format("http://api.oldschoolminecraft.com:8080/login?username=%s&password=%s", username, hash(password))));
		
		if (obj.has("sessionId"))
		{
			authenticated = true;
			sessionId = obj.getString("sessionId");
		} else {
			authenticated = false;
		}
	}
	
	public boolean isAuthenticated()
	{
		return authenticated;
	}
	
	public String getSessionID()
	{
		return sessionId;
	}
	
	private String hash(String base)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < hash.length; i++)
	        {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        return hexString.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	public String get(String url)
    {
        try
        {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            return response.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return "";
    }
}