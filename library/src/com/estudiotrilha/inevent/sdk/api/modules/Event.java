package com.estudiotrilha.inevent.sdk.api.modules;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.estudiotrilha.inevent.sdk.api.ApiRequest;
import com.estudiotrilha.inevent.sdk.utils.StringUtils;


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
        getParamenters.add(new BasicNameValuePair("tokenID", StringUtils.argumentNullCheck(tokenID)));

        // POST parameters
        postParamenters.add(new BasicNameValuePair("name", name));
        postParamenters.add(new BasicNameValuePair("nickname", nickname));

        return new Event("create", getParamenters, postParamenters);
    }

    public static ApiRequest getEvents(String name, String city, String theme, String dateBegin, String dateEnd, String order)
    {
        List<NameValuePair> getParamenters = new ArrayList<NameValuePair>();
        List<NameValuePair> postParamenters = new ArrayList<NameValuePair>();

        // GET parameters
        getParamenters.add(new BasicNameValuePair("name", StringUtils.nullGuard(name, "any")));
        getParamenters.add(new BasicNameValuePair("city", StringUtils.nullGuard(city, "any")));
        getParamenters.add(new BasicNameValuePair("theme", StringUtils.nullGuard(theme, "any")));
        getParamenters.add(new BasicNameValuePair("dateBegin", StringUtils.nullGuard(dateBegin, "any")));
        getParamenters.add(new BasicNameValuePair("dateEnd", StringUtils.nullGuard(dateEnd, "any")));
        getParamenters.add(new BasicNameValuePair("order", StringUtils.nullGuard(order, "any")));

        // POST parameters

        return new Event("getEvents", getParamenters, postParamenters);
    }
}
