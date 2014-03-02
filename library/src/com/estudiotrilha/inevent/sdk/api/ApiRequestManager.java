package com.estudiotrilha.inevent.sdk.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import android.os.Handler;
import android.util.Log;

import com.estudiotrilha.inevent.sdk.InEventSDK;
import com.estudiotrilha.inevent.sdk.utils.ConnectionUtils;
import com.estudiotrilha.inevent.sdk.utils.JsonUtils;


public class ApiRequestManager
{
    public static final String BASE_URL = "https://api.inevent.us/?method=";
    public static final String ENCODING = HTTP.UTF_8;


    private static ApiRequestManager sInstance;
    public static ApiRequestManager getInstance() {
        if (sInstance == null)
        {
            sInstance = new ApiRequestManager();
        }
        return sInstance;
    }

    private final Runnable mNextRequest = new Runnable() {
        public void run()
        {
            if (mRequestQueue.isEmpty())
            {
                mRunning = false;
                mOnApiRequestListener.onRequestQueueCompletion();
            }
            else
            {
                nextQueueRequest();
            }
        }
    };
    private OnApiRequestListener mOnApiRequestListener = new SimpleOnApiRequestListener();

    private LinkedList<ApiRequest> mRequestQueue;
    private HashSet<Integer>       mPendingRequests;
    private Handler                mUserThreadHandler;
    private boolean                mRunning;


    private ApiRequestManager()
    {
        mRequestQueue = new LinkedList<ApiRequest>();
        mPendingRequests = new HashSet<Integer>();
        mUserThreadHandler = new Handler();
        mRunning = false;
    }


    public void clearRequestQueue()
    {
        mRequestQueue.clear();
    }

    /**
     * Adds a new {@link ApiRequest} to the end of the queue
     */
    public void addRequest(ApiRequest request)
    {
        int requestCode = request.getRequestCode();
        if (!request.forceRequest)
        {
            // Check if this request should be cancelled,
            // if the listener returns false, proceed with the request
            if (mPendingRequests.contains(requestCode)
                    && mOnApiRequestListener.onCancel(request)) return;

            // Only not forceful requests appear in this list
            mPendingRequests.add(requestCode);
        }

        if (request.enqueue)
        {
            // Add the request in the list
            mRequestQueue.add(request);
    
            if (!mRunning)
            {
                mRunning = true;
                mOnApiRequestListener.onRequestQueueStart();
                nextQueueRequest();
            }
        }
        else
        {
            processRequest(request);
        }
    }

    private void nextQueueRequest()
    {
        // Get the next request
        final ApiRequest request = mRequestQueue.getFirst();
        mRequestQueue.removeFirst();

        processRequest(request);
    }


    private void processRequest(final ApiRequest request)
    {
        if (!mOnApiRequestListener.onPreExecute(request))
        {
            onRequestCompletion(request, null);
            return;
        }

        Runnable runnable = generateRunnableForRequest(request);

        if (request.async)
        {
            new Thread(runnable).start();
        }
        else
        {
            runnable.run();
        }
    }


    private Runnable generateRunnableForRequest(final ApiRequest request)
    {
        return new Runnable() {
            @Override
            public void run()
            {
                Log.v(InEventSDK.LOG_TAG, "Running Request: "+request);

                // Build up the connection
                final HttpURLConnection connection = buildConnection(request);
                // Build the response object
                ApiResponse response = new ApiResponse();

                try
                {
                    // Send the post parameters, if any
                    if (!request.postParameters.isEmpty())
                    {
                        // Format the post parameters
                        String post = URLEncodedUtils.format(request.postParameters, ENCODING);
                        // Send the post
                        OutputStream outputStream = connection.getOutputStream();
                        byte[] buffer = post.getBytes(ENCODING);
                        outputStream.write(buffer);
                        outputStream.flush();
                        outputStream.close();
                    }

                    // Creates the connection
                    connection.connect();
                    // Acquire the response code and message
                    response.responseCode = connection.getResponseCode();
                    response.responseMessage = connection.getResponseMessage();

                    // Get JSON from the connection
                    response.json = JsonUtils.inputStreamToJSON(
                            connection.getInputStream(),
                            ENCODING);

                    // Log the results
                    Log.v(InEventSDK.LOG_TAG, "Response: "+response);
                }
                catch (Exception e)
                {
                    response.responseCode = ConnectionUtils.checkConnectionException(e, response.responseCode);
                    Log.w(InEventSDK.LOG_TAG, "Couldn't properly process request "+ request, e);
                }
                finally
                {
                    if (connection != null) connection.disconnect();

                    // Callback
                    onRequestCompletion(request, response);
                }
            }
        };
    }


    private void onRequestCompletion(final ApiRequest request, final ApiResponse response)
    {
        if (!request.forceRequest) mPendingRequests.remove(request.getRequestCode());

        if (response != null)
        {
            if (request.async)
            {
                mUserThreadHandler.post(new Runnable() {
                    public void run()
                    {
                        if (request.responseHandler != null) request.responseHandler.handleResponse(request, response);
                        mOnApiRequestListener.onPostExecute(request, response);
                    }
                });
            }
            else
            {
                if (request.responseHandler != null) request.responseHandler.handleResponse(request, response);
                mOnApiRequestListener.onPostExecute(request, response);
            }
        }

        if (request.enqueue) mUserThreadHandler.post(mNextRequest);
    }

    private HttpURLConnection buildConnection(ApiRequest request)
    {
        String method = BASE_URL+request.getNamespace()+"."+request.method;

        // Append the GET parameters
        String parameters = URLEncodedUtils.format(request.getParameters, ENCODING);
        if (parameters.length() > 0)
        {
            parameters = "&"+parameters;
        }

        HttpURLConnection connection = null;
        try
        {
            connection = ConnectionUtils.getURLPostConnection(new URL(method + parameters));
        }
        catch (MalformedURLException e)
        {
            Log.e(InEventSDK.LOG_TAG, "Error forming URL.\n\t" +
            		"Method    : "+method+"\n\t" +
    				"Parameters: "+parameters, e);
        }
        catch (IOException e)
        {
            Log.e(InEventSDK.LOG_TAG, "Error creating connection for "+request, e);
        }
        return connection;
    }


    public void setOnApiRequestListener(OnApiRequestListener l)
    {
        if (l == null) l = new SimpleOnApiRequestListener();
        mOnApiRequestListener = l;
    }
}
