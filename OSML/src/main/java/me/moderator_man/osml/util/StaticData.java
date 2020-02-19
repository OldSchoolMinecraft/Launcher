package me.moderator_man.osml.util;

public class StaticData
{
	public static final String[] natives_windows = new String[]
	{
		"jinput-dx8.dll",
		"jinput-dx8_64.dll",
		"jinput-raw.dll",
		"jinput-raw_64.dll",
		"jinput-wintab.dll",
		"lwjgl.dll",
		"lwjgl64.dll",
		"OpenAL32.dll",
		"OpenAL64.dll"
	};
	
	public static final String[] natives_mac = new String[]
	{
		"jinput.jnilib",
		"lwjgl.jnilib",
		"openal.dylib"
	};
	
	public static final String[] natives_linux = new String[]
	{
		"jinput.so",
		"jinput64.so",
		"lwjgl.so",
		"lwjgl64.so",
		"openal.so",
		"openal64.so"
	};
	
	public static final String[] libraries = new String[]
	{
		"lwjgl.jar",
		"jinput.jar",
		"lwjgl_util.jar",
		"json.jar"
	};
}
