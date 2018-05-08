package dev.blackcat.porlalivre.fragments;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.StringUtils;
import dev.blackcat.porlalivre.data.Announce;

public class AnnounceListAdapter extends BaseAdapter
{
	
	private static final int DESCRIPTION_MAX_LENGHT = 80;	
	
	LayoutInflater inflater;
	List<Announce> filteredAnnounces;
	Activity context;
	
	public AnnounceListAdapter(Activity context, List<Announce> filteredAnnounces)
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
			convertView = inflater.inflate(R.layout.cell_announce, null);
			
		final Announce announce = filteredAnnounces.get(position);
		
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
		textView = (TextView)convertView.findViewById(R.id.announceRowTitleTextView);
		textView.setText(announce.title);
		
		textView = (TextView)convertView.findViewById(R.id.announceRowPriceTextView);
		String currency = "$" + StringUtils.formatCurrency(announce.price);
		textView.setText(currency);
		
		textView = (TextView)convertView.findViewById(R.id.announceRowDescriptionTextView);
		textView.setText(StringUtils.cropText(announce.description, DESCRIPTION_MAX_LENGHT));
		
		return convertView;
	}

}