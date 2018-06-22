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
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasar.multirow.costomspinner.MaterialSpinner;
import com.example.yasar.multirow.dummy.model.CityEvent;
import com.example.yasar.multirow.dummy.model.SliderModel;
import com.example.yasar.multirow.notificationpackage.NotificationUtils;
import com.example.yasar.multirow.request.Postdata;
import com.example.yasar.multirow.utility.Config;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivitybackup extends AppCompatActivity implements SearchView.OnQueryTextListener, ActionBar.OnNavigationListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = "MainActivity";

    public static final String MyPREFERENCES = "MyPrefsJson";

    private SharedPreferences sharedpreferences;

    private List<CityEvent> list = new ArrayList<>();

    private DifferentRowAdapter adapter;
    private Timer t;
    private String JSON = "jsonOfflineUpdate";

    private CustomArrayAdapter aAddpt;

    private int scrollPosition = 0;
    ArrayList<String> categoryList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;

    private String defaultSlectedCategory = "All";


    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.customactionbarview, null);

        MaterialSpinner spinner = (MaterialSpinner) mCustomView.findViewById(R.id.spinner);
//        spinner.setOnItemSelectedListener(this);

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

        categoryList.add("All categories");

        aAddpt = new CustomArrayAdapter(this, R.layout.spinnertextlayout, categoryList);
        spinner.setAdapter(aAddpt);

//        // Creating adapter for spinner
//        aAddpt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
//
//        // Drop down layout style - list view with radio button
//        aAddpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(aAddpt);

        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);


//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//
//

//
//        actionBar.setListNavigationCallbacks(aAddpt, this);

        adapter = new DifferentRowAdapter(list, this);
        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);


//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (linearLayoutManager != null) {
//                    scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
//        if (linearLayoutManager != null) {
//            mRecyclerView.scrollToPosition(scrollPosition);
//        }


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    Toast.makeText(MainActivitybackup.this, "Ok Complete", Toast.LENGTH_SHORT).show();

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // newimage push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "New Ads Posted", Toast.LENGTH_LONG).show();

                    Log.e(TAG, "onReceive: " + message);
                    updateRecyclerView(defaultSlectedCategory);
                }
            }
        };

        displayFirebaseRegId();
        updateRecyclerView(defaultSlectedCategory);


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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Toast.makeText(MainActivitybackup.this, newText, Toast.LENGTH_SHORT).show();

        List<CityEvent> backUp = list;


        final List<CityEvent> filteredList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            ArrayList<SliderModel> sliderimage = list.get(i).getSliderimage();

            for (int j = 0; j < sliderimage.size(); j++) {

                final String businessname = sliderimage.get(j).getBusinessName().toLowerCase();
                final String shopname = sliderimage.get(j).getShopName().toLowerCase();

                if (businessname.contains(newText) || shopname.contains(newText)) {

                    filteredList.add(list.get(i));


                }

            }


        }

        if (newText.length() > 0) {
            adapter.UpdateList(filteredList);

            adapter.notifyDataSetChanged();
        } else {
            adapter.UpdateList(backUp);

            adapter.notifyDataSetChanged();
        }


        return true;
    }


    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private List<String> data;
        private Context context;
        public Resources res;
        public LayoutInflater inflater;

        public CustomArrayAdapter(Context context, int resourceId,
                                  List<String> objects) {
            super(context, resourceId, objects);
            this.data = objects;
            this.context = context;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View row;
//        LayoutInflater inflater=getLayoutInflater();
            row = inflater.inflate(R.layout.spinnertextlayout, null);
            TextView _textView = (TextView) row.findViewById(R.id.textView);
            _textView.setText(data.get(position).toString());


            Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int _width = size.x;

            row.setMinimumWidth(_width - 100);
//            row.setMinimumWidth(_textView.getWidth());
            return row;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {


            View row = inflater.inflate(R.layout.spinnertextlayout, parent, false);
            TextView label = (TextView) row.findViewById(R.id.textView);

            label.setTextColor(Color.WHITE);

            RelativeLayout.LayoutParams llp11 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            llp11.setMargins(0, 0, 0, 0);
            label.setLayoutParams(llp11);
            label.setText(data.get(position).toString());
            label.setSingleLine(true);

//            View row = inflater.inflate(R.layout.spinnertextlayout, parent, false);
//
//            TextView tvCategory = (TextView) row.findViewById(R.id.textView);
//
//            tvCategory.setText(data.get(position).toString());
//
//            tvCategory.setTextColor(Color.WHITE);

            return row;

        }

    }

    private void updateRecyclerView(String category) {
        if (category.equalsIgnoreCase("All categories")) {
            category = "all";
        }
        if (isInternetOn()) {
            new getData().execute(category);
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
            new Postdata().execute("http://192.168.0.26/xirid/deviceregister.php", regId);
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

//        //Declare the timer
//        t = newimage Timer();
////Set the schedule function and rate
//        t.scheduleAtFixedRate(newimage TimerTask() {
//
//                                  @Override
//                                  public void run() {
//                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
//                                      newimage getData().execute();
//                                  }
//
//                              },
////Set how long before to start calling the TimerTask (in milliseconds)
//                0,
////Set the amount of time between each execution (in milliseconds)
//                1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
      /*  Log.e(TAG, "onPause: ");
        t.cancel();*/
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
        if (!categoryList.isEmpty()) {
            updateRecyclerView(categoryList.get(position));
            defaultSlectedCategory = categoryList.get(position);

//            RelativeLayout r = (RelativeLayout) view.findViewById(R.id.r);
//            r.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
//            TextView t = (TextView) view.findViewById(R.id.textView);
//            t.setTextColor(Color.WHITE);

        }

//        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class getData extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://192.168.0.26/xirid/getjson.php?appcategory=" + args[0]);

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
            try {
                JSONObject jsonObj = new JSONObject(result);


                // Getting JSON Array node
                JSONArray category = jsonObj.getJSONArray("category");

                categoryList.clear();

                categoryList.add("All categories");

                for (int i = 0; i < category.length(); i++) {
                    JSONObject c = category.getJSONObject(i);
                    categoryList.add(c.getString("categoryname"));
                }


                // Getting JSON Array node
                JSONArray slider = jsonObj.getJSONArray("slider");

                ArrayList<SliderModel> sliderlist = new ArrayList<>();
                sliderlist.clear();

                for (int i = 0; i < slider.length(); i++) {
                    JSONObject c = slider.getJSONObject(i);
                    SliderModel sm = new SliderModel();
                    if (!c.getString("firstimage").equalsIgnoreCase("")) {
                        sm.setFirstimage("http://192.168.0.26/xirid/" + c.getString("firstimage"));
                    }

                    if (!c.getString("secondimage").equalsIgnoreCase("")) {
                        sm.setSecondimage("http://192.168.0.26/xirid/" + c.getString("secondimage"));
                    }

                    if (!c.getString("thirdimage").equalsIgnoreCase("")) {
                        sm.setThirdimage("http://192.168.0.26/xirid/" + c.getString("thirdimage"));
                    }

                    if (!c.getString("fourthimage").equalsIgnoreCase("")) {
                        sm.setFourthimage("http://192.168.0.26/xirid/" + c.getString("fourthimage"));
                    }

                    if (!c.getString("fifthimage").equalsIgnoreCase("")) {
                        sm.setFifthimage("http://192.168.0.26/xirid/" + c.getString("fifthimage"));
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
                    sm.setThumpimage("http://192.168.0.26/xirid/" + c.getString("thumpimage"));

//                    String getvalues = getValuesNewItems("newitem");
//                    if (getvalues == null) {
//                        saveNewItems("newitem", "true");
//                    }
////                    else {
////                        saveNewItems("newitem", "false");
////                    }
//                    String newitem = getValuesNewItems("newitem");
//
//                    Log.e(TAG, "newitem " + newitem);

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

                    if (!rectangleurl.getString("firstimage").equalsIgnoreCase("")) {
                        smrectangle.setFirstimage("http://192.168.0.26/xirid/" + rectangleurl.getString("firstimage"));
                    }

                    if (!rectangleurl.getString("secondimage").equalsIgnoreCase("")) {
                        smrectangle.setSecondimage("http://192.168.0.26/xirid/" + rectangleurl.getString("secondimage"));
                    }

                    if (!rectangleurl.getString("thirdimage").equalsIgnoreCase("")) {
                        smrectangle.setThirdimage("http://192.168.0.26/xirid/" + rectangleurl.getString("thirdimage"));
                    }

                    if (!rectangleurl.getString("fourthimage").equalsIgnoreCase("")) {
                        smrectangle.setFourthimage("http://192.168.0.26/xirid/" + rectangleurl.getString("fourthimage"));
                    }

                    if (!rectangleurl.getString("fifthimage").equalsIgnoreCase("")) {
                        smrectangle.setFifthimage("http://192.168.0.26/xirid/" + rectangleurl.getString("fifthimage"));
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
                    smrectangle.setThumpimage("http://192.168.0.26/xirid/" + rectangleurl.getString("thumpimage"));

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
                }


                JSONArray banner = jsonObj.getJSONArray("banner");


                Log.e(TAG, "onPostExecute: banner " + banner.length());

                for (int j = 0; j < banner.length(); j++) {
                    JSONObject bannerurl = banner.getJSONObject(j);
                    ArrayList<SliderModel> bannerlist = new ArrayList<>();
                    SliderModel smbanner = new SliderModel();

                    if (!bannerurl.getString("firstimage").equalsIgnoreCase("")) {
                        smbanner.setFirstimage("http://192.168.0.26/xirid/" + bannerurl.getString("firstimage"));
                    }

                    if (!bannerurl.getString("secondimage").equalsIgnoreCase("")) {
                        smbanner.setSecondimage("http://192.168.0.26/xirid/" + bannerurl.getString("secondimage"));
                    }

                    if (!bannerurl.getString("thirdimage").equalsIgnoreCase("")) {
                        smbanner.setThirdimage("http://192.168.0.26/xirid/" + bannerurl.getString("thirdimage"));
                    }

                    if (!bannerurl.getString("fourthimage").equalsIgnoreCase("")) {
                        smbanner.setFourthimage("http://192.168.0.26/xirid/" + bannerurl.getString("fourthimage"));
                    }

                    if (!bannerurl.getString("fifthimage").equalsIgnoreCase("")) {
                        smbanner.setFifthimage("http://192.168.0.26/xirid/" + bannerurl.getString("fifthimage"));
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
                    smbanner.setThumpimage("http://192.168.0.26/xirid/" + bannerurl.getString("thumpimage"));

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

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.notifyDataSetChanged();

        aAddpt.notifyDataSetChanged();

//        new Postdata().execute("http://192.168.0.26/xirid/updatenewitem.php", "");


    }

}
