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
import android.widget.Spinner;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.dialogs.DataCleanerDialog;
import dev.blackcat.porlalivre.fragments.filesystem.FileSelectorFragment;
import dev.blackcat.porlalivre.fragments.internet.InternetUpdateFragment;

public class UpdateFragment extends Fragment
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        String[] options = view.getResources().getStringArray(R.array.cleanup_options);
        final Spinner spinner = (Spinner)view.findViewById(R.id.cleanUpSpinner);
        spinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, options));


        Button button = view.findViewById(R.id.buttonSDCardUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Fragment fragment = new FileSelectorFragment();
                FragmentNavigator.add(fragment);
            }
        });

        button = view.findViewById(R.id.buttonInternetUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Fragment fragment = new InternetUpdateFragment();
                FragmentNavigator.add(fragment);
            }
        });

        button = view.findViewById(R.id.buttonCleanUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int pos = spinner.getSelectedItemPosition();
                int days = 0;
                switch (pos)
                {
                    case 0: days = 7; break;
                    case 1: days = 15; break;
                    case 2: days = 30; break;
                    case 3: days = 60; break;
                }
                DataCleanerDialog dialog = new DataCleanerDialog(getActivity(), days);
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.action_update);
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
    }
}
