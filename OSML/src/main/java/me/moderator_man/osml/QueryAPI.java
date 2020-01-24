package me.moderator_man.osml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
	
	public static String get(String url)
    {
        try
        {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
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
