package me.moderator_man.osml.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class FormatWriter<T>
{
	public void write(T target, String path)
	{
		try
		{
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path))))
			{
				oos.writeObject(target);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
