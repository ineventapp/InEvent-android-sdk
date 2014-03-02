package com.estudiotrilha.inevent.sdk.api;

import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;

import com.estudiotrilha.inevent.sdk.api.ApiResponseHandler;


public abstract class ApiRequest
{
    protected final String method;

    protected final List<NameValuePair> getParameters;
    protected final List<NameValuePair> postParameters;

    /** Set this to <b>false</b> to run the request synchronously */
    protected boolean            async        = true;
    /** Set this to <b>false</b> to run the request immediately, without passing through the queue */
    protected boolean            enqueue      = true;
    /** Set this to <b>true</b> to add the request in the queue, regardless of it having a similar pending request */
    protected boolean            forceRequest = false;
    protected ApiResponseHandler responseHandler;


    protected ApiRequest(String method, List<NameValuePair> getParameters, List<NameValuePair> postParameters)
    {
        this.method = method;
        this.getParameters = getParameters;
        this.postParameters = postParameters;
    }

    protected abstract String getNamespace();


    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ApiRequest)
        {
            ApiRequest r = (ApiRequest) o;

            return
                    this.getNamespace().equals(r.getNamespace()) &&
                    this.method.equals(r.method) &&
                    this.getParameters.equals(r.getParameters) &&
                    this.postParameters.equals(r.postParameters);
        }
        return false;
    }

    public int getRequestCode()
    {
        return toString().hashCode();
    }

    @Override
    public String toString()
    {
        return getNamespace()+"."+method +
                "\n\t"+"GET : "+Arrays.toString(getParameters.toArray()) +
                "\n\t"+"POST: "+Arrays.toString(postParameters.toArray());
    }


    /// Setters
    /**
     *  The request runs asynchronously by default, set this to <b>false</b>
     *  to run the request synchronously. 
     */
    public ApiRequest setAsync(boolean async)
    {
        this.async = async;
        return this;
    }
    /**
     *  The request is enqueued in the {@link ApiRequestManager} by default, set this
     *  to <b>false</b> to run the request immediately, without passing through
     *  the queue.
     */
    public ApiRequest setEnqueue(boolean enqueue)
    {
        this.enqueue = enqueue;
        return this;
    }
    /** 
     * If there is a similar request in the queue, this will be ignored by
     * default, set this to <b>true</b> to add the request in the queue,
     * regardless of the {@link ApiRequestManager} having a similar pending request
     * in the queue.
     */
    public ApiRequest setForceRequest(boolean force)
    {
        this.forceRequest = force;
        return this;
    }
    /** 
     * Sets the callback for the request.
     */
    public ApiRequest setResponseHandler(ApiResponseHandler handler)
    {
        this.responseHandler = handler;
        return this;
    }

    /// Getters
    public boolean isAsync()
    {
        return async;
    }
    public boolean isEnqueued()
    {
        return enqueue;
    }
    public boolean isForcingRequest()
    {
        return forceRequest;
    }
}
