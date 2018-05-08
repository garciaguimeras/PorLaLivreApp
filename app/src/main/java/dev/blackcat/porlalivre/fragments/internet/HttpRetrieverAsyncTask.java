package dev.blackcat.porlalivre.fragments.internet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.FragmentNavigator;
import dev.blackcat.porlalivre.dialogs.DialogBuilder;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpRetrieverAsyncTask extends AsyncTask<Void, Void, String> 
{
	
	public interface HttpRetrieverListener
	{
		void onURLContentFiltered(ArrayList<UpdateInfo> files);
	}
	
	Fragment fragment;
	String url;
	HttpRetrieverListener listener;
	
	AlertDialog dialog;

	public HttpRetrieverAsyncTask(Fragment fragment, String url, HttpRetrieverListener listener)
	{
		this.fragment = fragment;
		this.url = url;
		this.listener = listener;
	}
	
	@Override
	protected String doInBackground(Void... params) 
	{
		StringBuffer strBuffer = new StringBuffer();
	
        try 
        {
            // InputStream isContent = PorLaLivreApp.context.getAssets().open("website.html");

			OkHttpClient client = new OkHttpClient.Builder().build();
			Request request = new Request.Builder().url(url).build();
			Call call = client.newCall(request);
            Response response = call.execute();
            InputStream isContent = response.body().byteStream();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(isContent));
            String s = "";
            while ((s = buffer.readLine()) != null) 
            	strBuffer.append(s);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return null;
        }
	    
	    return strBuffer.toString();
	}
	
	@Override
	protected void onPreExecute() 
	{
		dialog = DialogBuilder.simpleDialog(fragment.getActivity(), R.string.text_searching_updates);
		dialog.setCancelable(false);
		fragment.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}
	
    @Override
    protected void onPostExecute(String content) 
    {
    	dialog.dismiss();
    	fragment.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    	
   		if (listener != null)
   		{
   			if (content != null)
    			listener.onURLContentFiltered(filterFiles(content));
   			else
   			{
   				DialogBuilder.simpleDialog(fragment.getActivity(), R.string.text_no_internet_connection);
   				FragmentNavigator.terminate();
   			}
   		}
    }
    
	private ArrayList<UpdateInfo> filterFiles(String content)
	{
		ArrayList<UpdateInfo> infoList = new ArrayList<UpdateInfo>();
		
		String patternStr = "";
		patternStr += "<tr class=\"desktop-pud-item \\w+ \">\\s*";
		patternStr += "<td>(\\d+/\\d+/\\d+)</td>\\s*";   // date
		patternStr += "<td>(\\w+)</td>\\s*";             // freq
		patternStr += "<td class=\"hidden-xs hidden-md\">([DS]\\d+.pud)</td>\\s*";     // name
		patternStr += "<td class=\"hidden-xs\">(\\d+)</td>\\s*";             // total
		patternStr += "<td class=\"hidden-xs\">(\\d+ \\w+)</td>\\s*";     // size
		patternStr += "<td><a class=\"btn btn-default btn-xs\" href=\"(/desktop/pud_download/[DS]\\d+.pud/)\" rel=\"nofollow\">Descargar</a></td>\\s*";     // url
		patternStr += "</tr>";
		
		Pattern pattern = Pattern.compile(patternStr);		
		Matcher matcher = pattern.matcher(content);
		while (matcher.find())
		{
			UpdateInfo info = new UpdateInfo(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6));
		    infoList.add(info);
		}
		return infoList;
	}    

}

