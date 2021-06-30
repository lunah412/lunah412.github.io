package com.example.searchevent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    String id;
    String url;
    String name, venue, date, category, price, status, ticketurl, seatmap;
    SimpleAdapter adapter;
    FragmentManager fm;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = MainActivity.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();

    public DetailFragment() {
        // Required empty public constructor
    }
/*
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.

    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        EventDetail detail = (EventDetail) getActivity();
        id = detail.getID();

        //setup url
        url =  "https://hw8-316909.wl.r.appspot.com/detailinfo?id=" + id;
        //request event detail json
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, url);
                try {
                    Map<String, Object> showitem = new HashMap<String, Object>();
                    Map<String, Object> showitem2 = new HashMap<String, Object>();
                    Map<String, Object> showitem3 = new HashMap<String, Object>();
                    Map<String, Object> showitem4 = new HashMap<String, Object>();
                    Map<String, Object> showitem5 = new HashMap<String, Object>();
                    Map<String, Object> showitem6 = new HashMap<String, Object>();
                    Map<String, Object> showitem7 = new HashMap<String, Object>();
                    Map<String, Object> showitem8 = new HashMap<String, Object>();
                    name = response.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(0).getString("name");
                    for(int i = 1; i <response.getJSONObject("_embedded").getJSONArray("attractions").length();i++){
                        name = name + " | " +response.getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(i).getString("name");
                    }
                    showitem.put("subtitle","Artist(s)/Teams");
                    showitem.put("data",name);
                    listitem.add(showitem);
                    if(response.getJSONObject("_embedded").getJSONArray("venues")!= null){
                        venue = response.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                        showitem2.put("subtitle","Venue");
                        showitem2.put("data",venue);
                        listitem.add(showitem2);
                    }
                    if(response.getJSONObject("dates") != null){
                        date = response.getJSONObject("dates").getJSONObject("start").getString("localDate");
                        showitem3.put("subtitle", "Date");
                        showitem3.put("data",date);
                        listitem.add(showitem3);
                    }
                    category = "";
                    if(response.getJSONArray("classifications").getJSONObject(0).getString("subGenre") != null){
                        category = category + response.getJSONArray("classifications").getJSONObject(0).getJSONObject("subGenre").getString("name");
                        if(response.getJSONArray("classifications").getJSONObject(0).getString("genre") != null){
                            category = category +" | "+ response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                        }
                        if(response.getJSONArray("classifications").getJSONObject(0).getString("segment")!= null){
                            category = category + " | " + response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                        }
                    }else{
                        if(response.getJSONArray("classifications").getJSONObject(0).getString("genre") != null){
                            category = category +" | "+ response.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
                        }else{
                        if(response.getJSONArray("classifications").getJSONObject(0).getString("segment")!= null){
                            category = category + response.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                        }}
                    }
                    if(category.length() != 0){
                        showitem4.put("subtitle", "Category");
                        showitem4.put("data",category);
                        listitem.add(showitem4);
                    }
                    if(response.getJSONArray("priceRanges")!=null){
                        price = response.getJSONArray("priceRanges").getJSONObject(0).getString("min") + " - "
                                + response.getJSONArray("priceRanges").getJSONObject(0).getString("max") + " USD";
                        showitem5.put("subtitle","Price Range");
                        showitem5.put("data", price);
                        listitem.add(showitem5);
                    }
                    if(response.getJSONObject("dates").getJSONObject("status").getString("code")!=null){
                        status = response.getJSONObject("dates").getJSONObject("status").getString("code");
                        showitem6.put("subtitle","Ticket Status");
                        showitem6.put("data", status);
                        listitem.add(showitem6);
                    }
                    adapter = new SimpleAdapter(rootView.getContext(), listitem, R.layout.artistitem
                            , new String[] {"subtitle","data"}, new int[]{R.id.subtitle, R.id.artistinfo});
                    ListView listView = (ListView) rootView.findViewById(R.id.detail_list);
                    listView.setDivider(null);
                    listView.setAdapter(adapter);
                    if(response.has("url")){
                        ticketurl = response.getString("url");
                        TextView checkouttitle = rootView.findViewById(R.id.checkout);
                        checkouttitle.setText("Buy Ticket At");
                        TextView tickekLink = rootView.findViewById(R.id.ticketurl);
                        String ticket = "<a href=\"" + ticketurl + "\">TicketMaster</a>";
                        tickekLink.setText(Html.fromHtml(ticket));
                        tickekLink.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    if(response.has("seatmap")){
                        seatmap = response.getJSONObject("seatmap").getString("staticUrl");
                        TextView seatmaptitle = rootView.findViewById(R.id.view_seat);
                        seatmaptitle.setText("Seat Map");
                        TextView seatLink = rootView.findViewById(R.id.seat_map);
                        String seathtml = "<a href=\"" + seatmap + "\">View Seat Map Here</a>";
                        seatLink.setText(Html.fromHtml(seathtml));
                        seatLink.setMovementMethod(LinkMovementMethod.getInstance());
                    }
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


}