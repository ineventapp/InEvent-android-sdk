package com.estudiotrilha.inevent.sdk.api;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;


public class ApiResponse implements Serializable
{
    private static final long serialVersionUID = 5122827101036632617L;

    public int        responseCode;
    public String     responseMessage;
    public JSONObject json;


    @Override
    public String toString()
    {
        String jsonString = null;
        try
        {
            jsonString = json.toString(2);
        }
        catch (JSONException e) {}

        return "Code "+ responseCode +
                ", Message: \""+ responseMessage +
        		"\"\nJSON = "+jsonString ;
    }
}
