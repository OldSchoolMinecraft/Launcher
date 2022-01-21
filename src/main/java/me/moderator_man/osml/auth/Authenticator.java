package me.moderator_man.osml.auth;

public abstract class Authenticator
{
    public abstract void authenticate();
    public abstract AuthenticationResult getResult();
}
