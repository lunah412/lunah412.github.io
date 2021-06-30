package com.example.searchevent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.MediaRouteActionProvider;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.searchevent.ui.main.Eventlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultList extends AppCompatActivity {
    private static final String TAG = SearchResultList.class.getSimpleName();
    ArrayList<String> eventname = new ArrayList<String>();
    ArrayList<String> eventdate = new ArrayList<String>();
    ArrayList<String> locationlist = new ArrayList<String>();
    ArrayList<String> categorylist =  new ArrayList<String>();
    ArrayList<Integer> imagelist = new ArrayList<Integer>();
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> artistlist = new ArrayList<String>();
    private View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result");
        Intent intent = getIntent();

        String data = intent.getStringExtra("data");

        JSONArray eventlist = null;
        try {
            JSONObject response = new JSONObject(data);
            eventlist = response.getJSONObject("_embedded").getJSONArray("events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < eventlist.length(); i++){
            String date = null;
            String event = null;
            String location = null;
            String category = null;
            String id = null;
            String artist = null;
            try {
                date = eventlist.getJSONObject(i).getJSONObject("dates").getJSONObject("start").getString("localDate");
                event = eventlist.getJSONObject(i).getString("name");
                location = eventlist.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                category = eventlist.getJSONObject(i).getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("id");
                id = eventlist.getJSONObject(i).getString("id");
                artist = eventlist.getJSONObject(i).getJSONObject("_embedded").getJSONArray("attractions").getJSONObject(0).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            idlist.add(id);
            eventdate.add(date);
            eventname.add(event);
            locationlist.add(location);
            categorylist.add(category);
            artistlist.add(artist);
        }

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
            String cate = categorylist.get(i);
            categorylist.set(i, categorylist. get(minindex));
            categorylist.set(minindex, cate);
            String id = idlist.get(i);
            idlist.set(i, idlist.get(minindex));
            idlist.set(minindex, id);
            String artistname = artistlist.get(i);
            artistlist.set(i, artistlist.get(minindex));
            artistlist.set(minindex, artistname);
        }
        for(int i = 0; i < categorylist.size();i++){
            if(categorylist.get(i).equals("KZFzniwnSyZfZ7v7nJ")){
                imagelist.add(R.drawable.music_icon);
            }
            if(categorylist.get(i).equals("KZFzniwnSyZfZ7v7nE")){
                imagelist.add(R.drawable.ic_sport_icon);
            }
            if(categorylist.get(i).equals("KZFzniwnSyZfZ7v7nn")){
                imagelist.add(R.drawable.film_icon);
            }
            if(categorylist.get(i).equals("KZFzniwnSyZfZ7v7n1")){
                imagelist.add(R.drawable.miscellaneous_icon);
            }
            if(categorylist.get(i).equals("KZFzniwnSyZfZ7v7na")){
                imagelist.add(R.drawable.art_icon);
            }
        }
        //setup tabledata
        List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
        for(int i = 0; i < eventlist.length();i++){
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("image", imagelist.get(i));
            String name = "";
            if(eventname.get(i).length()>35){
                name = eventname.get(i).substring(0,35) + "...";
            }else{
                name = eventname.get(i);
            }
            showitem.put("event",name);
            showitem.put("venue",locationlist.get(i));
            showitem.put("date",eventdate.get(i));
            List<Favourite> fav_list = SetFravourite.readlist(SearchResultList.this);
            String id = idlist.get(i);
            showitem.put("heart",R.drawable.heart_outline_black);
            for(int j = 0; j < fav_list.size();j++){
                if(id.equals(fav_list.get(j).getID())){
                    showitem.put("heart",R.drawable.heart_fill_red);
                    //Log.v("RED",  id);
                }
            }
            listitem.add(showitem);
        }
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), listitem, R.layout.search_item, new String[] {"image","event","venue","date","heart"}, new int[]{R.id.image, R.id.event, R.id.venue, R.id.date, R.id.heart});
        ListView listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(adapter);

        Intent intent1 = new Intent(this,  EventDetail.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("name", ""+eventname.get(i));
                bundle.putString("data", ((String) ""+ idlist.get(i)));
                bundle.putString("artist", artistlist.get(i));
                bundle.putString("location", locationlist.get(i));
                bundle.putString("date", eventdate.get(i));
                bundle.putString("category", categorylist.get(i));
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }


}