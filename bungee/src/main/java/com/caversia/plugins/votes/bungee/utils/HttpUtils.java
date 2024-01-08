package com.caversia.plugins.votes.bungee.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Provided utility functions related with http requests.
 * 
 * @author amartins
 */
public class HttpUtils {
    /**
     * Executes an HTTP POST request.
     * 
     * @param urlPath the URL to be invoked
     * @param body the body to be sent on the request
     * @return the received answer
     * @throws Exception when the request fails
     */
    public static String executePOST(String urlPath, String body) throws Exception {
        URL url;
        HttpsURLConnection connection = null;
        try {
            url = new URL(urlPath);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "minecraft");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(body.getBytes().length));

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
