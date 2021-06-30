package com.example.searchevent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchFragment extends Fragment implements View.OnClickListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = MainActivity.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spinner1;
    Spinner spinner2;
    Button clear;
    Button search;
    EditText getkeyword;
    EditText inputdistance;
    EditText inputlocation;
    RadioButton radioButton4;
    RadioButton radioButton5;
    Double latitude, longitude, currlat, currlog;
    FusedLocationProviderClient fusedLocationProviderClient;
    JsonObjectRequest jsonObjectRequest;
    String url, getsearchurl, googleurl;
    String keyword, unit, category, radius, cateID;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        spinner1 = rootView.findViewById(R.id.spinner1);
        initspinnerfooter();
        spinner2 = rootView.findViewById(R.id.spinner2);
        initunitspanner();
        clear = rootView.findViewById(R.id.clearbutton);
        search = rootView.findViewById(R.id.searchbutton);
        clear.setOnClickListener(this);
        search.setOnClickListener(this);
        getkeyword = rootView.findViewById(R.id.getkeyword);
        inputdistance = rootView.findViewById(R.id.inputdistance);
        inputlocation = rootView.findViewById(R.id.inputlocation);
        radioButton4 = rootView.findViewById(R.id.radioButton4);
        radioButton5 = rootView.findViewById(R.id.radioButton5);

        url = "https://hw8-316909.wl.r.appspot.com";

        googleurl = "";

        //get current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //check permission
        if (ActivityCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            getlocation();
        } else {
            //when permission denied
            ActivityCompat.requestPermissions(getActivity()
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        return rootView;
    }

    private void getlocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();
                if (location != null) {
                    //initialize address list
                    currlat = location.getLatitude();
                    currlog = location.getLongitude();
                }
            }
        });
    }

    public LatLng getLocationFromAddress(String addressStr){
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(addressStr,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            p1 = new LatLng((double) (location.getLatitude()), (double) (location.getLongitude()));
            return p1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void initspinnerfooter(){
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),R.array.category, android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initunitspanner(){
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.unit, android.R.layout.simple_spinner_item);
        spinner2.setAdapter(adapter2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    //implentement clear button
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.searchbutton:
                if (TextUtils.isEmpty(getkeyword.getText().toString().trim())) {
                    getkeyword.setError("Please enter mandatory field");
                    if (TextUtils.isEmpty(inputlocation.getText().toString().trim()) && radioButton5.isChecked()) {
                        inputlocation.setError("Please enter mandatory field");
                    }
                }else{

                    String loc = inputlocation.getText().toString();
                    Log.d("Location is", inputlocation.getText().toString());
                    if(radioButton5.isChecked()){
                        LatLng geo = getLocationFromAddress(loc);
                        Log.d("LATITUDE is", geo.toString());
                        latitude = geo.latitude;
                        longitude = geo.longitude;
                    }else{
                        latitude = currlat;
                        longitude = currlog;
                    }
                    keyword = getkeyword.getText().toString();
                    unit = spinner2.getSelectedItem().toString();
                    category = spinner1.getSelectedItem().toString();
                    radius = inputdistance.getText().toString();
                    try {
                        searchevent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.clearbutton: {

                getkeyword.getText().clear();
                inputdistance.setText("10");
                inputlocation.getText().clear();
                getkeyword.setError(null);
                inputlocation.setError(null);
                radioButton4.setChecked(true);
                spinner1.setSelection(0);
                spinner2.setSelection(0);
                break;
            }
        }

    }

    private void searchevent() throws JSONException {
        String sendunit;
        if (unit.equals("miles")) {
            sendunit = "miles";
        } else {
            sendunit = "km";
        }
        if (category.equals("All")) {
            cateID = "Default";
            //getsearchurl = url + "/search?&keyword=" + keyword + "&location="
            //+ latitude.toString() + "," + longitude.toString() + "&radius=" + radius + "&unit=" + sendunit;// +"&apikey=B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8";
        } else {
            if (category.equals("Music")) {
                cateID = "KZFzniwnSyZfZ7v7nJ";
            } else if (category.equals("Sport")) {
                cateID = "KZFzniwnSyZfZ7v7nE";
            } else if (category.equals("Film")) {
                cateID = "KZFzniwnSyZfZ7v7nn";
            } else if (category.equals("Miscellaneous")) {
                cateID = "KZFzniwnSyZfZ7v7n1";
            } else {
                cateID = "KZFzniwnSyZfZ7v7na";
            }
           // +"&apikey=B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8";
        }
        getsearchurl = url + "/search?keyword=" + keyword + "&location="
                + latitude.toString() + "," + longitude.toString() + "&radius=" + radius + "&unit=" + sendunit +"&category=" + cateID;
        Log.d(TAG, "  url is   " +getsearchurl);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, getsearchurl, (JSONObject) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray eventlist = response.getJSONObject("_embedded").getJSONArray("events");
                                JSONObject[] events =  new JSONObject[eventlist.length()];

                                ArrayList<String> eventname = new ArrayList<String>();
                                ArrayList<String> eventdate = new ArrayList<String>();
                                ArrayList<String> locationlist = new ArrayList<String>();
                                for(int i = 0; i < eventlist.length(); i++){
                                    String date = eventlist.getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("localDate");
                                    eventdate.add(date);
                                    String event = eventlist.getJSONObject(i).getString("name");
                                    eventname.add(event);
                                    String location = eventlist.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                    locationlist.add(location);
                                }
                                //Log.d(TAG, "size  "+ locationlist.size());
                                for(int i = 0; i < eventlist.length(); i++) {
                                    int minindex = i;
                                    String date = eventdate.get(i);
                                    for (int j = i+1; j < eventlist.length(); j++) {
                                        if (eventdate.get(j).compareTo(eventdate.get(minindex)) < 0){
                                            minindex = j;
                                        }
                                    }
                                    eventdate.set(i,eventdate.get(minindex));
                                    eventdate.set(minindex,date);
                                    String name = eventname.get(i);
                                    eventname.set(i,eventname.get(minindex));
                                    eventname.set(minindex, name);
                                    String location = locationlist.get(i);
                                    locationlist.set(i,locationlist.get(minindex));
                                    locationlist.set(minindex, location);
                                }

                                Intent sendlist = new Intent(getActivity(), SearchResultList.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("data",response.toString());
                                sendlist.putExtras(bundle);
                                startActivity(sendlist);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            queue.add(jsonObjectRequest);

    }

}