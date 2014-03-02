package com.estudiotrilha.inevent.sdk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import com.estudiotrilha.inevent.sdk.InEventSDK;

import android.util.Log;


public class JsonUtils
{
    public static int BUFFER_SIZE = 1024;   // the default is one kilobyte


    /**
     * Generates a hash code for JSON using the SHA-256 algorithm
     */
    public static String generateHash(JSONObject json)
    {
        final String algorithm = "SHA-256";

        try
        {
            // Generate hash
            MessageDigest digester = MessageDigest.getInstance(algorithm);
            digester.reset();
            byte[] digest = digester.digest(json.toString().getBytes());

            // Get the string from it
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < digest.length; i++)
            {
                // From the first nibble from the first byte
                builder.append(Character.forDigit((digest[i] & 0xf0) >> 4, 16));
                // Second piece of the byte (second nibble) from the second byte
                builder.append(Character.forDigit(digest[i] & 0x0f, 16));
            }
            return builder.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e(InEventSDK.LOG_TAG, "Error retrieving algorithm for "+algorithm, e);
        }

        // Return the own JSON if something wen wrong
        return json.toString();
    }

    public static JSONObject inputStreamToJSON(InputStream in, String encoding) throws IOException, JSONException
    {
        return inputStreamToJSON(in, encoding, BUFFER_SIZE);
    }
    public static JSONObject inputStreamToJSON(InputStream in, String encoding, int bufferSize) throws IOException, JSONException
    {
        JSONObject result = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding), bufferSize);

        StringBuilder sb = new StringBuilder();
        String line = null;
        String json = null;

        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }
        json = sb.toString();
        Log.v(InEventSDK.LOG_TAG, "Request response: "+json);

        in.close();

        result = new JSONObject(json);

        return result;
    }

    public static JSONObject getJsonFromConnection(URLConnection connection, String encoding) throws IOException, JSONException
    {
        // create the connection
        connection.connect();

        // get the data input stream
        InputStream in = connection.getInputStream();
        JSONObject json = inputStreamToJSON(in, encoding);

        return json;
    }
}
