package il.co.togetthere.server;

import java.util.List;

import il.co.togetthere.db.ServiceProvider;
import il.co.togetthere.db.ServiceProviderCategory;
import il.co.togetthere.db.Task;

public class AsyncResult {

    private ServiceProviderCategory resultCategory;
    private List<ServiceProvider> serviceProviderList;
    private List<Task> taskList;

    private boolean error = false;

    public ServiceProviderCategory getResultCategory() {
        return resultCategory;
    }

    public void setResultCategory(ServiceProviderCategory resultCategory) {
        this.resultCategory = resultCategory;
    }

    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
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
}