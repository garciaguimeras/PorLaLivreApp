package dev.blackcat.porlalivre.data.readers;

import java.util.ArrayList;

import dev.blackcat.porlalivre.data.JSONData;

public class ArrayListJacksonConsumer<T extends JSONData> implements JacksonReader.Consumer
{
	
	ArrayList<T> list = new ArrayList<T>();
	
	@Override
	public void consume(JSONData obj, int count) 
	{
		list.add((T)obj);
	}
	
	public ArrayList<T> getArrayList()
	{
		return list;
	}

}
