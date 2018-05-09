package dev.blackcat.porlalivre.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.fragments.CategoryGridFragment;
import dev.blackcat.porlalivre.fragments.SearchFragment;

public class FragmentNavigator 
{

	public static FragmentActivity fragmentActivity;
	
	public static Fragment getDefaultFragment()
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		Fragment oldFragment = fm.findFragmentById(R.id.fragment_main_layout);
		if (oldFragment != null)
			return oldFragment;
		
		return new SearchFragment();
	}
	
	public static void add(Fragment fragment)
	{
		FragmentNavigator.add(fragment, true);
	}
	
	public static void add(Fragment fragment, boolean addToBackStack)
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		Fragment oldFragment = fm.findFragmentById(R.id.fragment_main_layout);
		
		FragmentTransaction ft = fm.beginTransaction();
		if (oldFragment == null)
			ft.add(R.id.fragment_main_layout, fragment);
		else
			ft.replace(R.id.fragment_main_layout, fragment);
        if (addToBackStack)
            ft.addToBackStack(null);
		ft.commit();
	}
	
	public static void refresh()
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_main_layout);
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.detach(fragment);
		ft.attach(fragment);
		ft.commit();
	}
	
	public static void replace(Fragment fragment)
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragment_main_layout, fragment);
		ft.commit();		
	}	
	
	public static void terminate()
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		fm.popBackStack();
	}

	public static void clean()
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); i++)
		    fm.popBackStack();
	}
	
	public static int getFragmentsInBackStack()
	{
		FragmentManager fm = fragmentActivity.getSupportFragmentManager();
		return fm.getBackStackEntryCount();
	}
	
}
