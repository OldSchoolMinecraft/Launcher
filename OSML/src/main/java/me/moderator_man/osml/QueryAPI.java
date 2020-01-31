package me.moderator_man.osml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class QueryAPI
{
	public static News getNews()
	{
		String raw_response = get(Endpoints.NEWS_API);
		JSONObject obj = new JSONObject(raw_response);
		
		String news_title = obj.getString("news_title");
		String news_text = obj.getString("news_text");
		
		return new News(news_title, news_text);
	}
	
	public static String getNewSession(String username, String password)
	{
		JSONObject obj = new JSONObject(get(String.format("http://api.oldschoolminecraft.com:8080/login?username=%s&password=%s", username, hash(password))));
		return obj.has("sessionId") ? obj.getString("sessionId") : "";
	}
	
	private static String hash(String base)
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
	
	public static String get(String url)
    {
        try
        {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "GangBot/0");
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
