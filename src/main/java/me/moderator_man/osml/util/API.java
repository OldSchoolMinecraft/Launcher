package me.moderator_man.osml.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class API
{
    private String sessionId;
    
    public API(String sessionId)
    {
        this.sessionId = sessionId;
    }
    
    public String get_cosmetic(String username, String cosmetic_type)
    {
        JSONObject obj = new JSONObject(request("cosmetics/get_cosmetic", "username=" + username, "type=" + cosmetic_type));
        return obj.getString("value");
    }
    
    public void set_skin(String username, String skin)
    {
        //TODO
    }
    
    public void set_cloak(String username, String cloak)
    {
        //TODO
    }
    
    public void set_badge(String username, String badge)
    {
        //TODO
    }
    
    public void set_cosmetic(String username, String cosmetic_type, String value)
    {
        //TODO
    }
    
    public String request(String endpoint, String... parameters)
    {
        String base = "https://os-mc.net/api/";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++)
        {
            if (i == 0)
                sb.append("?" + parameters[i]);
            else
                sb.append("&" + parameters[i]);
        }
        String url = base + endpoint + sb.toString();
        return get(url);
    }

    private String get(String url)
    {
        try
        {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "OSM/0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            return response.toString();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }
}
