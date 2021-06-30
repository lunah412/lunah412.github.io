package com.example.searchevent;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {
    public void notifySuccess( JSONObject response);
    public void notifyError( VolleyError error);
}
