package il.co.togetthere.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HTTPHandler {
    // http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

    private static final String USER_AGENT = "Mozilla/5.0";

    // HTTP POST request
    public static String postRequest(String url, String jsonRequest) throws IOException {

        // Create HTTP client
        HttpClient httpClient = new DefaultHttpClient();

        // Encode URL
        url = encodeURL(url);

        // Create POST request
        HttpPost request = new HttpPost(url);
        request.setHeader("User-Agent", USER_AGENT);
        request.addHeader("content-type", "application/json");
        StringEntity params = new StringEntity(jsonRequest, HTTP.UTF_8);
        request.setEntity(params);

        // Execute request
        HttpResponse response = httpClient.execute(request);

        // Log
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + request.getEntity());
        //System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        // Handle Response
        // Convert response to JSON
        // String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String jsonRsponse = result.toString();

        // Check for errors
        if (response.getStatusLine().getStatusCode() != 200){
            throw new IOException("ERROR(" + response.getStatusLine().getStatusCode() + ")" + jsonRsponse);
        }

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();

        // Return JSON
        return jsonRsponse;
    }

    // HTTP GET request
    public static String getRequest(String url) throws IOException {

        // Create HTTP client
        HttpClient httpClient = new DefaultHttpClient();

        // Encode URL
        url = encodeURL(url);

        // Create GET request
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);

        // Execute request
        HttpResponse response = httpClient.execute(request);

        // Log
        // System.out.println("\nSending 'GET' request to URL : " + url);
        // System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        // Handle Response
        // Convert response to JSON
        // String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String json = result.toString();

        // Check for errors
        if (response.getStatusLine().getStatusCode() != 200){
            throw new IOException("ERROR(" + response.getStatusLine().getStatusCode() + ")" + json);
        }

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();

        // Return JSON
        return json;
    }

    private static String encodeURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            urlStr = url.toString();
        } catch (MalformedURLException e) {
            // Bad URL Encoding, return original string
        } catch (URISyntaxException e) {
            // Bad URI Encoding, return original string
        }
        return urlStr;
    }
}
