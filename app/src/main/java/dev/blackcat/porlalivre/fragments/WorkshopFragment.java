package dev.blackcat.porlalivre.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import dev.blackcat.porlalivre.R;

public class WorkshopFragment extends Fragment
{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_workshop, container, false);
		
		ListView listView = (ListView)view.findViewById(R.id.workshopsListView);
		listView.setAdapter(new WorkshopListAdapter(getActivity()));
		
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(R.string.action_workshops);
		actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
	}

}
