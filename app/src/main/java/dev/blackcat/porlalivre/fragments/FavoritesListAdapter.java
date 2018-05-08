package dev.blackcat.porlalivre.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.StringUtils;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.Category;

public class FavoritesListAdapter extends BaseAdapter
{

	private static final int DESCRIPTION_MAX_LENGHT = 80;

	LayoutInflater inflater;
	List<Announce> filteredAnnounces;
	Activity context;

	public FavoritesListAdapter(Activity context, List<Announce> filteredAnnounces)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.filteredAnnounces = filteredAnnounces;
	}

	@Override
	public int getCount() 
	{
		return filteredAnnounces.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return filteredAnnounces.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
			convertView = inflater.inflate(R.layout.cell_favorite, null);
			
		final Announce announce = filteredAnnounces.get(position);
		final Category category = new Category().findParent(Category.getCategories(context), announce.categoryId);
		
		final ImageView imageView = (ImageView)convertView.findViewById(R.id.starredImageView);
		if (announce.starred)
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumb_up_icon));
		else
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumb_down_icon));
		imageView.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				announce.starred = !announce.starred;
				Announce.update(context, announce);
				if (announce.starred)
					imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumb_up_icon));
				else
					imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumb_down_icon));
			}
		});

		TextView textView;
		textView = (TextView)convertView.findViewById(R.id.favoriteRowTitleTextView);
		textView.setText(announce.title);

		textView = (TextView)convertView.findViewById(R.id.favoriteRowCategoryTextView);
		textView.setText(category.title);
		
		textView = (TextView)convertView.findViewById(R.id.favoriteRowPriceTextView);
		String currency = NumberFormat.getCurrencyInstance().format(announce.price);
		textView.setText(currency);
		
		textView = (TextView)convertView.findViewById(R.id.favoriteRowDescriptionTextView);
		textView.setText(StringUtils.cropText(announce.description, DESCRIPTION_MAX_LENGHT));
		
		return convertView;
	}

}