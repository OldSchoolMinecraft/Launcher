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
		"liblwjgl.so",
		"liblwjgl64.so",
		"libopenal.so",
		"libopenal64.so",
		"libjinput-linux.so",
		"libjinput-linux64.so",
	};
	
	public static final String[] libraries = new String[]
	{
		//"launchwrapper.jar",
		//"jopt-simple.jar", // https://libraries.minecraft.net/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar
		//"asm-all.jar", // https://libraries.minecraft.net/org/ow2/asm/asm-all/4.1/asm-all-4.1.jar
		"jinput.jar",
		//"jutils.jar", // https://libraries.minecraft.net/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar
		"lwjgl.jar",
		"lwjgl_util.jar",
		"json.jar",
		//"minecraft.jar"
	};
}
