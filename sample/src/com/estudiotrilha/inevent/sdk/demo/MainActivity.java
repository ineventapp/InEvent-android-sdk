package com.estudiotrilha.inevent.sdk.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
        {
            // Adds a simple list fragment to display the API request results
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new SampleListFragment())
                    .commit();
        }
    }
}
