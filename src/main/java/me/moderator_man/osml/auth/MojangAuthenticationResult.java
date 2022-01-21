package me.moderator_man.osml.auth;

public class MojangAuthenticationResult extends AuthenticationResult
{
    private boolean success;
    private boolean error;
    private String errorMessage;
    private String username;
    private String uuid;
    private String accessToken;
    
    public MojangAuthenticationResult(String errorMessage)
    {
        success = false;
        error = true;
        this.errorMessage = errorMessage;
    }
    
    public MojangAuthenticationResult(String username, String uuid, String accessToken)
    {
        success = true;
        error = false;
        errorMessage = "";
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
    }
    
    @Override
    public boolean isAuthenticated()
    {
        return success;
    }

    @Override
    public boolean hasError()
    {
        return error;
    }

    @Override
    public String getErrorMessage()
    {
        return errorMessage;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getSessionID()
    {
        return accessToken;
    }
    
    @Override
    public String getUUID()
    {
        return uuid;
    }
}
