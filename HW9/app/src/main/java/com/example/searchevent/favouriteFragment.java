package com.example.searchevent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favouriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Favourite> favouriteList;

    public favouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favouriteFragment newInstance(String param1, String param2) {
        favouriteFragment fragment = new favouriteFragment();
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
        favouriteList = SetFravourite.readlist(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);



        if(favouriteList.size() == 0){
            TextView noresult = rootView.findViewById(R.id.nofavourite);
            noresult.setText("No favourite");
        }else{
            List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
            for(int i = 0; i < favouriteList.size();i++){
                Map<String, Object> showitem = new HashMap<String, Object>();

                Log.d("Favourite category  ", favouriteList.get(i).getCategory());
                String category = favouriteList.get(i).getCategory();
                if(category.equals("KZFzniwnSyZfZ7v7nJ")){
                    showitem.put("image", R.drawable.music_icon);
                }
                if(category.equals("KZFzniwnSyZfZ7v7nE")){
                    showitem.put("image", R.drawable.ic_sport_icon);
                }
                if(category.equals("KZFzniwnSyZfZ7v7nn")){
                    showitem.put("image", R.drawable.film_icon);
                }
                if(category.equals("KZFzniwnSyZfZ7v7n1")){
                    showitem.put("image", R.drawable.miscellaneous_icon);
                }
                if(category.equals("KZFzniwnSyZfZ7v7na")){
                    showitem.put("image", R.drawable.art_icon);
                }
                String name = "";
                if(favouriteList.get(i).getEvent().length()>35){
                    name = favouriteList.get(i).getEvent().substring(0,35) + "...";
                }else{
                    name = favouriteList.get(i).getEvent();
                }
                showitem.put("event", name);
                showitem.put("venue", favouriteList.get(i).getVenue());
                showitem.put("date", favouriteList.get(i).getDate());
                showitem.put("heart", R.drawable.heart_fill_red);
                listitem.add(showitem);
            }
            SimpleAdapter adapter = new SimpleAdapter(rootView.getContext(), listitem, R.layout.search_item, new String[] {"image","event","venue","date","heart"}, new int[]{R.id.image, R.id.event, R.id.venue, R.id.date, R.id.heart});
            ListView listView = (ListView) rootView.findViewById(R.id.favourite_list);
            listView.setAdapter(adapter);

            Intent intent1 = new Intent(getContext(),  EventDetail.class);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", ""+favouriteList.get(i).getEvent());
                    bundle.putString("data", ((String) ""+ favouriteList.get(i).getID()));
                    bundle.putString("artist", favouriteList.get(i).getArtist());
                    bundle.putString("location", favouriteList.get(i).getVenue());
                    bundle.putString("date", favouriteList.get(i).getDate());
                    bundle.putString("category", favouriteList.get(i).getCategory());
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            });
        }
        return rootView;
    }
}