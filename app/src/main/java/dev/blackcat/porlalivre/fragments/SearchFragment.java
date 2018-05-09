package dev.blackcat.porlalivre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.AnnounceFilter;
import dev.blackcat.porlalivre.data.Site;
import dev.blackcat.porlalivre.utils.DeviceUtils;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.utils.StringUtils;

public class SearchFragment extends Fragment
{

    public static final long SEARCH_FILTER_ID = -100;

    EditText searchText;
    Spinner searchState;
    EditText searchFromPrice;
    EditText searchToPrice;
    Switch searchCheaperFirst;
    Switch searchNewerFirst;
    Button searchButton;

    public SearchFragment()
    {}

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

        ArrayList<Site> siteList = new ArrayList<Site>();
        String text = getActivity().getString(R.string.filter_all_states);
        siteList.addAll(Site.getSites(getContext()));
        siteList.add(0, new Site(0, text));

        searchState = view.findViewById(R.id.searchState);
        searchState.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, siteList));

        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AnnounceFilter.save(getContext(), filter);
                FragmentNavigator.add(new SearchListFragment());
                DeviceUtils.dismissKeyboard(getActivity());
            }
        });

        return view;
    }

}
