package com.example.searchevent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String id, url, name, address, city, phone, hour, generalR, childR;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public MapView mMapView;

    public VenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_venue, container, false);

        EventDetail detail = (EventDetail) getActivity();
        id = detail.getID();

        mMapView = rootView.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        //setup url
        url =  "https://hw8-316909.wl.r.appspot.com/detailinfo?id=" + id;
        List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
        //request event detail json
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("URL is  ", url);
                try {
                    Map<String, Object> showitem = new HashMap<String, Object>();
                    Map<String, Object> showitem2 = new HashMap<String, Object>();
                    Map<String, Object> showitem3 = new HashMap<String, Object>();
                    Map<String, Object> showitem4 = new HashMap<String, Object>();
                    Map<String, Object> showitem5 = new HashMap<String, Object>();
                    Map<String, Object> showitem6 = new HashMap<String, Object>();
                    Map<String, Object> showitem7 = new HashMap<String, Object>();

                    if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("name")){
                        name = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                        showitem.put("subtitle","Name");
                        showitem.put("data", name);
                        listitem.add(showitem);
                    }
                    if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("address")){
                        address = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");
                        showitem2.put("subtitle","Address");
                        showitem2.put("data", address);
                        listitem.add(showitem2);
                    }
                    if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("city")){
                        city = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name")
                        + ", " +response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("name");
                        showitem3.put("subtitle", "City");
                        showitem3.put("data", city);
                        listitem.add(showitem3);
                    }

                   if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("boxOfficeInfo")){
                       if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").has("phoneNumberDetail")){
                           phone = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
                           showitem4.put("subtitle", "Phone Number");
                           showitem4.put("data", phone);
                           listitem.add(showitem4);
                       }
                       if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").has("openHoursDetail")){
                           hour = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("boxOfficeInfo").getString("openHoursDetail");
                           showitem5.put("subtitle","Open Hours");
                           showitem5.put("data", hour);
                           listitem.add(showitem5);
                       }
                    }
                    if (response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).has("generalInfo")) {
                        if (response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").has("generalRule")) {
                            generalR = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("generalRule");
                            showitem6.put("subtitle", "General Rule");
                            showitem6.put("data", generalR);
                            listitem.add(showitem6);
                        }
                        if(response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").has("childRule")){
                            childR = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("generalInfo").getString("childRule");
                            showitem7.put("subtitle","Child Rule");
                            showitem7.put("data", childR);
                            listitem.add(showitem7);
                        }
                    }

                    SimpleAdapter adapter = new SimpleAdapter(rootView.getContext(), listitem, R.layout.artistitem
                            , new String[] {"subtitle","data"}, new int[]{R.id.subtitle, R.id.artistinfo});
                    ListView listView = (ListView) rootView.findViewById(R.id.venue_list);
                    listView.setDivider(null);
                    listView.setAdapter(adapter);


                    //MapView map = rootView.findViewById(R.id.mapView);
                    //map.

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
        return rootView;
    }
    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap ) {
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}