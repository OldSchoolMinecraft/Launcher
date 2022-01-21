package me.moderator_man.osml.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Credentials
{
    private String username;
    private String password;
    
    public Credentials(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public boolean isEmail()
    {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(username);
        return mat.matches();
    }
}
