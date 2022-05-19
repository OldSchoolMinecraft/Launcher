package me.moderator_man.osml.redux.auth;

public class YggdrasilAuthRequest
{
    public YggdrasilAuthRequestAgent agent = new YggdrasilAuthRequestAgent();
    public String username;
    public String password;

    public YggdrasilAuthRequest() {}

    public YggdrasilAuthRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    private static class YggdrasilAuthRequestAgent
    {
        public String name = "Minecraft";
        public int version = 1;
    }
}
