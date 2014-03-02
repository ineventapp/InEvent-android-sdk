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
    public boolean            async        = true;
    /** Set this to <b>false</b> to run the request immediately, without passing through the queue */
    public boolean            enqueue      = true;
    /** Set this to <b>true</b> to add the request in the queue, regardless of it having a similar pending request */
    public boolean            forceRequest = false;
    public ApiResponseHandler responseHandler;


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
}
