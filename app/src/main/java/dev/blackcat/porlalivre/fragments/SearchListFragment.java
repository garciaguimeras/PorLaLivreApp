package dev.blackcat.porlalivre.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.live.LiveAsyncTask;
import dev.blackcat.porlalivre.utils.FragmentNavigator;

public class SearchListFragment extends Fragment implements OnItemClickListener
{

	List<Announce> filteredAnnounces = new ArrayList<>();
	FavoritesListAdapter adapter;

	RelativeLayout emptySearchLayout;
	ImageView emptySearchImageView;
	TextView emptySearchTextView;
	ListView listView;

	public SearchListFragment()
	{}
	
    public static SearchListFragment newInstance()
    {
    	SearchListFragment f = new SearchListFragment();
        return f;
    }

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		AppCompatActivity activity = (AppCompatActivity)getActivity();

		String title = activity.getString(R.string.action_search);
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(title);
		actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
	}

    @Override
	public void setArguments(Bundle args) 
	{
		super.setArguments(args);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_search_list, container, false);

		emptySearchLayout = view.findViewById(R.id.emptySearchLayout);
		emptySearchImageView = view.findViewById(R.id.emptySearchImageView);
		emptySearchTextView = view.findViewById(R.id.emptySearchTextView);

		listView = view.findViewById(R.id.searchListView);
		adapter = new FavoritesListAdapter(getActivity(), filteredAnnounces);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		filterAllAnnounces();
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Announce announce = filteredAnnounces.get(position);
		AnnounceFragment fragment = AnnounceFragment.newInstance(announce.id);
		FragmentNavigator.add(fragment);
	}

	private void filterAllAnnounces()
	{
		LiveAsyncTask.start(new LiveAsyncTask.LiveService<Announce>() {

			@Override
			public void beforeStart()
			{
                if (filteredAnnounces.size() == 0)
                {
                    emptySearchImageView.setImageResource(R.drawable.wait_icon);
                    emptySearchTextView.setText(R.string.text_searching);
                    listView.setVisibility(View.GONE);
                    emptySearchLayout.setVisibility(View.VISIBLE);
                }
			}

			@Override
			public void afterFinish(List<Announce> list)
			{
				filteredAnnounces = list;
				if (filteredAnnounces.size() == 0)
				{
					emptySearchImageView.setImageResource(R.drawable.warning_icon);
					emptySearchTextView.setText(R.string.text_no_announces_found);
					listView.setVisibility(View.GONE);
					emptySearchLayout.setVisibility(View.VISIBLE);
				}
				else
				{
					listView.setVisibility(View.VISIBLE);
					emptySearchLayout.setVisibility(View.GONE);

					adapter.setFilteredAnnounces(filteredAnnounces);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public List<Announce> populateData()
			{
				return Announce.filterAll(getContext());
			}
		});
	}

}
