package me.moderator_man.osml;

import java.io.Serializable;

public class Configuration implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public boolean keepOpen;
	public boolean openOutput;
	public boolean forceUpdate;
	public boolean rememberPassword;
	public int ramMb;
}
