package dev.blackcat.porlalivre.fragments.filesystem;

import java.io.File;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;

public class FileSelectorAdapter extends BaseAdapter
{
	
	LayoutInflater inflater;
	File[] files;
	Activity context;

	public FileSelectorAdapter(Activity context)
	{
		this.context = context;
	}

	public FileSelectorAdapter(Activity context, File[] files)
	{
		this.inflater = LayoutInflater.from(context);
		this.files = files;
	}
	
	public void setFiles(File[] files)
	{
		this.files = files;
	}

	@Override
	public int getCount() 
	{
		return files.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return files[position];
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
			convertView = inflater.inflate(R.layout.cell_file_selector, null);
			
		File f = files[position];

		ImageView imageView = convertView.findViewById(R.id.fileSelectorFolderImageView);
		BitmapDrawable bmp;
		if (f.isDirectory())
			bmp = (BitmapDrawable)convertView.getResources().getDrawable(R.drawable.folder_icon);
		else
			bmp = (BitmapDrawable)convertView.getResources().getDrawable(R.drawable.file_icon);
		imageView.setImageDrawable(bmp);

		TextView textView = convertView.findViewById(R.id.fileSelectorFileNameTextView);
		textView.setText(f.getName());
		
		return convertView;
	}
	


}