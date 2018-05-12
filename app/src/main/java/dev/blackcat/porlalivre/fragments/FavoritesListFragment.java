package dev.blackcat.porlalivre.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.FavoritesFilter;
import dev.blackcat.porlalivre.data.live.LiveAsyncTask;
import dev.blackcat.porlalivre.utils.DeviceUtils;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.data.Announce;

public class FavoritesListFragment extends Fragment implements OnItemClickListener
{

	List<Announce> filteredAnnounces = new ArrayList<>();
	private Long categoryId;

	RelativeLayout emptyFavoritesLayout;
	ImageView emptyFavoritesImageView;
	TextView emptyFavoritesTextView;

	LinearLayout filterLayout;
	EditText filterText;

	ListView listView;
	FavoritesListAdapter adapter;

	public FavoritesListFragment()
	{
		this.categoryId = new Long(-1);
	}
	
    public static FavoritesListFragment newInstance()
    {
    	FavoritesListFragment f = new FavoritesListFragment();
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

		String title = activity.getString(R.string.action_favorite_announces);
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(title);
		actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
	}

    @Override
	public void setArguments(Bundle args) 
	{
		super.setArguments(args);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

		emptyFavoritesLayout = view.findViewById(R.id.emptyFavoritesLayout);
		emptyFavoritesImageView = view.findViewById(R.id.emptyFavoritesImageView);
		emptyFavoritesTextView = view.findViewById(R.id.emptyFavoritesTextView);

		listView = view.findViewById(R.id.favoritesListView);
		adapter = new FavoritesListAdapter(getActivity(), filteredAnnounces);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		filterLayout = view.findViewById(R.id.filterLayout);
		filterText = view.findViewById(R.id.filterText);

		Button applyButton = (Button)view.findViewById(R.id.applyButton);
		applyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				hideFilter(true);
			}
		});

		Button cleanButton = view.findViewById(R.id.cleanButton);
		cleanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				showFilter(new FavoritesFilter());
			}
		});

		filterFavorites();

		FavoritesFilter filter = FavoritesFilter.get(getContext());
		if (!filter.isEmpty())
			showFilter(filter);
		
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.announce_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_filter:
				if (filterLayout.getVisibility() == View.GONE)
					showFilter(FavoritesFilter.get(getContext()));
				else
					hideFilter(false);
				return true;
		}

		return super.onOptionsItemSelected(item);
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

	private void filterFavorites()
	{
		LiveAsyncTask.start(new LiveAsyncTask.LiveService<Announce>() {

			@Override
			public void beforeStart()
			{
				if (filteredAnnounces.size() == 0)
				{
					emptyFavoritesImageView.setImageResource(R.drawable.wait_icon);
					emptyFavoritesTextView.setText(R.string.text_searching);
					listView.setVisibility(View.GONE);
					emptyFavoritesLayout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void afterFinish(List<Announce> list)
			{
				filteredAnnounces = list;
				if (filteredAnnounces.size() == 0)
				{
					emptyFavoritesImageView.setImageResource(R.drawable.warning_icon);
					emptyFavoritesTextView.setText(R.string.text_no_favorites_found);
					listView.setVisibility(View.GONE);
					emptyFavoritesLayout.setVisibility(View.VISIBLE);
				}
				else
				{
					listView.setVisibility(View.VISIBLE);
					emptyFavoritesLayout.setVisibility(View.GONE);

					adapter.setFilteredAnnounces(list);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public List<Announce> populateData()
			{
				return Announce.filterByFavorites(getContext());
			}
		});

	}

	private void showFilter(FavoritesFilter favoritesFilter)
	{
		filterText.setText(favoritesFilter.text);
		filterLayout.setVisibility(View.VISIBLE);
	}

	private void hideFilter(boolean commitChanges)
	{
		if (commitChanges)
		{
			String text = filterText.getText().toString();

			FavoritesFilter.save(getContext(), new FavoritesFilter(text));
			filterFavorites();
		}
		DeviceUtils.dismissKeyboard(getActivity());
		filterLayout.setVisibility(View.GONE);
	}

}
