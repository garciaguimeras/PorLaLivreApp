package dev.blackcat.porlalivre.fragments;

import java.util.List;

import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.Category;

public class CategoryGridFragment extends CategoryBaseFragment
{
	
	@Override
	protected int getLayoutResource() 
	{
		return R.layout.fragment_category_grid;
	}

	@Override
	protected void initView(View view, List<Category> categories) 
	{
		GridView gridView = (GridView)view.findViewById(R.id.categoriesGridView);
		gridView.setAdapter(new CategoryGridAdapter(getActivity(), categories));
		gridView.setOnItemClickListener(this);
	}
	
}
