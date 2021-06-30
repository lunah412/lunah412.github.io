package com.example.searchevent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetail extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter2 adapter;
    String artisturl;
    String id, eventname, venue, date, category;
    String artistname;
    MenuItem likebtn;
    Boolean present=false;

    //setup app bar button:twitter and like
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.button, menu);
        likebtn = menu.findItem(R.id.like);
        //preload favlist
        List<Favourite> fav_list = SetFravourite.readlist(EventDetail.this);
        for(int i = 0; i < fav_list.size();i++){
            if(id.equals(fav_list.get(i).getID())){
                likebtn.setIcon(R.drawable.heart_fill_white);
                present = true;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.like:
                setlike();
                return true;
            case R.id.twitter:
                postTwitter();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void setlike() {
        List<Favourite> old_list = SetFravourite.readlist(EventDetail.this);
        if(present){
            //write to list
            List<Favourite> new_list = new ArrayList<Favourite>();
            for(int i = 0; i < old_list.size(); i++){
                if(id.equals(old_list.get(i).getID())){
                    ;
                }else{
                    new_list.add(old_list.get(i));
                }
            }
            SetFravourite.writeList(EventDetail.this, new_list);
            present = false;
            likebtn.setIcon(R.drawable.heart_outline_white);
        }else{
            Favourite newitem = new Favourite(id, eventname, venue, date, category, artistname);
            old_list.add(newitem);
            SetFravourite.writeList(EventDetail.this, old_list);
            present = true;
            likebtn.setIcon(R.drawable.heart_fill_white);
        }
    }

    private void postTwitter() {
        String twitterlink = "https://twitter.com/intent/tweet?text=Check%20out%20" +eventname+ "%20at%20" +venue+"%2E%23CSCI571EventSearch";
        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        intent1.setData(Uri.parse(twitterlink));
        startActivity(intent1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        artisturl = "https://hw8-316909.wl.r.appspot.com/artistinfo?artist=";
        //get data from previous activity
        id = intent.getStringExtra("data");
        eventname = intent.getStringExtra("name");
        artistname = intent.getStringExtra("artist");
        venue = intent.getStringExtra("location");
        date = intent.getStringExtra("date");
        category = intent.getStringExtra("category");

        Log.v("Send Categoty is ", category);
        //set bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(eventname);

        tabLayout = findViewById(R.id.tabs2);
        pager2 = findViewById(R.id.view_pager2);
        FragmentManager fm = getSupportFragmentManager();

        adapter = new FragmentAdapter2(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("EVENTS"));
        tabLayout.addTab(tabLayout.newTab().setText("ARTIST"));
        tabLayout.addTab(tabLayout.newTab().setText("VENUE"));
        tabLayout.getTabAt(0).setIcon(R.drawable.info_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.artist);
        tabLayout.getTabAt(2).setIcon(R.drawable.venue);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    public String getID() {
        return id;
    }
    public String getArtist(){
        return artistname;
    }


}