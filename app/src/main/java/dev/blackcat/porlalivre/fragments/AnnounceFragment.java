package dev.blackcat.porlalivre.fragments;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.DeviceUtils;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.utils.StringUtils;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.Location;
import dev.blackcat.porlalivre.data.Site;
import dev.blackcat.porlalivre.data.Tag;
import dev.blackcat.porlalivre.dialogs.DialogBuilder;

public class AnnounceFragment extends Fragment
{

	public static final String ANNOUNCEFRAGMENT_ANNOUNCEID = "com.porlalivre.AnnounceFragment.announceId";
	
	private String announceId;
	
	public AnnounceFragment()
	{
		this.announceId = null;
	}
	
    public static AnnounceFragment newInstance(String announceId) 
    {
    	AnnounceFragment f = new AnnounceFragment();

        Bundle args = new Bundle();
        args.putString(ANNOUNCEFRAGMENT_ANNOUNCEID, announceId);
        f.setArguments(args);

        return f;
    }	
	
	@Override
	public void setArguments(Bundle args) 
	{
		super.setArguments(args);
		this.announceId = args.getString(ANNOUNCEFRAGMENT_ANNOUNCEID);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		AppCompatActivity activity = (AppCompatActivity)getActivity();
		ActionBar actionBar = activity.getSupportActionBar();
		actionBar.setHomeAsUpIndicator(R.drawable.back_arrow_icon);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_announce, container, false);

		if (announceId == null && savedInstanceState.containsKey(ANNOUNCEFRAGMENT_ANNOUNCEID))
			announceId = savedInstanceState.getString(ANNOUNCEFRAGMENT_ANNOUNCEID);
		
		Announce announce = Announce.find(getContext(), announceId);
		if (announce == null)
		{
			DialogBuilder.simpleDialog(getActivity(), R.string.text_no_announce_found);
			FragmentNavigator.terminate();
		}
		else
			initAnnounce(view, announce);
		
		return view;
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putString(ANNOUNCEFRAGMENT_ANNOUNCEID, announceId);
	}

	private String[] getPhoneNumbers(String text)
    {
        if (text == null)
            text = "";
        text = text.replace(" ", "");
        text = text.replace("(+53)", "");
        text = text.replace("+53", "");

        String number = "";
        ArrayList<String> numbers = new ArrayList<>();
        for (int i = 0; i < text.length(); i++)
        {
            char chr = text.charAt(i);
            if (chr >= '0' && chr <= '9')
            {
            	// Patch to avoid numbers starting with "0..."
            	if (number.length() == 0 && chr == '0')
            		continue;
            	// --

                number += Character.toString(chr);
            }
            else
            {
                if (number.length() > 0)
                {
                    numbers.add(number);
                    number = "";
                }
            }
        }
        if (number.length() > 0)
            numbers.add(number);

        return numbers.toArray(new String[] {});
    }

	private void initAnnounce(View view, final Announce announce)
	{
		final ImageView imageView = (ImageView)view.findViewById(R.id.starredImageView);
		if (announce.starred)
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.thumb_up_icon));
		else
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.thumb_down_icon));
		imageView.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				announce.starred = !announce.starred;
				Announce.update(getContext(), announce);
				if (announce.starred)
					imageView.setImageDrawable(getResources().getDrawable(R.drawable.thumb_up_icon));
				else
					imageView.setImageDrawable(getResources().getDrawable(R.drawable.thumb_down_icon));
			}
		});
		
		TextView textView;
		textView = (TextView)view.findViewById(R.id.announceTitleTextView);
		textView.setText(announce.title);

		textView = (TextView)view.findViewById(R.id.announcePriceTextView);
		String currency = "$" + StringUtils.formatCurrency(announce.price);
		textView.setText(currency);
		
		textView = view.findViewById(R.id.announceDescriptionTextView);
		textView.setText(announce.description);

		textView = view.findViewById(R.id.announceTagTextView);
		String tagStr = "";
		if (announce.tags != null)
		{
			for (Long tagId: announce.tags)
			{
				if (!tagStr.equals(""))
					tagStr += "|";
				Tag tag = (Tag) new Tag().find(Tag.getTags(getContext()), tagId);
				if (tag != null)
					tagStr += " " + tag.title + " ";
			}
		}
		textView.setText(tagStr);
		if (announce.tags == null || announce.tags.size() == 0)
		    textView.setVisibility(View.GONE);

		textView = (TextView)view.findViewById(R.id.announceUpdateDateTextView);
		String formattedDate = new SimpleDateFormat("MMMM, dd").format(announce.updateDate);
		formattedDate = StringUtils.capitalize(formattedDate);
		textView.setText(formattedDate);

		textView = view.findViewById(R.id.announceSiteTextView);
		String text = "";
		if (announce.locationId != 0)
		{
			Location location = (Location) new Location().find(Location.getLocations(getContext()), announce.locationId);
			text = location.name + ", ";
		}
		Site site = (Site) new Site().find(Site.getSites(getContext()), announce.siteId);
		text += site.name;
		textView.setText(text);
		
		textView = view.findViewById(R.id.announceContactNameTextView);
		textView.setText(announce.contactName);
		if (announce.contactName == null || announce.contactName.isEmpty())
			view.findViewById(R.id.announceNameLayout).setVisibility(View.GONE);

		LinearLayout phoneLayout = view.findViewById(R.id.contactPhoneLayout);
		String[] numbers = getPhoneNumbers(announce.contactPhone);
		for (final String number: numbers)
        {
            TextView phoneView = new TextView(getActivity());
            phoneView.setText(number);
            phoneView.setGravity(Gravity.CENTER_VERTICAL);
            phoneView.setPadding(0, 0, 10, 0);
            if (number.length() != 8)
                phoneView.setTextAppearance(getActivity(), R.style.ContactFont);
            else
            {
                phoneView.setTextAppearance(getActivity(), R.style.LinkFont);
                phoneView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        DeviceUtils.call(getActivity(), number);
                    }
                });
            }
            phoneLayout.addView(phoneView);
        }
		if (numbers.length == 0)
			view.findViewById(R.id.announcePhoneLayout).setVisibility(View.GONE);
		
		final TextView emailTextView = view.findViewById(R.id.announceContactEmailTextView);
		emailTextView.setText(announce.contactEmail);
		emailTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
			    String address = emailTextView.getText().toString();
			    String subject = getString(R.string.mail_subject);
				DeviceUtils.sendMail(getActivity(), address, subject, "");
			}
		});
		if (announce.contactEmail == null || announce.contactEmail.isEmpty())
			view.findViewById(R.id.announceEmailLayout).setVisibility(View.GONE);
	}

}
