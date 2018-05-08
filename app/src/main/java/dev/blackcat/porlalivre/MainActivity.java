package dev.blackcat.porlalivre;

import java.io.File;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import dev.blackcat.porlalivre.data.SysInfo;
import dev.blackcat.porlalivre.process.ProcessService;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.data.db.DatabaseHelper;
import dev.blackcat.porlalivre.data.readers.AssetsCategoryReader;
import dev.blackcat.porlalivre.data.readers.AssetsLocationReader;
import dev.blackcat.porlalivre.data.readers.AssetsSiteReader;
import dev.blackcat.porlalivre.data.readers.AssetsTagReader;
import dev.blackcat.porlalivre.data.readers.AssetsWorkshopReader;
import dev.blackcat.porlalivre.dialogs.DialogBuilder;
import dev.blackcat.porlalivre.fragments.AboutFragment;
import dev.blackcat.porlalivre.fragments.CategoryGridFragment;
import dev.blackcat.porlalivre.fragments.FavoritesListFragment;
import dev.blackcat.porlalivre.fragments.UpdateFragment;

public class MainActivity extends AppCompatActivity
{

	private final int REQUEST_WRITE_PERMISSION_CODE = 0;
    private final String CURRENT_FRAGMENT = "dev.blackcat.porlalivre.MainActivity.currentFragment";

    DrawerLayout drawerLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);

		int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission == PackageManager.PERMISSION_GRANTED)
        {
            initDatabase();
        }
        else
		{
			ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_PERMISSION_CODE);
		}

		FragmentNavigator.fragmentActivity = this;
        Fragment f = FragmentNavigator.getDefaultFragment();
        if (savedInstanceState != null && savedInstanceState.getString(CURRENT_FRAGMENT) != null)
        {
            try
            {
                String className = savedInstanceState.getString(CURRENT_FRAGMENT);
                Object instance = Class.forName(className).newInstance();
                f = (Fragment) instance;
            }
            catch (Exception e)
            {}
        }
		FragmentNavigator.add(f);

        checkFirstTime(savedInstanceState);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
		{
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem)
			{
				drawerLayout.closeDrawers();
				return onOptionsItemSelected(menuItem);
			}
		});
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        String name = FragmentNavigator.getDefaultFragment().getClass().getName();
        outState.putString(CURRENT_FRAGMENT, name);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    FragmentNavigator.terminate();
                    if (FragmentNavigator.getFragmentsInBackStack() == 1)
                        finish();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment;

		switch (item.getItemId())
		{
            case android.R.id.home:
            	Fragment f = FragmentNavigator.getDefaultFragment();
                if (FragmentNavigator.getFragmentsInBackStack() == 1)
                {
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;
                }
                FragmentNavigator.terminate();
                return true;

			case R.id.action_about:
				fragment = new AboutFragment();
				FragmentNavigator.clean();
                FragmentNavigator.add(fragment);
				return true;

            /*
			case R.id.action_workshops:
				fragment = new WorkshopFragment();
				FragmentNavigator.add(fragment);
				return true;
			*/

            case R.id.action_all_announces:
				fragment = new CategoryGridFragment();
				FragmentNavigator.clean();
				FragmentNavigator.add(fragment);
                return true;

            case R.id.action_favorite_announces:
                fragment = new FavoritesListFragment();
                FragmentNavigator.clean();
                FragmentNavigator.add(fragment);
                return true;

            case R.id.action_update:
                fragment = new UpdateFragment();
                FragmentNavigator.clean();
                FragmentNavigator.add(fragment);
                return true;

            /*
			case R.id.action_cleanup:
				new DataCleanerDialog(this).show();
				return true;
				
			case R.id.action_update_sdcard:
				fragment = new FileSelectorFragment();
				FragmentNavigator.add(fragment);
				return true;
				
			case R.id.action_update_internet:
				fragment = new InternetUpdateFragment();
				FragmentNavigator.add(fragment);
				return true;
			*/
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if (requestCode == REQUEST_WRITE_PERMISSION_CODE)
		{
			int permission = grantResults[0];
			if (permission == PackageManager.PERMISSION_GRANTED)
            {
                initDatabase();
            }
            else
            {
                DialogBuilder.oneButtonDialog(this,
                        R.string.text_permission_alert_title,
                        R.string.text_permission_alert,
                        R.string.text_permission_alert_button,
                        new DialogBuilder.OnDialogButtonListener() {
                            @Override
                            public void onPositiveButtonSelected() {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                        REQUEST_WRITE_PERMISSION_CODE);
                            }
                        });
            }
		}
	}

	private void initDatabase()
    {
        File file = new File(PorLaLivreApp.SDCARD_APP_DIR);
        if (!file.exists())
            file.mkdirs();
    }

    private void checkFirstTime(Bundle savedInstanceState)
    {
        if (savedInstanceState == null)
        {
            if (SysInfo.get(this).lastDataUpdate == null)
            {
                AlertDialog dialog = DialogBuilder.oneButtonDialog(this,
                        R.string.dialog_first_time_title,
                        R.string.dialog_first_time_text,
                        R.string.button_update,
                        new DialogBuilder.OnDialogButtonListener() {
                    @Override
                    public void onPositiveButtonSelected()
                    {
                       FragmentNavigator.clean();
                       FragmentNavigator.add(new UpdateFragment());
                    }
                });
                dialog.show();
            }
        }
    }

}
