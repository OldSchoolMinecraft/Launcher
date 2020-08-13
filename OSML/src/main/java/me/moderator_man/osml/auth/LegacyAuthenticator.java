package me.moderator_man.osml.auth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import me.moderator_man.osml.Main;
import me.moderator_man.osml.util.Util;

public class LegacyAuthenticator
{
    private boolean authenticated;
    private boolean mojang;
    private String sessionId;
    private String username;
    private String uuid;
    
    private boolean hasError;
    private String errorMessage;

    public LegacyAuthenticator()
    {
        authenticated = false;
        mojang = false;
        sessionId = "";
    }

    public String generateClientToken()
    {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Main.config.clientToken = token;
        Main.saveConfig();
        return token;
    }

    public void mojangAuth(String email, String password)
    {
        if (Main.config.clientToken == null || Main.config.clientToken.length() != 32)
        {
            mojangAuth(email, password, generateClientToken());
        } else {
            mojangAuth(email, password, Main.config.clientToken);
        }
    }

    public void mojangAuth(String email, String password, String clientToken)
    {
        JSONObject request = new JSONObject();
        JSONObject agent = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", 1);
        request.put("agent", agent);
        request.put("username", email);
        request.put("password", password);
        request.put("requestUser", true);
        
        JSONObject response = postJSON("https://authserver.mojang.com/authenticate", request);
        
        if (response.has("error"))
        {
            JOptionPane.showMessageDialog(null, "Authentication Failed: " + response.getString("error"));
            return;
        }
        
        if (!response.has("accessToken"))
        {
            hasError = true;
            errorMessage = "Authentication Failed: Access token missing from response! The full response has been logged to standard output.";
            System.out.println(response.toString());
            return;
        }
        
        if (!response.has("selectedProfile"))
        {
            hasError = true;
            errorMessage = "Authentication Failed: Profile data missing from response! The full response has been logged to standard output.";
            System.out.println(response.toString());
            return;
        }
        
        mojang = true;
        authenticated = true;
        sessionId = response.getString("accessToken"); //new JSONObject(new String(Base64.getDecoder().decode(response.getString("accessToken").split("\\.")[1]))).getString("yggt");
        username = response.getJSONObject("selectedProfile").getString("name");
        uuid = response.getJSONObject("selectedProfile").getString("id");
    }

    public void refreshToken(String accessToken)
    {
        if (Main.config.clientToken == null || Main.config.clientToken.length() != 32)
        {
            refreshToken(accessToken, generateClientToken());
        } else {
            refreshToken(accessToken, Main.config.clientToken);
        }
    }

    public void refreshToken(String accessToken, String clientToken)
    {
        JSONObject request = new JSONObject();
        request.put("accessToken", accessToken);
        request.put("clientToken", clientToken);
        
        JSONObject response = postJSON("https://authserver.mojang.com/refresh", request);
        
        if (response.has("error"))
        {
            //TODO: handle error
        }
    }

    public void tryAuth2(String username, String password)
    {
        JSONObject obj = new JSONObject(get(String.format("http://api.oldschoolminecraft.com:8080/v2/login?username=%s&password=%s", username, hash(password))));

        if (obj.has("sessionId"))
        {
            authenticated = true;
            sessionId = obj.getString("sessionId");
        } else {
            authenticated = false;
        }
    }

    public void tryAuth(String username, String password)
    {
        if (Util.isEmail(username))
        {
            mojangAuth(username, password);
            return;
        }
        
        JSONObject obj = new JSONObject(get(String.format("http://api.oldschoolminecraft.com:8080/login?username=%s&password=%s", username, hash(password))));

        if (obj.has("sessionId"))
        {
            authenticated = true;
            sessionId = obj.getString("sessionId");
            this.username = username;
        } else {
            authenticated = false;
        }
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
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }
    
    public boolean isMojang()
    {
        return mojang;
    }

    public String getSessionID()
    {
        return sessionId;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getUUID()
    {
        return uuid;
    }
    
    public boolean hasError()
    {
        return hasError;
    }
    
    public String getErrorMessage()
    {
        return errorMessage;
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

    public JSONObject postJSON(String url, JSONObject payload)
    {
        try
        {
            URL object = new URL(url);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(payload.toString());
            wr.flush();

            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                    sb.append(line + "\n");
                br.close();
                return new JSONObject(sb.toString());
            } else {
                JSONObject obj = new JSONObject();
                obj.put("error", "Server returned non-200 response: " + HttpResult);
                return obj;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("error", "Something went wrong: " + ex.getMessage());
            return obj;
        }
        /*JSONObject obj = new JSONObject();
        obj.put("error", "Can this even happen?");
        return obj;*/
    }
}
