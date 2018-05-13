package dev.blackcat.porlalivre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.AnnounceFilter;
import dev.blackcat.porlalivre.data.Site;
import dev.blackcat.porlalivre.dialogs.DialogBuilder;
import dev.blackcat.porlalivre.utils.DeviceUtils;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.utils.StringUtils;

public class SearchFragment extends Fragment
{

    public static final long SEARCH_FILTER_ID = -100;

    List<Site> siteList;

    EditText searchText;
    Spinner searchState;
    EditText searchFromPrice;
    EditText searchToPrice;
    Switch searchCheaperFirst;
    Switch searchNewerFirst;
    Button searchButton;

    public SearchFragment()
    {}

    @Override
    public void onResume()
    {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity)getActivity();

        String title = activity.getString(R.string.app_name);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final AnnounceFilter filter = new AnnounceFilter(SEARCH_FILTER_ID);

        searchText = view.findViewById(R.id.searchText);
        searchFromPrice = view.findViewById(R.id.searchFromPrice);
        searchFromPrice.setText(StringUtils.formatCurrency(filter.minPrice));
        searchToPrice = view.findViewById(R.id.searchToPrice);
        searchToPrice.setText(StringUtils.formatCurrency(filter.maxPrice));
        searchCheaperFirst = view.findViewById(R.id.searchCheaperFirst);
        searchCheaperFirst.setChecked(filter.cheaperFirst);
        searchNewerFirst = view.findViewById(R.id.searchNewerFirst);
        searchNewerFirst.setChecked(filter.newerFirst);

        siteList = Site.getSites(getContext());
        String text = getActivity().getString(R.string.filter_all_states);
        siteList.add(0, new Site(0, text));
        searchState = view.findViewById(R.id.searchState);
        searchState.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, siteList));

        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String text = searchText.getText().toString().trim();
                double min = Double.valueOf(searchFromPrice.getText().toString());
                double max = Double.valueOf(searchToPrice.getText().toString());
                int statePos = searchState.getSelectedItemPosition();
                long siteId = siteList.get(statePos).id;
                boolean newerFirst = searchNewerFirst.isChecked();
                boolean cheaperFirst = searchCheaperFirst.isChecked();

                if (text.length() == 0)
                {
                    DialogBuilder.simpleDialog(getActivity(), R.string.search_error_hint).show();
                    return;
                }

                AnnounceFilter newFilter = new AnnounceFilter(SEARCH_FILTER_ID, min, max, siteId, newerFirst, cheaperFirst, text);
                AnnounceFilter.save(getContext(), newFilter);

                FragmentNavigator.add(new SearchListFragment());
                DeviceUtils.dismissKeyboard(getActivity());
            }
        });

        return view;
    }

}
