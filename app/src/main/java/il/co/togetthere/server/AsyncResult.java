package il.co.togetthere.server;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

import il.co.togetthere.db.Review;
import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.Task;

public class AsyncResult {

    private ServiceProviderCategory resultCategory;
    private List<ServiceProvider> serviceProviderList;
    private List<Review> reviewsList;
    private ServiceProvider serviceProvider;
    private List<Task> taskList;
    List<String> imagesURLs;
    List<Bitmap> imagesBitmaps;

    private boolean error = false;
    private String message = "";
    private int statusCode = 200;

    public ServiceProviderCategory getResultCategory() {
        return resultCategory;
    }

    public void setResultCategory(ServiceProviderCategory resultCategory) {
        this.resultCategory = resultCategory;
    }
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }

    public List<Review> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public boolean errored() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<String> getImagesURLs() {
        return imagesURLs;
    }

    public void setImagesURLs(List<String> imagesURLs) {
        this.imagesURLs = imagesURLs;
    }

    public List<Bitmap> getImagesBitmaps() {
        return imagesBitmaps;
    }

    public void setImagesBitmaps(List<Bitmap> imagesBitmaps) {
        this.imagesBitmaps = imagesBitmaps;
    }

    public void catchException(IOException e){
        this.setError(true);
        this.setMessage(e.getMessage().substring(e.getMessage().indexOf(")") + 1));
        this.setStatusCode(Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("(") + 1, e.getMessage().indexOf(")"))));
    }
}