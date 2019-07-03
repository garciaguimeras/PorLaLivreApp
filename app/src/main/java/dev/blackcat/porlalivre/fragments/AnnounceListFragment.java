package dev.blackcat.porlalivre.fragments;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.AnnounceFilter;
import dev.blackcat.porlalivre.data.live.LiveAsyncTask;
import dev.blackcat.porlalivre.utils.DeviceUtils;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.Category;
import dev.blackcat.porlalivre.data.Site;
import dev.blackcat.porlalivre.utils.StringUtils;

public class AnnounceListFragment extends Fragment implements OnItemClickListener 
{
	
	public static final String ANNOUNCELISTFRAGMENT_CATEGORYID = "com.porlalivre.AnnounceListFragment.categoryId";

	List<Announce> filteredAnnounces = new ArrayList<>();
	private Long categoryId;
    List<Site> siteList;

    RelativeLayout emptyAnnouncesLayout;
    TextView emptyAnnouncesTextView;
    ImageView emptyAnnouncesImageView;
    ListView listView;
    Button filterButton;
    Button updateButton;

	MenuItem filterMenuItem;
	LinearLayout filterLayout;
	EditText filterText;
	Spinner stateSpinner;
	EditText fromPriceEdit;
	EditText toPriceEdit;
	Switch cheaperFirstSwitch;
	Switch newerFirstSwitch;

	AnnounceListAdapter adapter;

	public AnnounceListFragment()
	{
		categoryId = new Long(-1);
	}
	
    public static AnnounceListFragment newInstance(Long categoryId) 
    {
    	AnnounceListFragment f = new AnnounceListFragment();

        Bundle args = new Bundle();
        args.putLong(ANNOUNCELISTFRAGMENT_CATEGORYID, categoryId);
        f.setArguments(args);

        return f;
    }

	@Override
	public void setArguments(Bundle args)
	{
		super.setArguments(args);
		this.categoryId = args.getLong(ANNOUNCELISTFRAGMENT_CATEGORYID);
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

		String title = activity.getString(R.string.app_name);
		Category category = (Category) new Category().find(Category.getCategories(getContext()), this.categoryId);
		if (category != null)
			title = category.title;

		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setTitle(title);
		actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_announce_list, container, false);

		if (categoryId == -1 && savedInstanceState.containsKey(ANNOUNCELISTFRAGMENT_CATEGORYID))
			categoryId = savedInstanceState.getLong(ANNOUNCELISTFRAGMENT_CATEGORYID);

        emptyAnnouncesLayout = view.findViewById(R.id.emptyAnnouncesLayout);
        emptyAnnouncesTextView = view.findViewById(R.id.emptyAnnouncesTextView);
        emptyAnnouncesImageView = view.findViewById(R.id.emptyAnnouncesImageView);

        listView = view.findViewById(R.id.announcesListView);
        adapter = new AnnounceListAdapter(getActivity(), filteredAnnounces);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		siteList = Site.getSites(getContext());
		String text = getActivity().getString(R.string.filter_all_states);
		siteList.add(0, new Site(0, text));

        filterButton = view.findViewById(R.id.noAnnouncesFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showFilter(AnnounceFilter.get(getContext(), categoryId));
			}
		});

        updateButton = view.findViewById(R.id.noAnnouncesUpdateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FragmentNavigator.clean();
				FragmentNavigator.add(new UpdateFragment());
			}
		});

        filterAnnounces();

        filterLayout = view.findViewById(R.id.filterLayout);
		filterText = view.findViewById(R.id.filterText);
		stateSpinner = view.findViewById(R.id.stateSpinner);
        fromPriceEdit = view.findViewById(R.id.fromPriceEdit);
        toPriceEdit = view.findViewById(R.id.toPriceEdit);
        cheaperFirstSwitch = view.findViewById(R.id.cheaperFirstSwitch);
        newerFirstSwitch = view.findViewById(R.id.newerFirstSwitch);

        Button applyButton = view.findViewById(R.id.applyButton);
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
				showFilter(new AnnounceFilter(categoryId));
			}
		});

		return view;
	}

	private MenuItem getFilterMenuItem(Menu menu)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			MenuItem item = menu.getItem(i);
			if (item.getItemId() == R.id.action_filter)
			{
				return item;
			}
		}
		return null;
	}

	private void setFilterMenuItemIcon()
	{
		if (filterMenuItem != null)
		{
			AnnounceFilter filter = AnnounceFilter.get(getContext(), categoryId);
			if (!filter.isEmpty())
				filterMenuItem.setIcon(R.drawable.filter_plus_icon);
			else
				filterMenuItem.setIcon(R.drawable.filter_icon);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.announce_list, menu);

		filterMenuItem = getFilterMenuItem(menu);
		setFilterMenuItemIcon();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_filter:
				if (filterLayout.getVisibility() == View.GONE)
					showFilter(AnnounceFilter.get(getContext(), categoryId));
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
		outState.putLong(ANNOUNCELISTFRAGMENT_CATEGORYID, categoryId);
	}	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Announce announce = filteredAnnounces.get(position);
		AnnounceFragment fragment = AnnounceFragment.newInstance(announce.id);
		FragmentNavigator.add(fragment);
	}

	private void filterAnnounces()
    {
		LiveAsyncTask.start(new LiveAsyncTask.LiveService<Announce>() {

			@Override
			public void beforeStart()
			{
                if (filteredAnnounces.size() == 0)
                {
                    emptyAnnouncesImageView.setImageResource(R.drawable.wait_icon);
                    emptyAnnouncesTextView.setText(R.string.text_searching);
                    listView.setVisibility(View.GONE);
                    emptyAnnouncesLayout.setVisibility(View.VISIBLE);
                    filterButton.setVisibility(View.GONE);
                    updateButton.setVisibility(View.GONE);
                }
			}

			@Override
			public void afterFinish(List<Announce> list)
			{
				filteredAnnounces = list;
				if (filteredAnnounces.size() == 0)
				{
                    emptyAnnouncesImageView.setImageResource(R.drawable.warning_icon);
                    emptyAnnouncesTextView.setText(R.string.text_no_announces_found);
					listView.setVisibility(View.GONE);
					emptyAnnouncesLayout.setVisibility(View.VISIBLE);
					filterButton.setVisibility(View.VISIBLE);
					updateButton.setVisibility(View.VISIBLE);
				}
				else
				{
					listView.setVisibility(View.VISIBLE);
					emptyAnnouncesLayout.setVisibility(View.GONE);

					adapter.setFilteredAnnounces(list);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public List<Announce> populateData()
			{
				if (categoryId == -1)
					return new ArrayList<Announce>();
				return Announce.filterByCategoryId(getContext(), categoryId);
			}
		});
    }

	private void showFilter(AnnounceFilter announceFilter)
    {
        filterText.setText(announceFilter.text);

		stateSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, siteList));
		for (int i = 0; i < siteList.size(); i++)
			if (siteList.get(i).id == announceFilter.site)
				stateSpinner.setSelection(i);

		fromPriceEdit.setText(StringUtils.formatCurrency(announceFilter.minPrice));
		toPriceEdit.setText(StringUtils.formatCurrency(announceFilter.maxPrice));
		cheaperFirstSwitch.setChecked(announceFilter.cheaperFirst);
		newerFirstSwitch.setChecked(announceFilter.newerFirst);

        filterLayout.setVisibility(View.VISIBLE);
    }

    private void hideFilter(boolean commitChanges)
    {
        if (commitChanges)
        {
            String text = filterText.getText().toString();
            double min = Double.valueOf(fromPriceEdit.getText().toString());
            double max = Double.valueOf(toPriceEdit.getText().toString());
            int statePos = stateSpinner.getSelectedItemPosition();
            long siteId = siteList.get(statePos).id;
            boolean newerFirst = newerFirstSwitch.isChecked();
            boolean cheaperFirst = cheaperFirstSwitch.isChecked();

            AnnounceFilter.save(getContext(), new AnnounceFilter(categoryId, min, max, siteId, newerFirst, cheaperFirst, text));
            filterAnnounces();
            setFilterMenuItemIcon();
        }
		DeviceUtils.dismissKeyboard(getActivity());
        filterLayout.setVisibility(View.GONE);
    }

}
