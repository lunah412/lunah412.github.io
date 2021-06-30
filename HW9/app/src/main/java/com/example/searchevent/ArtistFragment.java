package com.example.searchevent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String artistname, follower, popularity, checkurl;
    String requesturl;
    String url = "https://hw8-316909.wl.r.appspot.com/artistinfo?artist=";

    public ArtistFragment() {
        // Required empty public constructor
    }
/*
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        EventDetail eventdetail = (EventDetail)getActivity();
        artistname = eventdetail.getArtist();
        String[] checkquote = artistname.split("\\(");
        Log.d("SPLIT RESULT  ", checkquote.toString());
        String modifiedartist = "";
        for(int i = 0; i < checkquote.length; i++){
                modifiedartist += checkquote[i];
        }

        requesturl = url + modifiedartist;
        Log.d("Spotify url   " ,requesturl);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requesturl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
                try {
                    if(response.has("items")){
                        String artist = response.getJSONArray("items").getJSONObject(0).getString("name");
                        popularity = response.getJSONArray("items").getJSONObject(0).getString("popularity");
                        follower = response.getJSONArray("items").getJSONObject(0).getJSONObject("followers").getString("total");
                        checkurl = response.getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify");
                        Log.d("Get data   ", artist);
                        Map<String, Object> showitem = new HashMap<String, Object>();
                        showitem.put("subtitle", "Name");
                        showitem.put("info", artist);
                        listitem.add(showitem);
                        Map<String, Object> showitem2 = new HashMap<String, Object>();
                        showitem2.put("subtitle", "Followers");
                        showitem2.put("info", follower);
                        listitem.add(showitem2);
                        Map<String, Object> showitem3 = new HashMap<String, Object>();
                        showitem3.put("subtitle", "Popularity");
                        showitem3.put("info", popularity);
                        listitem.add(showitem3);
                        TextView subt = rootView.findViewById(R.id.check);
                        subt.setText("Check At");
                        TextView tv_link = rootView.findViewById(R.id.spotify);
                        String spotifyurl = "<a href=\"" + checkurl + "\">Spotify</a>";
                        tv_link.setText(Html.fromHtml(spotifyurl));
                        tv_link.setMovementMethod(LinkMovementMethod.getInstance());
                        SimpleAdapter adapter = new SimpleAdapter(rootView.getContext(), listitem, R.layout.artistitem, new String[] {"subtitle","info"}, new int[]{R.id.subtitle, R.id.artistinfo});
                        ListView listView = (ListView) rootView.findViewById(R.id.artist_list);
                        listView.setDivider(null);
                        listView.setAdapter(adapter);
                    }else{
                        TextView subt = rootView.findViewById(R.id.check);
                        subt.setText(artistname + "No details");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView subt = rootView.findViewById(R.id.check);
                subt.setText("No Record");
            }
        });
        queue.add(jsonObjectRequest);
        return rootView;
    }

}