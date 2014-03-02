package com.estudiotrilha.inevent.sdk.demo;

import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.estudiotrilha.inevent.sdk.api.ApiRequest;
import com.estudiotrilha.inevent.sdk.api.ApiRequestManager;
import com.estudiotrilha.inevent.sdk.api.ApiResponse;
import com.estudiotrilha.inevent.sdk.api.ApiResponseHandler;
import com.estudiotrilha.inevent.sdk.api.modules.Event;


public class SampleListFragment extends ListFragment
{
    private static final String STATE_DATA = "state.DATA";

    private ApiResponse mApiResponse;
    private EventAdater mAdater;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // Create the adapter
        mAdater = new EventAdater(getActivity());
        setListAdapter(mAdater);
        // Show loading state
        setListShown(false);

        setHasOptionsMenu(true);

        if (savedInstanceState != null)
        {
            // Try to recover data from previous app state
            mApiResponse = (ApiResponse) savedInstanceState.getSerializable(STATE_DATA);
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        setListShown(false);
        if (mApiResponse == null)
        {
            // There's no data stored, download some
            downloadInfo();
        }
        else
        {
            // Show previously downloaded data
            deliverResults(mApiResponse);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_DATA, mApiResponse);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_refresh)
        {
            // Redownload data
            downloadInfo();
        }

        return super.onOptionsItemSelected(item);
    }


    private void downloadInfo()
    {
        // Create the request
        ApiRequest request = Event.getEvents(null, null, null, null, null, null).setResponseHandler(new ApiResponseHandler() {
            @Override
            public void handleResponse(ApiRequest request, ApiResponse response)
            {
                // Put the results in the list
                deliverResults(response);
            }
        });

        // Retrieve the reference to the request manager
        ApiRequestManager requestManager = ApiRequestManager.getInstance();
        // Start the request
        requestManager.newRequest(request);
    }

    private void deliverResults(ApiResponse response)
    {
        // Store the data
        mApiResponse = response;
        try
        {
            if (response != null && response.json != null)
            {
                // Pass the data to the adapter and display the content
                mAdater.setData(response.json.getJSONArray("data"));
                setListShown(true);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
