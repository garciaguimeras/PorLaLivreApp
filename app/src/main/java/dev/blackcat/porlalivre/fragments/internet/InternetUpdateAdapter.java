package dev.blackcat.porlalivre.fragments.internet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dev.blackcat.porlalivre.R;

public class InternetUpdateAdapter extends BaseAdapter
{

	LayoutInflater inflater;
	ArrayList<UpdateInfo> updateList;
	Activity context;
	String lastDay;
	
	public InternetUpdateAdapter(Activity context, ArrayList<UpdateInfo> updateList)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.updateList = updateList;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		this.lastDay = df.format(calendar.getTime());
	}
	
	@Override
	public int getCount() 
	{
		return updateList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return updateList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		UpdateInfo info = updateList.get(position);

		if (info.date.equals(lastDay) && info.name.toUpperCase().startsWith("D"))
			return createTodayView(info);

		return createView(info);
	}

	private View createTodayView(UpdateInfo info)
	{
		View convertView = inflater.inflate(R.layout.cell_internet_update_today, null);

		String str = "";
		TextView textView = convertView.findViewById(R.id.internetUpdate1TextView);
		str = context.getString(R.string.text_internet_update_1);
		str = str.replace("${name}", info.name);
		textView.setText(str);

		textView = convertView.findViewById(R.id.internetUpdate2TextView);
		str = context.getString(R.string.text_internet_update_2);
		str = str.replace("${frequency}", info.frequency);
		textView.setText(str);

		textView = convertView.findViewById(R.id.internetUpdate5TextView);
		str = context.getString(R.string.text_internet_update_4);
		textView.setText(info.size);

		textView = convertView.findViewById(R.id.internetUpdate3TextView);
		str = context.getString(R.string.text_internet_update_5);
		str = str.replace("${totalUpdates}", info.totalUpdates);
		textView.setText(str);

		return convertView;
	}

	private View createView(UpdateInfo info)
	{
		View convertView = inflater.inflate(R.layout.cell_internet_update, null);

		String str = "";
		TextView textView = convertView.findViewById(R.id.internetUpdate1TextView);
		str = context.getString(R.string.text_internet_update_1);
		str = str.replace("${name}", info.name);
		textView.setText(str);

		textView = convertView.findViewById(R.id.internetUpdate4TextView);
		str = context.getString(R.string.text_internet_update_4);
		str = str.replace("${date}", info.date);
		textView.setText(str);

		textView = convertView.findViewById(R.id.internetUpdate2TextView);
		str = context.getString(R.string.text_internet_update_2);
		str = str.replace("${frequency}", info.frequency);
		textView.setText(str);

		textView = convertView.findViewById(R.id.internetUpdate3TextView);
		str = context.getString(R.string.text_internet_update_3);
		str = str.replace("${size}", info.size);
		str = str.replace("${totalUpdates}", info.totalUpdates);
		textView.setText(str);

		return convertView;
	}

}