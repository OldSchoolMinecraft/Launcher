package me.moderator_man.osml.redux.auth;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthUtility
{
    private static final Gson gson = new Gson();

    public YggdrasilAuthResponse makeRequest(YggdrasilAuthRequest request) throws IOException
    {
        HttpPost httpPost = new HttpPost("https://os-mc.net/api/v1/authenticate");
        StringEntity entity = new StringEntity(gson.toJson(request));
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(httpPost);
        return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8), YggdrasilAuthResponse.class);
    }
}
