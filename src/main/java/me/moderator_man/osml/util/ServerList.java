package me.moderator_man.osml.util;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.util.ArrayList;

public class ServerList
{
    private static final Gson gson = new Gson();

    private ArrayList<Server> servers;

    public ArrayList<Server> getServers()
    {
        return servers;
    }

    public static ServerList fetch()
    {
        try (CloseableHttpClient httpclient = HttpClients.createDefault())
        {
            String postEndpoint = "https://servers.api.legacyminecraft.com/api/v1/getServers?type=all&flags=true&icons=true";
            HttpGet httpPost = new HttpGet(postEndpoint);

            httpPost.setHeader("Accept", "application/json");

            HttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity responseEntity = response.getEntity();
                InputStream is = responseEntity.getContent();
                String contentEncoding = "UTF-8";
                String responseContent = IOUtils.toString(is, contentEncoding);
                return gson.fromJson(responseContent, ServerList.class);
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}