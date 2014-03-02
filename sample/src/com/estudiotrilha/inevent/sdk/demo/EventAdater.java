package com.estudiotrilha.inevent.sdk.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventAdater extends BaseAdapter
{
    private JSONArray      mJsonArray;
    private int            mSize;
    private LayoutInflater mInflater;


    public EventAdater(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mSize = 0;
    }

    public void setData(JSONArray json)
    {
        this.mJsonArray = json;
        this.mSize = (json == null) ? 0 : json.length();
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mSize;
    }

    @Override
    public Object getItem(int position)
    {
        try
        {
            return mJsonArray.getJSONObject(position);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        JSONObject json = (JSONObject) getItem(position);
        return json.optLong("id", -1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            // Inflate new view
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Fill the view with information
        JSONObject json = (JSONObject) getItem(position);
        String info = Html.fromHtml(json.optString("name")).toString();
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(info);

        return convertView;
    }
}
