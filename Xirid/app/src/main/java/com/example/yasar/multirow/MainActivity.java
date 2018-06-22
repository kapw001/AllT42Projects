package com.example.yasar.multirow;

import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasar.multirow.costomspinner.MaterialSpinner;
import com.example.yasar.multirow.dummy.DummyData;
import com.example.yasar.multirow.dummy.model.CityEvent;
import com.example.yasar.multirow.dummy.model.SliderModel;
import com.example.yasar.multirow.notificationpackage.NotificationUtils;
import com.example.yasar.multirow.request.Postdata;
import com.example.yasar.multirow.utility.Config;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ActionBar.OnNavigationListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    public static String BASE_URL = "http://ec2-52-66-137-137.ap-south-1.compute.amazonaws.com/xirid/";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = "MainActivity";

    public static final String MyPREFERENCES = "MyPrefsJson";

    private SharedPreferences sharedpreferences;

    private List<CityEvent> list = new ArrayList<>();

    private List<CityEvent> listSearch = new ArrayList<>();

    private DifferentRowAdapter adapter;
    private Timer t;
    private String JSON = "jsonOfflineUpdate";

    private ArrayAdapter aAddpt, aAddpt1;

    private int scrollPosition = 0;
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> locationList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;

    private String defaultSlectedCategory = "All";
    private String defaultSlectedLocation = "All";


    private SearchView searchView;
    private MenuItem searchMenuItem;

    private Spinner spinner, spinner2;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)

                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        // Set up the action bar to show a dropdown list.
//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);

//        LayoutInflater mInflater = LayoutInflater.from(this);
//
//        View mCustomView = mInflater.inflate(R.layout.customactionbarview, null);

        categoryList.add("All categories");

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner2 = (Spinner) findViewById(R.id.spinner2);

//        spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//
//        spinner2.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        spinner.setOnItemSelectedListener(this);

        spinner2.setOnItemSelectedListener(this);

//        categoryList.add("Test");
//        categoryList.add("TestTest");
//        categoryList.add("TestTestTestTest");
//        categoryList.add("TestTestTest");
//        categoryList.add("TestTestTestTestTest");
//        categoryList.add("TestTestTestTestTestTest");
//        categoryList.add("TestTestTestTestTestTestTest");


//        aAddpt = new ArrayAdapter<String>(this, R.layout.spinnertextlayout, categoryList);
//        aAddpt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,android.R.id.text1, categoryList);
//        aAddpt = new ArrayAdapter<String>(this, R.layout.spinnertextlayout, R.id.textView, categoryList);
//        spinner.setAdapter(aAddpt);


//        aAddpt = new CustomArrayAdapter(this, R.layout.spinnertextlayout, categoryList);
//        spinner.setAdapter(aAddpt);

//        // Creating adapter for spinner
        aAddpt = new ArrayAdapter<String>(this, R.layout.spinner_custom_textcolor, categoryList);
//
//        // Drop down layout style - list view with radio button
        aAddpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(aAddpt);
        locationList.add("All");
//        aAddpt1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationList);
        aAddpt1 = new ArrayAdapter<String>(this, R.layout.spinner_custom_textcolor, locationList);
//
//        // Drop down layout style - list view with radio button
        aAddpt1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aAddpt);
        spinner2.setAdapter(aAddpt1);

//        actionBar.setCustomView(mCustomView);
//        actionBar.setDisplayShowCustomEnabled(true);


//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//

//
//        actionBar.setListNavigationCallbacks(aAddpt, this);

        adapter = new DifferentRowAdapter(list, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#3F51B5"));
        swipeRefreshLayout.setColorSchemeResources(
                R.color.white,
                R.color.white,
                R.color.white);
        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    Toast.makeText(MainActivity.this, "Ok Complete", Toast.LENGTH_SHORT).show();

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // newimage push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "New Ads Posted", Toast.LENGTH_LONG).show();

                    Log.e(TAG, "onReceive: " + message);
                    updateRecyclerView(defaultSlectedCategory, defaultSlectedLocation);
                }
            }
        };

        displayFirebaseRegId();
        updateRecyclerView(defaultSlectedCategory, defaultSlectedLocation);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        // Inflate menu to add items to action bar if it is present.
//        inflater.inflate(R.menu.menu, menu);
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(this);

//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchMenuItem = menu.findItem(R.id.menu_search);
//        searchView = (SearchView) searchMenuItem.getActionView();
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(this);
//
//        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        // Do something when collapsed
//                        adapter.setFilter(list);
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        // Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();

//        List<CityEvent> backUp = list;
//
//
//        final List<CityEvent> filteredList = new ArrayList<>();
//
//        for (int i = 0; i < list.size(); i++) {
//
//            ArrayList<SliderModel> sliderimage = list.get(i).getSliderimage();
//
//            for (int j = 0; j < sliderimage.size(); j++) {
//
//                final String businessname = sliderimage.get(j).getBusinessName().toLowerCase();
//                final String shopname = sliderimage.get(j).getShopName().toLowerCase();
//
//                if (businessname.contains(newText) || shopname.contains(newText)) {
//
//                    filteredList.add(list.get(i));
//
//
//                }
//
//            }
//
//
//        }
//
//        if (newText.length() > 0) {
//            adapter.UpdateList(filteredList);
//
//            adapter.notifyDataSetChanged();
//        } else {
//            adapter.UpdateList(backUp);
//
//            adapter.notifyDataSetChanged();
//        }


        final List<CityEvent> filteredModelList = filter(listSearch, newText);

        if (newText.length() > 0) {
            adapter.setFilter(filteredModelList);
        } else {
            adapter.setFilter(list);
        }


        return true;
    }

    private List<CityEvent> filter(List<CityEvent> models, String query) {
        query = query.toLowerCase();

        final List<CityEvent> filteredModelList = new ArrayList<>();

        filteredModelList.clear();
        for (CityEvent model : models) {

            final ArrayList<SliderModel> slideList = model.getSliderimage();

//            final ArrayList<SliderModel> slideListNewAdd = new ArrayList<>();

            for (SliderModel sliderModel : slideList) {

                final String text = sliderModel.getBusinessName().toLowerCase();
                if (text.contains(query)) {

//                    slideListNewAdd.add(sliderModel);
                    filteredModelList.add(model);


//                    filteredModelList.add(model);
                    Log.e(TAG, "filter: " + sliderModel.getBusinessName());
                }
            }
//            filteredModelList.add(new CityEvent(slideListNewAdd, CityEvent.BANNER_TYPE));
        }

        return filteredModelList;
    }


    @Override
    public void onRefresh() {
        updateRecyclerView(defaultSlectedCategory, defaultSlectedLocation);
    }


    private void updateRecyclerView(String category, String location) {
        if (category.equalsIgnoreCase("All categories")) {
            category = "all";
        }
        if (location.equalsIgnoreCase("All")) {
            location = "all";
        }
        if (isInternetOn()) {
            new getData().execute(category, location);
            Log.e(TAG, "updateRecyclerView: Callled" + " " + category + "          " + location);
        } else {
            SharedPreferences preferences = sharedpreferences;
            String savedResultJson = preferences.getString(JSON, null);

            if (savedResultJson != null) {
                parseJson(savedResultJson);
            }
            Log.e(TAG, "onPostExecute:savedResultJson " + savedResultJson);
        }
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            Log.e(TAG, "displayFirebaseRegId: Firebase Reg Id: " + regId);
            new Postdata().execute(BASE_URL + "deviceregister.php", regId);
        } else {

            Log.e(TAG, "Firebase Reg Id is not received yet! ");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");


// register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register newimage push message receiver
        // by doing this, the activity will be notified each time a newimage message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        Log.e(TAG, "run: Called");
                    }
                });


            }
        }, 0, 60 * 5 * 1000);


    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
      /*  Log.e(TAG, "onPause: ");
        t.cancel();*/
        timer.cancel();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        ;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner:
                defaultSlectedCategory = categoryList.get(position);
                Log.e(TAG, "onItemSelected: " + defaultSlectedCategory + "    " + categoryList.get(position));
                if (!categoryList.isEmpty()) {
                    updateRecyclerView(defaultSlectedCategory, defaultSlectedLocation);
                }
                break;
            case R.id.spinner2:
                defaultSlectedLocation = locationList.get(position);
                if (!locationList.isEmpty()) {
                    updateRecyclerView(defaultSlectedCategory, defaultSlectedLocation);
                }
                break;
        }

        Log.e(TAG, "onItemSelected: " + defaultSlectedCategory + "              " + defaultSlectedLocation);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class getData extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                String category = URLEncoder.encode(args[0], "utf-8");
                String location = URLEncoder.encode(args[1], "utf-8");
                URL url = new URL(BASE_URL + "getjson.php?appcategory=" + category + "&applocationsearch=" + location);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            swipeRefreshLayout.setRefreshing(false);
//            Log.e(TAG, "onPostExecute: " + result);

            if (result != null && !result.equalsIgnoreCase("")) {

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(JSON, result);
//                editor.clear();
//                editor.apply();
                editor.commit();

                Log.e(TAG, "onPostExecute: Calling test Test");
//
//                Log.e(TAG, "onPostExecute: Called Test");
//
//                String savedResultJson = sharedpreferences.getString(JSON, null);
//
//                Log.e(TAG, "onPostExecute: " + savedResultJson);

                parseJson(result);
            }


            //Do something with the JSON string


        }

    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

//            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

//            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    private void saveNewItems(String key, String values) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, values);
        editor.commit();
    }

    private String getValuesNewItems(String key) {
        SharedPreferences prefs = sharedpreferences;
        String restoredText = prefs.getString(key, null);
        return restoredText;

    }

    public void parseJson(String result) {
        if (result != null) {
            list.clear();
            listSearch.clear();
            try {
                JSONObject jsonObj = new JSONObject(result);


                // Getting JSON Array node
                JSONArray category = jsonObj.getJSONArray("category");

                if (category != null) {

                    categoryList.clear();

                    categoryList.add("All categories");

                    for (int i = 0; i < category.length(); i++) {
                        JSONObject c = category.getJSONObject(i);
                        categoryList.add(c.getString("categoryname"));
                    }
                }


                JSONArray location = jsonObj.getJSONArray("location");

                if (location != null) {

                    locationList.clear();

                    locationList.add("All");

                    for (int i = 0; i < location.length(); i++) {
                        JSONObject c = location.getJSONObject(i);
                        locationList.add(c.getString("location"));
                    }
                }


                // Getting JSON Array node
                JSONArray slider = jsonObj.getJSONArray("slider");

                ArrayList<SliderModel> sliderlist = new ArrayList<>();
                sliderlist.clear();

                for (int i = 0; i < slider.length(); i++) {
                    JSONObject c = slider.getJSONObject(i);
                    SliderModel sm = new SliderModel();
                    if (!c.getString("bannerimage").equalsIgnoreCase("")) {
                        sm.setSliderImage(BASE_URL + c.getString("bannerimage"));
                    }
                    if (!c.getString("firstimage").equalsIgnoreCase("")) {
                        sm.setFirstimage(BASE_URL + c.getString("firstimage"));
                    }

                    if (!c.getString("secondimage").equalsIgnoreCase("")) {
                        sm.setSecondimage(BASE_URL + c.getString("secondimage"));
                    }

                    if (!c.getString("thirdimage").equalsIgnoreCase("")) {
                        sm.setThirdimage(BASE_URL + c.getString("thirdimage"));
                    }

                    if (!c.getString("fourthimage").equalsIgnoreCase("")) {
                        sm.setFourthimage(BASE_URL + c.getString("fourthimage"));
                    }

                    if (!c.getString("fifthimage").equalsIgnoreCase("")) {
                        sm.setFifthimage(BASE_URL + c.getString("fifthimage"));
                    }

                    sm.setShopName(c.getString("shopname"));
                    sm.setAddress(c.getString("address1"));
                    sm.setPlace(c.getString("place"));
                    sm.setCity(c.getString("city"));
                    sm.setPincode(c.getString("state"));
                    sm.setState(c.getString("pincode"));
                    sm.setContactNumber(c.getString("contactnumber1"));
                    sm.setBusinessName(c.getString("productname"));
                    sm.setProductDescription(c.getString("productdescription"));
                    sm.setStartDate(c.getString("startdate"));
                    sm.setEndDate(c.getString("enddate"));
                    sm.setThumpimage(BASE_URL + c.getString("thumpimage"));
                    sm.setWebsitelink(c.getString("imagelink"));

                    sm.setNewitem("false");

                    sliderlist.add(sm);
                }

                if (!sliderlist.isEmpty()) {
                    list.add(new CityEvent(sliderlist, CityEvent.SLIDER_TYPE));
                }


                JSONArray rectangle = jsonObj.getJSONArray("rectangle");

                ArrayList<SliderModel> rectanglelist = new ArrayList<>();
                rectanglelist.clear();

                for (int j = 0; j < rectangle.length(); j++) {
                    JSONObject rectangleurl = rectangle.getJSONObject(j);

                    SliderModel smrectangle = new SliderModel();

                    if (!rectangleurl.getString("bannerimage").equalsIgnoreCase("")) {
                        smrectangle.setSliderImage(BASE_URL + rectangleurl.getString("bannerimage"));
                    }

                    if (!rectangleurl.getString("firstimage").equalsIgnoreCase("")) {
                        smrectangle.setFirstimage(BASE_URL + rectangleurl.getString("firstimage"));
                    }

                    if (!rectangleurl.getString("secondimage").equalsIgnoreCase("")) {
                        smrectangle.setSecondimage(BASE_URL + rectangleurl.getString("secondimage"));
                    }

                    if (!rectangleurl.getString("thirdimage").equalsIgnoreCase("")) {
                        smrectangle.setThirdimage(BASE_URL + rectangleurl.getString("thirdimage"));
                    }

                    if (!rectangleurl.getString("fourthimage").equalsIgnoreCase("")) {
                        smrectangle.setFourthimage(BASE_URL + rectangleurl.getString("fourthimage"));
                    }

                    if (!rectangleurl.getString("fifthimage").equalsIgnoreCase("")) {
                        smrectangle.setFifthimage(BASE_URL + rectangleurl.getString("fifthimage"));
                    }

                    smrectangle.setShopName(rectangleurl.getString("shopname"));
                    smrectangle.setAddress(rectangleurl.getString("address1"));
                    smrectangle.setPlace(rectangleurl.getString("place"));
                    smrectangle.setCity(rectangleurl.getString("city"));
                    smrectangle.setPincode(rectangleurl.getString("state"));
                    smrectangle.setState(rectangleurl.getString("pincode"));
                    smrectangle.setContactNumber(rectangleurl.getString("contactnumber1"));
                    smrectangle.setBusinessName(rectangleurl.getString("productname"));
                    smrectangle.setProductDescription(rectangleurl.getString("productdescription"));
                    smrectangle.setStartDate(rectangleurl.getString("startdate"));
                    smrectangle.setEndDate(rectangleurl.getString("enddate"));
                    smrectangle.setThumpimage(BASE_URL + rectangleurl.getString("thumpimage"));

                    smrectangle.setWebsitelink(rectangleurl.getString("imagelink"));

                    String getvalues = getValuesNewItems(rectangleurl.getString("id"));
                    String newitem = "false";
                    if (getvalues == null) {
                        saveNewItems(rectangleurl.getString("id"), "true");
                        newitem = getValuesNewItems(rectangleurl.getString("id"));
                    }

                    Log.e(TAG, "newitem " + newitem + "   " + rectangleurl.getString("id") + "  values " + getvalues);

                    smrectangle.setNewitem(newitem);

                    rectanglelist.add(smrectangle);


                }

                if (!rectanglelist.isEmpty()) {

                    list.add(new CityEvent(rectanglelist, CityEvent.RECTANGLE_TYPE));
                    listSearch.add(new CityEvent(rectanglelist, CityEvent.BANNER_TYPE));
                }


                JSONArray banner = jsonObj.getJSONArray("banner");


                Log.e(TAG, "onPostExecute: banner " + banner.length());

                for (int j = 0; j < banner.length(); j++) {
                    JSONObject bannerurl = banner.getJSONObject(j);
                    ArrayList<SliderModel> bannerlist = new ArrayList<>();
                    SliderModel smbanner = new SliderModel();

                    if (!bannerurl.getString("bannerimage").equalsIgnoreCase("")) {
                        smbanner.setSliderImage(BASE_URL + bannerurl.getString("bannerimage"));
                    }

                    if (!bannerurl.getString("firstimage").equalsIgnoreCase("")) {
                        smbanner.setFirstimage(BASE_URL + bannerurl.getString("firstimage"));
                    }

                    if (!bannerurl.getString("secondimage").equalsIgnoreCase("")) {
                        smbanner.setSecondimage(BASE_URL + bannerurl.getString("secondimage"));
                    }

                    if (!bannerurl.getString("thirdimage").equalsIgnoreCase("")) {
                        smbanner.setThirdimage(BASE_URL + bannerurl.getString("thirdimage"));
                    }

                    if (!bannerurl.getString("fourthimage").equalsIgnoreCase("")) {
                        smbanner.setFourthimage(BASE_URL + bannerurl.getString("fourthimage"));
                    }

                    if (!bannerurl.getString("fifthimage").equalsIgnoreCase("")) {
                        smbanner.setFifthimage(BASE_URL + bannerurl.getString("fifthimage"));
                    }


                    smbanner.setShopName(bannerurl.getString("shopname"));
                    smbanner.setAddress(bannerurl.getString("address1"));
                    smbanner.setPlace(bannerurl.getString("place"));
                    smbanner.setCity(bannerurl.getString("city"));
                    smbanner.setPincode(bannerurl.getString("state"));
                    smbanner.setState(bannerurl.getString("pincode"));
                    smbanner.setContactNumber(bannerurl.getString("contactnumber1"));
                    smbanner.setBusinessName(bannerurl.getString("productname"));
                    smbanner.setProductDescription(bannerurl.getString("productdescription"));
                    smbanner.setStartDate(bannerurl.getString("startdate"));
                    smbanner.setEndDate(bannerurl.getString("enddate"));
                    smbanner.setThumpimage(BASE_URL + bannerurl.getString("thumpimage"));

                    smbanner.setWebsitelink(bannerurl.getString("imagelink"));

                    String getvalues = getValuesNewItems(bannerurl.getString("id"));
                    String newitem = "false";
                    if (getvalues == null) {
                        saveNewItems(bannerurl.getString("id"), "true");
                        newitem = getValuesNewItems(bannerurl.getString("id"));
                    }

                    Log.e(TAG, "newitem " + newitem + "   " + bannerurl.getString("id") + "  values " + getvalues);

                    smbanner.setNewitem(newitem);

                    bannerlist.add(smbanner);
                    list.add(new CityEvent(bannerlist, CityEvent.BANNER_TYPE));
                    listSearch.add(new CityEvent(bannerlist, CityEvent.BANNER_TYPE));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();
        aAddpt.notifyDataSetChanged();
        aAddpt1.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

//        new Postdata().execute("http://192.168.0.26/xirid/updatenewitem.php", "");


    }

}
