package com.estudiotrilha.inevent.sdk.api;


public interface OnApiRequestListener
{
    /**
     * Called when there is another similar request in the queue.
     * @return <b>true</b> to cancel the request, false to proceed with it.
     */
    boolean onCancel(ApiRequest request);
    /**
     * @return <b>true</b> to proceed with the request, false to stop it from being send.
     */
    boolean onPreExecute(ApiRequest request);
    /**
     * @param response the response of the request. 
     * @return <b>true</b> to proceed with the requests, false to clear the queue.
     */
    boolean onPostExecute(ApiRequest request, ApiResponse response);

    /**
     * Called right before the first request in the queue is processed
     */
    void onRequestQueueStart();
    /**
     * Called right after all the requests in the queue have been completed
     */
    void onRequestQueueCompletion();
}
