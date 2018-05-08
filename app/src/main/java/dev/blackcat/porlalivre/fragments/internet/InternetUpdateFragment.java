package dev.blackcat.porlalivre.fragments.internet;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
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
import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.fragments.internet.HttpRetrieverAsyncTask.HttpRetrieverListener;
import dev.blackcat.porlalivre.process.DataCleanerProcess;
import dev.blackcat.porlalivre.process.FileDownloaderProcess;
import dev.blackcat.porlalivre.process.ProcessService;

public class InternetUpdateFragment extends Fragment implements OnItemClickListener, HttpRetrieverListener
{
	
	private static final String BASE_URL = "https://porlalivre.com";
	private static final String UPDATES_URL = BASE_URL + "/desktop/";
	
	ListView listView;
	TextView textView;
	ArrayList<UpdateInfo> updateList;
	
	public InternetUpdateFragment()
	{}

	@Override
	public void onResume()
	{
		super.onResume();

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(R.string.internet_updates);
		actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_internet_update, container, false);
		
		textView = (TextView)view.findViewById(R.id.internetUpdateStatus);
		
		listView = (ListView)view.findViewById(R.id.internetUpdateListView);
		listView.setOnItemClickListener(this);
		
		new HttpRetrieverAsyncTask(this, UPDATES_URL, this).execute();
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		UpdateInfo info = updateList.get(position);

		String url = BASE_URL + info.url;
		String filename = PorLaLivreApp.SDCARD_APP_DIR + "/" + info.name;

		Intent logIntent = new Intent(getContext(), LogActivity.class);
		getActivity().startActivity(logIntent);

		Intent intent = new Intent(getContext(), ProcessService.class);
		intent.putExtra(ProcessService.TYPE, ProcessService.TYPE_DOWNLOADER);
		intent.putExtra(ProcessService.INPUT_FILE, url);
		intent.putExtra(ProcessService.OUTPUT_FILE, filename);
		getActivity().startService(intent);
	}

	@Override
	public void onURLContentFiltered(ArrayList<UpdateInfo> updateList) 
	{
		this.updateList = updateList;
		
		if (updateList.size() == 0)
			textView.setVisibility(View.VISIBLE);
		else
			textView.setVisibility(View.GONE);
		
		listView.setAdapter(new InternetUpdateAdapter(getActivity(), updateList));
	}

}
