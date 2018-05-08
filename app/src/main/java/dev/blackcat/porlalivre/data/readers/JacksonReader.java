package dev.blackcat.porlalivre.data.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.blackcat.porlalivre.data.JSONData;

public class JacksonReader 
{
	
	public interface Consumer
	{
		public void consume(JSONData obj, int count);
	}
	
	public final <T extends JSONData> int getTotalElements(File f)
	{
		int objectCounter = 0;

		try 
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonParser parser = mapper.getFactory().createParser(f);
			
			JsonToken token = parser.nextToken();
			if (token == null || token != JsonToken.START_ARRAY)
				return 0;
			
			int arrayCounter = 1;
			int objectsStarted = 0;
			while (token != null && arrayCounter > 0) 
			{
				if (token == JsonToken.START_ARRAY)
					arrayCounter++;
				if (token == JsonToken.END_ARRAY)
					arrayCounter--;			
				if (token == JsonToken.START_OBJECT)
				{
					objectsStarted++;
					if (objectsStarted == 1)
						objectCounter++;
				}
				if (token == JsonToken.END_OBJECT)
					objectsStarted--;
				token = parser.nextToken();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			objectCounter = 0;
		}
		
		return objectCounter;
	}	
	
	public final <T extends JSONData> void read(File f, Class<T> clazz, Consumer consumer)
	{
		try 
		{
			read(new FileInputStream(f), clazz, consumer);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public final <T extends JSONData> void read(InputStream is, Class<T> clazz, Consumer consumer)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonParser parser = mapper.getFactory().createParser(is);
			
			JsonToken token = parser.nextToken();
			if (token == null || token != JsonToken.START_ARRAY)
				return;
			token = parser.nextToken();
			int count = 0;
			while (token != null && token != JsonToken.END_ARRAY) 
			{
				T obj = parser.readValueAs(clazz);
				count++;
				consumer.consume(obj, count);
				token = parser.nextToken();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
