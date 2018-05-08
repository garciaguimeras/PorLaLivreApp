package dev.blackcat.porlalivre.data.writers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.readers.JacksonReader;

public class SQLiteAnnounceWriter 
{

	public File decompress(File f)
	{
		try 
		{
			FileInputStream fis = new FileInputStream(f);
			GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(fis));
			File jsonFile = new File(f.getAbsoluteFile() + ".json");
			FileOutputStream fos = new FileOutputStream(jsonFile);
			
			byte[] buffer = new byte[1024];
			int count = zis.read(buffer);
			while (count != -1)
			{
				fos.write(buffer, 0, count);
				count = zis.read(buffer);
			}
			
			fos.close();
			zis.close();
			fis.close();
			
			return jsonFile;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void remove(File f)
	{
		f.delete();
	}	
	
	public int getTotalElements(File f)
	{
		JacksonReader reader = new JacksonReader();
		return reader.getTotalElements(f);
	}
	
	public void deserialize(File f, JacksonReader.Consumer sqliteConsumer)
	{
		JacksonReader reader = new JacksonReader();
		reader.read(f, Announce.class, sqliteConsumer);
	}
	
}
