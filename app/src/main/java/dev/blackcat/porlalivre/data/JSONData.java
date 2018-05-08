package dev.blackcat.porlalivre.data;

import java.util.List;

public abstract class JSONData 
{

	public abstract JSONData find(List<? extends JSONData> list, Object value);
	
}
