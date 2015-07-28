package il.co.togetthere.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Images {

    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
    public List<String> getURLs(){
        List<String> urls = new ArrayList<>();
        for (Result result : responseData.getResults()){
            urls.add(result.getUrl());
        }
        return urls;
    }

    public static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }

    public static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }

    public static List<Bitmap> urlsToBitmaps(List<String> imagesURLs){
        List<Bitmap> imagesBitmaps = new ArrayList<>();
        for (String imageURL : imagesURLs){
            URL url = null;
            try {
                url = new URL(imageURL);
                final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                imagesBitmaps.add(bmp);
            } catch (MalformedURLException e) {
                // TODO
                e.printStackTrace();
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
        }
        return imagesBitmaps;
    }
}