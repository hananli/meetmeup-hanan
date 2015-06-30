package com.meetmeup.fragment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.GoogleLocationClient;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.EventAdapter;
import com.meetmeup.adapters.EventAdapter.EventItemClickListener;
import com.meetmeup.asynch.GetAddressByLatLongTask;
import com.meetmeup.asynch.GetNearByEventsAsync;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.Place;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.GPSTracker;
import com.meetmeup.helper.Utill;
//import com.meetmeup.fragment.HomeFragment.EventItemClickListener;

//This class is used for showing list of the nearby people according to radius.
public class NearByEventsFragement extends Fragment /*implements SeekBarChangeListener*/ {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	ListView nearByListView;
	public static ArrayList<EventsBean> mainList;
	ArrayList<EventsBean> filterList;
	ArrayList<EventsBean> finalList;
	
	public static ArrayList<EventsBean> filteredCategoryList;
	
	
	ImageView imgMyLocation;
	
//	GoogleLocationClient googleLocationClient;
	
	
	
//	Button All, sun, mon, tue, wed, thurs, fri, sat;
	public static DashBoard dashBoard;
	
	 TextView first,second;
	 
	 ImageView homeIV, settingIV, createEventIV, myEventsIV, nearByEventsIV;
	 
	 TextView txt_current_location;
	 
//	 GoogleMap googleMap;
//	 MarkerOptions markerOptions;
	 
	 
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			
			mfFragment = new NearByEventsFragement();
		
		}
		
		return mfFragment;
		
	}
	
	
	OnClickListener bottomClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.homeIV:
				
				dashBoard.swithFragment(DashBoard.FRAGMENT_HOME);
				break;
				
			case R.id.settingIV:
				
				dashBoard.swithFragment(DashBoard.FRAGMENT_SETTING);
				break;
				
			case R.id.createEventIV:
				
				dashBoard.swithFragment(DashBoard.FRAGMENT_CREATE_EVENT);
				break;
				
			case R.id.myEventsIV:
//				button_yourevent.performClick();
				
				myEventsIV.setImageResource(R.drawable.my_event_active_btn);
				nearByEventsIV.setImageResource(R.drawable.nearby_deactive_btn);
				
				try{
					
				if (filteredCategoryList != null) {

					if (!filteredCategoryList.isEmpty()) {

						filteredCategoryList.clear();
						
					}

					UserBean user = Utill.getUserPreferance(mContext);

					if (mainList != null) {
						for (int i = 0; i < mainList.size(); i++) {
							EventsBean evtbean = mainList.get(i);
							if (evtbean.getEvent_owner_id().equals(user.getUser_id()) || evtbean.getEventAcceptStatus().equalsIgnoreCase("1")) {
								filteredCategoryList.add(evtbean);
							}
						}

					    setEventAdapter(filteredCategoryList);
						
					}
				}
				}catch (Exception e) {
					
					e.printStackTrace();
					
				}
								
				break;
				
			case R.id.nearByEventsIV:
				
				dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT);
				
				myEventsIV.setImageResource(R.drawable.my_event_deactive_btn);
				nearByEventsIV.setImageResource(R.drawable.nearby_active_btn);
				
				try{
			
				if (filteredCategoryList != null) {
					
				
				if(filteredCategoryList != null && !filteredCategoryList.isEmpty()){
					
					filteredCategoryList.clear();
					
				}
				    
				if(mainList != null){
				
					for(int i=0;i<mainList.size();i++){
						
					filteredCategoryList.add(mainList.get(i));
						
				   }
					
				   setEventAdapter(filteredCategoryList);				  
				 
				}
				
			   }
			   
			  }catch (Exception e) {
				
				  e.printStackTrace();
			 
			  }
				
			   break;

			default:
				
				break;
				
			}

		}
		
	};
		

	@Override
	public void onStart() {
		
		if (DashBoard.actionBar != null) {
			
//			DashBoard.resetActionBarTitle("Near By Events(" + Utill.getRadius(mContext) + " Km.)");
			DashBoard.resetActionBarTitle("Events Nearby",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.rightButton.setImageResource(R.drawable.map);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		
		}
		
		if(AppConstants.getCurrentLatitude() > 0.0 || AppConstants.getCurrentLatitude() > 0){
			
			
		}else{
			
//			new GPSTracker(mContext);
			
//			googleLocationClient.onStart();
			
			handler.postDelayed(runnable,2000);
			
		}
		
		super.onStart();
		
	}
	
	@Override
	public void onStop() {
	
//		googleLocationClient.onStop();
		super.onStop();
		
				
	}
	
	 private GoogleMap map;
	 private MapView mapView;
	 private boolean mapsSupported = true;
	
	 
	    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		dashBoard = (DashBoard) mActivity;
		View view = inflater.inflate(R.layout.near_by_list_view, container, false);
		
		
//		googleLocationClient = new GoogleLocationClient(mContext);
		
		initializeView(view);
		
		mainList = null;
		filterList = null;
		finalList = null;
			
		setOnClickeListeners();
		
		initMapview(view, savedInstanceState);	
		
		getNearByList();		
		
		
		return view;
		
	}

	
	public void initMapview(View view,Bundle savedInstanceState){
		
		mapView = (MapView) view.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		imgMyLocation = (ImageView)view.findViewById(R.id.imgMyLocation);
	   
		imgMyLocation.setOnClickListener(new OnClickListener() {
						
			@Override
			public void onClick(View v) {
				
				if(map != null){
					
				    LatLng latLng = new LatLng(AppConstants.getCurrentLatitude(),
				    		AppConstants.getCurrentLongitude());
				    
				    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
				    map.animateCamera(cameraUpdate);
				    
				}
				
			}
		});
		
		
		
		if(isGoogleMapsInstalled()){
			if(Utill.isNetworkAvailable(mContext)){	
				
//				 showMap();
//				 googleLocationClient.onStart();
				 handler.postDelayed(runnable,0);
			
			}
			else{
				
				Toast.makeText(mContext,"network error",Toast.LENGTH_LONG).show();
//				ShowUserMessage.showToastMessage(mContext, getResources().getString(R.string.internet_connection));
		   }
			
		}else{
			
			AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
			builder.setMessage("Install Google Maps");
			builder.setCancelable(false);
			builder.setButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
					startActivity(intent);
				}
			});
			builder.show();
		}
				
	}
		
	
	//for getting list of the near by people
	void getNearByList() {
		
		if (Utill.isNetworkAvailable(mContext)) {
			
			MultipartEntity multi = new MultipartEntity();
			
			try {
				
				multi.addPart("radius", new StringBody(Utill.getRadius(mContext)));
				multi.addPart("lat", new StringBody(AppConstants.getLattitude()));
				multi.addPart("long", new StringBody(AppConstants.getLogitude()));
				multi.addPart("user_id", new StringBody(Utill.getUserPreferance(mContext).getUser_id()));
				showProgress();
				new GetNearByEventsAsync(mContext, new NearByListener(), multi).execute();
			
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
							
			}
		
		} else {
			
			Utill.showNetworkError(mContext);
		
		}
		
	}
	
	public void setEventAdapter(ArrayList<EventsBean> list){
		
		nearByListView.setAdapter(null);
		EventAdapter adapter = new EventAdapter(mContext, list);			
		
        adapter.setEventItemClickListener(new EventItemClickListener() {
			
			@Override
			public void onClickProfilePic(String user_id) {
			
				dashBoard.showOwnerProfile(user_id);
				
				
			}
		});		
				
		nearByListView.setAdapter(adapter);
		nearByListView.setVisibility(View.VISIBLE);
				
	 }
		
		
	//This is the listener class for notify web response.
	public class NearByListener {
		
		public void onSuccess(ArrayList<EventsBean> list, String msg) {
		
			mainList = list;
			filterList = list;
			finalList = list;		
			  
//			getLocation();			

			for(int i= 0;i<NearByEventsFragement.mainList.size();i++){
				
			  try{
				
//				BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.current_position_tennis_ball);
				
//			    Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			    
			    
			      Bitmap bitmap = BitmapFactory
			    		.decodeResource(mContext.getResources(),R.drawable.location_count_icon);
//			    		.copy(Bitmap.Config.ARGB_8888, true);
			    
			      int width = bitmap.getWidth();
			      int height = bitmap.getHeight();
			    
			      bitmap = convertToMutable(mContext, bitmap);
					    
			      Canvas canvas = new Canvas(bitmap);

			      Paint paint = new Paint();
			      paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD,Typeface.BOLD));
//			      paint.setColor(Color.parseColor("#3D9992")); 
			      paint.setStyle(Paint.Style.FILL);       
//			      canvas.drawPaint(paint);

			    
			      paint.setColor(Color.WHITE);
			      paint.setStrokeWidth(20);
			      paint.setAntiAlias(true);
			
			      paint.setTextSize(60);
			    
			      paint.setTextAlign(Paint.Align.CENTER);			  
			      canvas.drawText(""+(i+1), (width / 2.f) , (height / 2.f), paint);
							    
//			      bitmap =  Bitmap.createBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.location_count_icon));
			    
			      BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
				
				  EventsBean bean = NearByEventsFragement.mainList.get(i);
				  double l = Double.parseDouble(bean.getEvent_lat());
				  double lon = Double.parseDouble(bean.getEvent_eventLong());
				  String t = bean.getEvent_title();
								 
				  Marker m = map.addMarker(new MarkerOptions().position(new LatLng(l, lon)).title(t).icon(icon));
				
				   //map.setMyLocationEnabled(true);
				  map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l, lon), 15));
				  map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
				  map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(l , lon)));
				 				 
		         
			    }catch (Exception e) {
					
			    	e.printStackTrace();
			    	
			   }	
			  
			}
					
			showcurrentLocation();
			
					
            filteredCategoryList = new ArrayList<EventsBean>();
		    
			for(int i=0;i<mainList.size();i++){
				
				filteredCategoryList.add(mainList.get(i));
				
			}
			
			setEventAdapter(filteredCategoryList);
			
			hideProgress();
//			EventAdapter adapter = new EventAdapter(mContext, list);
//			nearByListView.setAdapter(adapter);
//			Utill.showToast(mContext, msg);
//			All.performClick();
		}

		public void onError(String msg) {
			
			hideProgress();
//			Utill.showToast(mContext, msg);
			showcurrentLocation();
			
		}
	}
	
	
	public void showcurrentLocation(){
		
		try {
		
			double Lat = AppConstants.getCurrentLatitude();
			double Lon = AppConstants.getCurrentLongitude();

			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromBitmap(BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.location));

			map.addMarker(new MarkerOptions().position(
					new LatLng(AppConstants.getCurrentLatitude(), AppConstants
							.getCurrentLongitude())).icon(icon));

			// map.setMyLocationEnabled(true);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat,
					Lon), 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Lat, Lon)));

		} catch (Exception e) {

			e.printStackTrace();

		}
		
	}
	
	
	Handler handler = new Handler();
	boolean isCurrLocationVisible = false;
	

	final Runnable runnable = new Runnable() {
	    public void run() {
	        
	    	if(!isCurrLocationVisible){	    
	    		
	    		 showMap();	
	    		 showcurrentLocation();
//	    		 handler.postDelayed(runnable,1000);	  
	    		 
	    	}	    	
	    	
	    }
	};
    
	
	
	/*public Location getLocation() {
      
		Location location = null;
//		map.setMyLocationEnabled(true);
				
		try {
			
			LocationManager locationManager = (LocationManager) mContext
                    .getSystemService(mContext.LOCATION_SERVICE);
 
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            	
           
            } else {
//                this.canGetLocation = true;
                // First get location from Network Provider
               
            	if (isNetworkEnabled) {
                  
                	locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000,
                            1, new LocationListener() {
								
								@Override
								public void onStatusChanged(String provider, int status, Bundle extras) {
									
									
								}
								
								@Override
								public void onProviderEnabled(String provider) {
									
									
								}
								
								@Override
								public void onProviderDisabled(String provider) {
									
									
								}
								
								@Override
								public void onLocationChanged(Location location) {
								
									
									isCurrLocationVisible = true;
									
									
								}
						});
                	
                	
                    Log.d("Network", "Network");
                   
                    if (locationManager != null) {
                                          	
                    	location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                     
                       if (location != null) {
                        	
                        	double latitude =  location.getLatitude();
                        	double longitude = location.getLongitude();
                        	
                        	BitmapDescriptor icon = BitmapDescriptorFactory
                        			.fromBitmap(BitmapFactory.
                        					decodeResource(mContext.getResources(),R.drawable.location));
                        			
                        	map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).icon(icon));
                        	
                        	setAddress(latitude, longitude);
                        	
                       }
                       
                   }
                    
                }
                
            	
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                  
                	if (location == null) {
                		
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1000,
                                1, new LocationListener() {
									
									@Override
									public void onStatusChanged(String provider, int status, Bundle extras) {
										
										
										
									}
									
									@Override
									public void onProviderEnabled(String provider) {
										
										
										
									}
									
									@Override
									public void onProviderDisabled(String provider) {
										
										
										
									}
									
									@Override
									public void onLocationChanged(Location location) {

										
										isCurrLocationVisible = true;										
										
										
									}
									
								});
                        
                        
                        Log.d("GPS Enabled", "GPS Enabled");
                    
                       if (locationManager != null) {
                        	
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                           
                            if (location != null) {
                            	
                                  double latitude = location.getLatitude();
                                  double longitude = location.getLongitude();
                               
                                  BitmapDescriptor icon = BitmapDescriptorFactory
                           			.fromBitmap(BitmapFactory.
                           					decodeResource(mContext.getResources(),R.drawable.location));
                           			
                           	      map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).icon(icon));
                           	  
                           	      setAddress(latitude, longitude);
                           	                                	  
                            }
                            
                        }
                    }
                }
            }
 
        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
 
        return location;
        
    }*/
  
   /* private void setUpMap() { 
    
       try{
    	
    	 map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet")); 
 
        // Enable MyLocation Layer of Google Map 
    	 map.setMyLocationEnabled(true); 
 
        // Get LocationManager object from System Service LOCATION_SERVICE 
         LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE); 
 
        // Create a criteria object to retrieve provider 
         Criteria criteria = new Criteria(); 
 
        // Get the name of the best provider 
         String provider = locationManager.getBestProvider(criteria, true); 
 
        // Get Current Location 
         Location myLocation = locationManager.getLastKnownLocation(provider); 
 
        // set map type 
         map.setMapType(GoogleMap.MAP_TYPE_NORMAL); 
 
        // Get latitude of the current location 
         double latitude = myLocation.getLatitude(); 
 
        // Get longitude of the current location 
         double longitude = myLocation.getLongitude(); 
 
        // Create a LatLng object for the current location 
         LatLng latLng = new LatLng(latitude, longitude); 
 
        // Show the current location in Google Map 
         map.moveCamera(CameraUpdateFactory.newLatLng(latLng)); 
 
        // Zoom in the Google Map 
         map.animateCamera(CameraUpdateFactory.zoomTo(14)); 
//        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).; 
    
         map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)));
        
       }catch(Exception e){
    	   
    	   e.printStackTrace();
    	   
       }
       
    } */

	
	
	public void setAddress(double latitude,double longitude){
		
//		try {
//			
//			Geocoder geocoder;
//			List<Address> addresses;
//			geocoder = new Geocoder(mContext);
//			
//			addresses = geocoder.getFromLocation(latitude, longitude, 1);
//			
//			String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//			String city = addresses.get(0).getLocality();
//			String state = addresses.get(0).getAdminArea();
//			String country = addresses.get(0).getCountryName();
//			String postalCode = addresses.get(0).getPostalCode();
//			String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//			
//			txt_current_location.setText(address+" "+city+","+state+","+country);
//			
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//		} // Here 1 represent max location result to returned, by documents it recommended 1 to 5

		
//		getCurrentAddress(latitude, longitude);
		
		
	}
	
	void getCurrentAddress(String lat,String lon) {
		
		if (Utill.isNetworkAvailable(mContext)) { 

			String url = lat + "," + lon;
			
//		showProgress();
		    
		   new GetAddressByLatLongTask(mContext, url, new GetAddressPublishListener()).execute();
		
		} else {
			
		   Utill.showNetworkError(mContext);
		
		}
		
	}
	
	public class GetAddressPublishListener{

		public void onError(String string) {
		
			
		}

		public void onSuccess(Place place) {
			
			txt_current_location.setText(place.getFormattendAddress());
			
		}
		
		
		
	}
	
	
	
	public static Bitmap convertToMutable(final Context context, final Bitmap imgIn) {
		
	    final int width = imgIn.getWidth(), height = imgIn.getHeight();
	    final Config type = imgIn.getConfig();
	    File outputFile = null;
	    final File outputDir = context.getCacheDir();
	    try {
	        outputFile = File.createTempFile(Long.toString(System.currentTimeMillis()), null, outputDir);
	        outputFile.deleteOnExit();
	        final RandomAccessFile randomAccessFile = new RandomAccessFile(outputFile, "rw");
	        final FileChannel channel = randomAccessFile.getChannel();
	        final MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
	        imgIn.copyPixelsToBuffer(map);
	        imgIn.recycle();
	        final Bitmap result = Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        result.copyPixelsFromBuffer(map);
	        channel.close();
	        randomAccessFile.close();
	        outputFile.delete();
	        return result;
	    } catch (final Exception e) {
	    } finally {
	        if (outputFile != null)
	            outputFile.delete();
	    }
	    return null;
	}
	
	
	
	
	int min = 0;
	int max =24;
	void initializeView(View view) {
		
//		mapView = (MapView) view.findViewById(R.id.map);
		
		nearByListView = (ListView) view.findViewById(R.id.list);
		
		homeIV = (ImageView) view.findViewById(R.id.homeIV);
		settingIV = (ImageView) view.findViewById(R.id.settingIV);
		createEventIV = (ImageView) view.findViewById(R.id.createEventIV);
		myEventsIV = (ImageView) view.findViewById(R.id.myEventsIV);
		nearByEventsIV = (ImageView) view.findViewById(R.id.nearByEventsIV);

		homeIV.setOnClickListener(bottomClickListener);
		settingIV.setOnClickListener(bottomClickListener);
		createEventIV.setOnClickListener(bottomClickListener);
		myEventsIV.setOnClickListener(bottomClickListener);
		nearByEventsIV.setOnClickListener(bottomClickListener);
		
		txt_current_location = (TextView) view.findViewById(R.id.txt_current_location);
		
		
		
//		All = (Button) view.findViewById(R.id.all);
//		sun = (Button) view.findViewById(R.id.sun);
//		mon = (Button) view.findViewById(R.id.mon);
//		tue = (Button) view.findViewById(R.id.tue);
//		wed = (Button) view.findViewById(R.id.wed);
//		thurs = (Button) view.findViewById(R.id.thu);
//		fri = (Button) view.findViewById(R.id.fri);
//		sat = (Button) view.findViewById(R.id.sat);
//		first = (TextView) view.findViewById(R.id.first);
//		second = (TextView) view.findViewById(R.id.second);
				
		
//		long a = 0,b=24;
//		RangeSeekBar<Long> seekBar = new RangeSeekBar<Long>(a,b,mContext);
//		seekBar.setOnRangeSeekBarChangeListener(seekBarListener);
//		LinearLayout layout = (LinearLayout) view.findViewById(R.id.main);
//		layout.addView(seekBar);
		
	}

	public boolean isGoogleMapsInstalled(){
		
		try{
			
			ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
			return true;
			
		} 
		catch(PackageManager.NameNotFoundException e){
			
			return false;
			
		}
		
	}
	
	
	private void showMap(){
		
		mapView.onResume();
		map = mapView.getMap();
		if(map!=null){ 
		
			try {
			
				MapsInitializer.initialize(mContext);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			} 
			
			
			try{
			
//			  map.setMyLocationEnabled(true);
//			  map.getUiSettings().setMyLocationButtonEnabled(true);
			  map.getUiSettings().isCompassEnabled();
			  map.getUiSettings().isRotateGesturesEnabled();
			  map.getUiSettings().isScrollGesturesEnabled();
			  map.getUiSettings().isTiltGesturesEnabled();
			  map.getUiSettings().isZoomControlsEnabled();
						
			}catch(Exception e){
				
				
			}
			
			getCurrentAddress(AppConstants.getLattitude(),AppConstants.getLogitude());
			
		}
	}
	
	
	
	
	
	
	//This listener is used for showing event list according to day selection.
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			resetBar();
			int id = v.getId();
			int dayNumber = -1;
			getResources().getColor(R.color.unselectedd);
//			All.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			sun.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			mon.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			tue.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			wed.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			thurs.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			fri.setBackgroundColor(getResources().getColor(R.color.unselectedd));
//			sat.setBackgroundColor(getResources().getColor(R.color.unselectedd));
			
			
			
			/*switch (id) {
			case R.id.all:
				if(mainList!=null){
				EventAdapter adapter = new EventAdapter(mContext, mainList);
				filterList = mainList;
				finalList = mainList;
				nearByListView.setAdapter(adapter);
				}
				All.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.mon:
				dayNumber = 2;
				mon.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.tue:
				dayNumber = 3;
				tue.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.wed:
				dayNumber = 4;
				wed.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.thu:
				dayNumber = 5;
				thurs.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.fri:
				dayNumber = 6;
				fri.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.sat:
				dayNumber = 7;
				sat.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
			case R.id.sun:
				dayNumber = 1;
				sun.setBackgroundColor(getResources().getColor(R.color.selectedd));
				break;
	
				
			default:
				break;
			}*/
			
			if(mainList!=null && dayNumber!=-1){
				filterList = new ArrayList<EventsBean>();
				EventsBean bean =null;
				for(int i=0;i<mainList.size();i++){
					bean = mainList.get(i);
					int number = Utill.getDayNumber(bean.getEvent_date(),bean.getEvent_time());
					int timeNumber = Utill.get24HrFormatTime(bean.getEvent_time());
					if(number == dayNumber && (timeNumber>=min && timeNumber<max)){
						filterList.add(mainList.get(i));
					}
				}
				finalList = filterList;
				EventAdapter adapter = new EventAdapter(mContext, filterList);
				nearByListView.setAdapter(adapter);
				
			}else{
			}

		}
	};

	public void setOnClickeListeners() {
	
		
//		All.setOnClickListener(clickListener);
//		sun.setOnClickListener(clickListener);
//		mon.setOnClickListener(clickListener);
//		tue.setOnClickListener(clickListener);
//		wed.setOnClickListener(clickListener);
//		thurs.setOnClickListener(clickListener);
//		fri.setOnClickListener(clickListener);
//		sat.setOnClickListener(clickListener);

		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentManager.getBackStackEntryCount() > 0) {
					
					mFragmentManager.popBackStack();
					
				}
			}
		});
		
		
		/*DashBoard.rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mainList!=null && mainList.size()>0){
					Intent i = new Intent(mActivity, ShowAllNearByUsersActivity.class);
					startActivityForResult(i,900);
				}
			}
		});*/
		
		
		nearByListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				NearByEventDetailFragement.bean = finalList.get(arg2);
				dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT_DETAIL);
			}
		});
	}

	ProgressDialog progress;
	public static Activity mActivity;

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}

	// This method is used for showing progress bar
	public void showProgress() {
		try {
			if (progress == null)
				progress = new ProgressDialog(mActivity);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				progress = new ProgressDialog(mActivity);
				progress.setMessage("Please Wait..");
				progress.setCancelable(false);
				progress.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// This method is used for hiding progress bar.
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

//	@Override
//	public void SeekBarValueChanged(int Thumb1Value, int Thumb2Value) {
//		System.out.println("Thumb 1 "+Thumb1Value);
//		System.out.println("Thumb 2 "+Thumb2Value);
//		
//	}
//	
//	
//	OnRangeSeekBarChangeListener seekBarListener = new OnRangeSeekBarChangeListener<Long>() {
//		@Override
//		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue,Long maxValue) {
//            // handle changed range values
//        	Log.e("min", ""+minValue);
//        	Log.e("max", ""+maxValue);
//        	first.setText(minValue+":00");
//        	second.setText(maxValue+":00");
//        	min = Integer.parseInt(""+minValue);
//        	max = Integer.parseInt(""+maxValue);
//        	finalList = new ArrayList<EventsBean>();
//        	if(filterList!=null){
//				EventsBean bean =null;
//				 for(int i=0;i<filterList.size();i++){
//					bean = filterList.get(i);
//					//int number = Utill.getDayNumber(bean.getEvent_date(),bean.getEvent_time());
//					int timeNumber = Utill.get24HrFormatTime(bean.getEvent_time());
//					if((timeNumber>=min && timeNumber<max)){
//						finalList.add(filterList.get(i));
//					}
//				}
//				
//				EventAdapter adapter = new EventAdapter(mContext,finalList);
//				nearByListView.setAdapter(adapter);
//			}
//        }
//	};
//	
//	//for resetting the bar to its initial position.
//	void resetBar(){
//		long a = 0,b=24;
//		min = 0;
//		max = 24;
//		first.setText("00:00");
//		second.setText("24:00");
//		RangeSeekBar<Long> seekBar = new RangeSeekBar<Long>(a,b,mContext);
//		seekBar.setOnRangeSeekBarChangeListener(seekBarListener);
//		LinearLayout layout = (LinearLayout)getView().findViewById(R.id.main);
//		layout.removeAllViews();
//		layout.addView(seekBar);
//	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
//		if (requestCode == 900 && resultCode == Activity.RESULT_OK) {
//			super.onActivityResult(requestCode, resultCode, data);
//			int index = data.getExtras().getInt("index");
//			NearByEventDetailFragement.bean = mainList.get(index);
//			dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT_DETAIL);
//
//		}

	}

}
