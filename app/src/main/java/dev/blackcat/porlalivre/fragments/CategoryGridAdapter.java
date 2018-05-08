package dev.blackcat.porlalivre.fragments;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.StringUtils;
import dev.blackcat.porlalivre.data.Category;

public class CategoryGridAdapter extends BaseAdapter
{
	
	private static final int DESCRIPTION_MAX_LENGHT = 80;
	
	LayoutInflater inflater;
	List<Category> filteredCategories;
	Activity context;
	
	public CategoryGridAdapter(Activity context, List<Category> filteredCategories)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.filteredCategories = filteredCategories;
	}

	@Override
	public int getCount() 
	{
		return filteredCategories.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
			convertView = inflater.inflate(R.layout.cell_category, null);
			
		TextView textView;
		Category category = filteredCategories.get(position);
		
		textView = (TextView)convertView.findViewById(R.id.categoryTitleTextView);
		textView.setText(category.title + " >");
		
		textView = (TextView)convertView.findViewById(R.id.categoryDescriptionTextView);
		textView.setText(StringUtils.cropText(category.description, DESCRIPTION_MAX_LENGHT));
		
		return convertView;
	}
	


}