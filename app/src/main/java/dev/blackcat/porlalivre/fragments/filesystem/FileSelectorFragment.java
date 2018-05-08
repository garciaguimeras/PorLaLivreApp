package dev.blackcat.porlalivre.fragments.filesystem;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import dev.blackcat.porlalivre.LogActivity;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.process.DeserializerProcess;
import dev.blackcat.porlalivre.process.FileDownloaderProcess;
import dev.blackcat.porlalivre.process.ProcessService;
import dev.blackcat.porlalivre.utils.FragmentNavigator;

public class FileSelectorFragment extends Fragment implements OnItemClickListener
{
	
	public static final String FILESELECTORFRAGMENT_PATH = "com.porlalivre.FileSelectorFragment.path";
	private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath();
	
	private String path;
	private File[] files;
	
	public FileSelectorFragment()
	{
		this.path = BASE_PATH;
	}
	
    public static FileSelectorFragment newInstance(String path) 
    {
    	FileSelectorFragment f = new FileSelectorFragment();

        Bundle args = new Bundle();
        args.putString(FILESELECTORFRAGMENT_PATH, path);
        f.setArguments(args);

        return f;
    }	
	
	@Override
	public void setArguments(Bundle args) 
	{
		super.setArguments(args);
		this.path = args.getString(FILESELECTORFRAGMENT_PATH);
	}	
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putString(FILESELECTORFRAGMENT_PATH, path);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(R.string.file_explorer);
		actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_file_selector, container, false);
		
		if (savedInstanceState != null && savedInstanceState.containsKey(FILESELECTORFRAGMENT_PATH))
			path = savedInstanceState.getString(FILESELECTORFRAGMENT_PATH);	
		
		TextView textView = (TextView)view.findViewById(R.id.fileSelectorFolderNameTextView);
		textView.setText(path);		
		
		File f = new File(path);
		if (!f.isDirectory())
		{
			Intent logIntent = new Intent(getContext(), LogActivity.class);
			getActivity().startActivity(logIntent);

			Intent intent = new Intent(getContext(), ProcessService.class);
			intent.putExtra(ProcessService.TYPE, ProcessService.TYPE_DESERIALIZER);
			intent.putExtra(ProcessService.INPUT_FILE, path);
			getActivity().startService(intent);

			FragmentNavigator.terminate();
			return view;
		}
		
		files = getFiles(f);
		
		ListView listView = (ListView)view.findViewById(R.id.fileSelectorListView);
		listView.setAdapter(new FileSelectorAdapter(getActivity(), files));
		listView.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		File f = files[position];
		FileSelectorFragment fragment = FileSelectorFragment.newInstance(path + File.separator + f.getName());
		FragmentNavigator.add(fragment);
	}
	
	private File[] getFiles(File f)
	{
		FileFilter pudFilter = new FileFilter() 
		{
			@Override
			public boolean accept(File f) 
			{
				if (f.isDirectory())
					return true;
				String name = f.getName().toLowerCase();
				if (name.endsWith(".pud"))
					return true;
				return false;
			}
		};
	    File[] fileArray = f.listFiles(pudFilter);
	    List<File> fileList = Arrays.asList(fileArray);
	    Collections.sort(fileList);
	    return fileList.toArray(new File[] {});
	}
	
}
