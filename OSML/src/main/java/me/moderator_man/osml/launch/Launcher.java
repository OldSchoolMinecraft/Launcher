package me.moderator_man.osml.launch;

public class Launcher
{
	public Launcher() {}
	
	public void launch(String[] parameters)
	{
		ClassPathBuilder cpb = new ClassPathBuilder();
		
		String[] libs = new String[] { "lwjgl.jar", "jinput.jar", "lwjgl_util.jar", "json.jar", "minecraft.jar" };
		
		for (String lib : libs)
		{
			cpb.addToClasspath(lib);
		}
		
		String classpath = cpb.getFinal();
		
		ProcessBuilder pb = new ProcessBuilder();
	}
}
