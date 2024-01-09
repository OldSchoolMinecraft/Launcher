package me.moderator_man.osml.auth;

import java.util.UUID;

import org.json.JSONObject;

import me.moderator_man.osml.Main;
import me.moderator_man.osml.util.Util;

public class MojangAuthenticator extends Authenticator
{
    private Credentials credentials;
    private MojangAuthenticationResult result;
    
    public MojangAuthenticator(Credentials credentials)
    {
        this.credentials = credentials;
    }
    
    private String generateClientToken()
    {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Main.config.clientToken = token;
        Main.saveConfig();
        return token;
    }

    public void authenticate()
    {
        String token = "";
        
        if (Main.config.clientToken == null || Main.config.clientToken.length() != 32)
            token = generateClientToken();
        else
            token = Main.config.clientToken;
        
        JSONObject request = new JSONObject();
        JSONObject agent = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", 1);
        request.put("agent", agent);
        request.put("username", credentials.getUsername());
        request.put("password", credentials.getPassword());
        request.put("clientToken", token);
        request.put("requestUser", true);
        
        JSONObject response = Util.postJSON("https://authserver.mojang.com/authenticate", request);
        
        if (response.has("error") && response.has("errorMessage"))
        {
            result = new MojangAuthenticationResult(String.format("%s: %s", response.getString("error"), response.getString("errorMessage")));
            return;
        }
        
        String username = response.getJSONObject("selectedProfile").getString("name");
        String uuid = response.getJSONObject("selectedProfile").getString("id");
        String accessToken = response.getString("accessToken");
        result = new MojangAuthenticationResult(username, uuid, accessToken);
        
        //sessionId = response.getString("accessToken"); //new JSONObject(new String(Base64.getDecoder().decode(response.getString("accessToken").split("\\.")[1]))).getString("yggt");
        //username = response.getJSONObject("selectedProfile").getString("name");
        //uuid = response.getJSONObject("selectedProfile").getString("id");
    }

    public MojangAuthenticationResult getResult()
    {
        return result;
    }
}
