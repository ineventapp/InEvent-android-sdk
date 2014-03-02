package com.estudiotrilha.inevent.sdk.api.modules;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.estudiotrilha.inevent.sdk.api.ApiRequest;


public class Event extends ApiRequest
{
    protected Event(String method, List<NameValuePair> getParameters, List<NameValuePair> postParameters)
    {
        super(method, getParameters, postParameters);
    }

    @Override
    protected String getNamespace()
    {
        return "event";
    }

    public static ApiRequest create(String tokenID, String name, String nickname)
    {
        List<NameValuePair> getParamenters = new ArrayList<NameValuePair>();
        List<NameValuePair> postParamenters = new ArrayList<NameValuePair>();

        // GET parameters
        getParamenters.add(new BasicNameValuePair("tokenID", tokenID));

        // POST parameters
        postParamenters.add(new BasicNameValuePair("name", name));
        postParamenters.add(new BasicNameValuePair("nickname", nickname));

        return new Event("create", getParamenters, postParamenters);
    }
}
