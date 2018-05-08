package dev.blackcat.porlalivre.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.Location;
import dev.blackcat.porlalivre.data.Site;
import dev.blackcat.porlalivre.data.Workshop;

public class WorkshopListAdapter extends BaseAdapter
{
	
	LayoutInflater inflater;
	Activity context;

	List<Workshop> workshops;
	
	public WorkshopListAdapter(Activity context)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.workshops = Workshop.getWorkshops(context);
	}

	@Override
	public int getCount() 
	{
		return workshops.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return workshops.get(position);
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
			convertView = inflater.inflate(R.layout.cell_workshop, null);
			
		TextView textView;
		Workshop workshop = workshops.get(position);
		
		textView = (TextView)convertView.findViewById(R.id.workshopNameTextView);
		textView.setText(workshop.name);
		
		textView = (TextView)convertView.findViewById(R.id.workshopAddressTextView);
		String text = workshop.address;
		if (workshop.locationId != -1)
		{
			Location location = (Location) new Location().find(Location.getLocations(context), workshop.locationId);
			text += ", " + location.name;
		}
		Site site = (Site) new Site().find(Site.getSites(context), workshop.siteId);
		text += ", " + site.name;
		textView.setText(text);		
		
		textView = (TextView)convertView.findViewById(R.id.workshopPhoneTextView);
		textView.setText(workshop.phone);
		
		textView = (TextView)convertView.findViewById(R.id.workshopEmailTextView);
		textView.setText(workshop.email);
		
		textView = (TextView)convertView.findViewById(R.id.workshopDescriptionTextView);
		textView.setText(workshop.description);	
		
		return convertView;
	}
	


}