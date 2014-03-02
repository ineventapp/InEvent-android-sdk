package com.estudiotrilha.inevent.sdk.api;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse
{
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
