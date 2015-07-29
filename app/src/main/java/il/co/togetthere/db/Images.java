package il.co.togetthere.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<Bitmap, Bitmap> urlsToThumbnailsAndBitmaps(List<String> imagesURLs) {
        Map<Bitmap, Bitmap> imagesThumbnailsAndBitmaps = new HashMap<>();
        for (String imageURL : imagesURLs){
            URL url = null;
            try {
                // Set URL
                url = new URL(imageURL);
                // Get Bitmap
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                // Get Thumbnail
                final int THUMBNAIL_SIZE = 256;
                Bitmap thumb = ThumbnailUtils.extractThumbnail(bmp, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
                // Rotate Bitmap if needed
                if (bmp.getWidth() > bmp.getHeight()){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bmp = Bitmap.createBitmap(bmp, 0, 0,
                            bmp.getWidth(), bmp.getHeight(),
                            matrix , true);
                }
                // Store
                imagesThumbnailsAndBitmaps.put(thumb, bmp);
            } catch (MalformedURLException e) {
                // Skip image
            } catch (IOException e) {
                // Skip image
            }
        }
        return imagesThumbnailsAndBitmaps;
    }
}