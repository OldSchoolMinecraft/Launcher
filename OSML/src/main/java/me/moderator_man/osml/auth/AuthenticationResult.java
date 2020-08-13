package me.moderator_man.osml.auth;

public abstract class AuthenticationResult
{
    public abstract boolean isAuthenticated();
    public abstract boolean hasError();
    public abstract String getErrorMessage();
    public abstract String getUsername();
    public abstract String getSessionID();
    public abstract String getUUID();
}
