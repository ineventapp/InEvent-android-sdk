package com.estudiotrilha.inevent.sdk.api;


public class SimpleOnApiRequestListener implements OnApiRequestListener
{
    @Override public boolean onCancel(ApiRequest request) { return true; }
    @Override public boolean onPreExecute(ApiRequest request) { return true; }
    @Override public boolean onPostExecute(ApiRequest request, ApiResponse response) { return true; }

    @Override public void onRequestQueueStart() {}
    @Override public void onRequestQueueCompletion() {}
}
