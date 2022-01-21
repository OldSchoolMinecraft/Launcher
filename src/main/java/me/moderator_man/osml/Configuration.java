package me.moderator_man.osml;

import java.io.Serializable;

public class Configuration implements Serializable
{
	private static final long serialVersionUID = 1L;

	public boolean disableBitDepthFix;
	public boolean disableUpdate;
	public boolean rememberPassword;
	public boolean legacyUI;
	public boolean disableJavaCheck;
	public int ramMb;
	public String username;
	public String password;
	public String accessToken;
	public String clientToken;
}
