package me.moderator_man.osml.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

@SuppressWarnings("unchecked")
public class FormatReader<T>
{
	public T read(String path)
	{
		try
		{
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(path))))
			{
				return (T) ois.readObject();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
