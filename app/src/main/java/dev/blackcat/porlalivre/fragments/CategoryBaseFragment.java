package dev.blackcat.porlalivre.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;

import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.data.Category;

public abstract class CategoryBaseFragment extends Fragment implements OnItemClickListener
{

	public static final String CATEGORYFRAGMENT_PARENTID = "com.porlalivre.CategoryBaseFragment.parentId";
	
	protected long parentId;
	protected List<Category> categories;
	protected List<Category> filteredCategories;
	
	protected abstract int getLayoutResource();
	protected abstract void initView(View view, List<Category> categories);
	
	public CategoryBaseFragment()
	{
		this.parentId = 0;
	}
	
    public static <T extends CategoryBaseFragment> T newInstance(Class<T> clazz, Long parentId) 
    {
		try 
		{
			T f = clazz.newInstance();

			Bundle args = new Bundle();
	        args.putLong(CATEGORYFRAGMENT_PARENTID, parentId);
	        f.setArguments(args);

	        return f;
		} 
		catch (Exception e) 
		{}
		return null;
    }	
    
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putLong(CATEGORYFRAGMENT_PARENTID, parentId);
	}    
	
	@Override
	public void setArguments(Bundle args) 
	{
		super.setArguments(args);
		this.parentId = args.getLong(CATEGORYFRAGMENT_PARENTID);
	}		
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		categories = Category.getCategories(getContext());
	}

    @Override
    public void onResume()
    {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity)getActivity();

        String title = activity.getString(R.string.app_name);
        Category category = (Category) new Category().find(categories, this.parentId);
        if (category != null)
            title = category.title;

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(title);
        if (category == null)
        	actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
        else
        	actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		
		View view = inflater.inflate(getLayoutResource(), container, false);

		if (parentId == 0 && savedInstanceState != null && savedInstanceState.containsKey(CATEGORYFRAGMENT_PARENTID))
			parentId = savedInstanceState.getLong(CATEGORYFRAGMENT_PARENTID);
		
		filteredCategories = new Category().filterByParent(categories, parentId);
		initView(view, filteredCategories);
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Category category = filteredCategories.get(position);
		List<Category> childCategories = new Category().filterByParent(categories, category.id);
		Fragment fragment = null;
		if (childCategories.size() > 0)
		{
			fragment = CategoryBaseFragment.newInstance(getClass(), category.id);
		}
		else
		{
			fragment = AnnounceListFragment.newInstance(category.id);
		}
		FragmentNavigator.add(fragment);
	}

	public long getParentId()
	{
		return parentId;
	}

}
