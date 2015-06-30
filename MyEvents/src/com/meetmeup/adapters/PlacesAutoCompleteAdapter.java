package com.meetmeup.adapters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.bean.Place;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<Place> implements
		Filterable {
	public ArrayList<Place> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<Place> resultList) {

		this.resultList = resultList;

	}

	private static final String LOG_TAG = "ExampleApp";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyDx7FsIHyizKEghTWrCoh10AFheZ38NjUk";
	 
//	private static final String API_KEY = WebService.API_KEY;

	private ArrayList<Place> resultList;
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	
	DisplayImageOptions opt;
	ImageLoader image_load;

	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {

		super(context, textViewResourceId);
		mContext = context;
		inflator = LayoutInflater.from(context);
		
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		

	}

	/*
	 * public PlacesAutoCompleteAdapter(Context context) { // TODO
	 * Auto-generated constructor stub }
	 */
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId,
			Place placeBean) {
		super(context, textViewResourceId);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		view = convertView;
		if (view == null) {
			
			view = inflator.inflate(R.layout.place_item, parent, false);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) view.findViewById(R.id.place_item_tv);
			
//			mHolder.image = (ImageView) view.findViewById(R.id.imgplace);
			
//			mHolder.progressbar_place = (ProgressBar) view.findViewById(R.id.progressbar_place);
			
			view.setTag(mHolder);
			// mHolder.name.setTypeface(CustomTypeFace.GetTypeFace(mContext));
		
		} else {
			
			mHolder = (ViewHolder) view.getTag();

		}
		
		if (mHolder.name != null) {
			
			if(position < resultList.size())
			 mHolder.name.setText(resultList.get(position)
					.getFormattendAddress());
		}
		
//		if (mHolder.image != null) {
//						
//			image_load.displayImage(resultList.get(position).getIconUrl(),mHolder.image,opt, new CustomImageLoader(mHolder.progressbar_place, mContext));
//		}
		
		return view;
		
	}

	@Override
	public int getCount() {
		
		return resultList.size();
		
	}

	@Override
	public Place getItem(int index) {
		
		return resultList.get(index);
		
	}

	@Override
	public Filter getFilter() {
		
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		
		return filter;
		
	}

	private ArrayList<Place> autocomplete(String input) {
		
		ArrayList<Place> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?key=" + API_KEY);
//			sb.append("&components=country:" + AppConstants.COUNTRY);
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			// sb.append("?key=" + API_KEY);
			// sb.append("&components=country:in");
			// sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<Place>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				Place bean = new Place();
				resultList.add(bean);
				bean.setFormattendAddress(predsJsonArray.getJSONObject(i)
						.getString("description"));
				bean.setPlaceId(predsJsonArray.getJSONObject(i).getString(
						"place_id"));
				
//				bean.setIconUrl(predsJsonArray.getJSONObject(i).getString("icon"));
				
				resultList.add(bean);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}

	public class ViewHolder {
		
		public ImageView image;
		public TextView name;
		public ProgressBar progressbar_place;
		
	}

}
