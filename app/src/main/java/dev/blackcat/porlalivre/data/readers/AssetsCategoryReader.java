package dev.blackcat.porlalivre.data.readers;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.Category;


public class AssetsCategoryReader extends JSONReader 
{

	public List<Category> readCategories(Context context)
	{
		return read(context, "categories.json", Category.class);
	}

}
