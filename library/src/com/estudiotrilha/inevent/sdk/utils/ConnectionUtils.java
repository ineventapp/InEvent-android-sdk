package com.estudiotrilha.inevent.sdk.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;


public class ConnectionUtils
{
    public static class ConnectionSettings
    {
        public static final int DEFAULT_READ_TIME_OUT    = 10000;
        public static final int DEFAULT_CONNECT_TIME_OUT = 15000;


        public int readTimeOut    = DEFAULT_READ_TIME_OUT;
        public int connectTimeOut = DEFAULT_CONNECT_TIME_OUT;

        public ConnectionSettings() {}
        public ConnectionSettings(int readTimeOut, int connectTimeOut)
        {
            this.readTimeOut = readTimeOut;
            this.connectTimeOut = connectTimeOut;
        }
    }

    private static ConnectionSettings sSettings = new ConnectionSettings();


    public static void setConnectionSettings(ConnectionSettings settings)
    {
        if (settings == null) settings = new ConnectionSettings();
        sSettings = settings;
    }

    public static int checkConnectionException(Exception e, int responseCode)
    {
        final String authError1 = "No authentication challenges found";
        final String authError2 = "Received authentication challenge is null";

        if (e instanceof SocketTimeoutException)    return HttpStatus.SC_REQUEST_TIMEOUT;
        else if (authError1.equals(e.getMessage())) return HttpStatus.SC_UNAUTHORIZED;
        else if (authError2.equals(e.getMessage())) return HttpStatus.SC_UNAUTHORIZED;
        else                                        return responseCode;
    }

    public static HttpURLConnection getURLGetConnection(URL url) throws IOException
    {
        return getURLConnection(url, HttpGet.METHOD_NAME);
    }
    public static HttpURLConnection getURLPostConnection(URL url) throws IOException
    {
        return getURLConnection(url, HttpPost.METHOD_NAME);
    }

    protected static HttpURLConnection getURLConnection(URL url, String methodName) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(sSettings.connectTimeOut);
        connection.setReadTimeout(sSettings.readTimeOut);
        connection.setRequestMethod(methodName);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        return connection;
    }
}
