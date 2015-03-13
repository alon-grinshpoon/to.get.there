package il.co.togetthere.db;
/*
 * Copyright 2010-2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */



import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;

import il.co.togetthere.LoginActivity;
import il.co.togetthere.Task;
import il.co.togetthere.TypeChooserActivity;
import il.co.togetthere.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DynamoDBManager {

    private static final String TAG = "DynamoDBManager";

   
    /*
     * add service provider
     */
    public static void addObjectToDB(Object obj) {
        AmazonDynamoDBClient ddb = TypeChooserActivity.clientManager
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        try {
                Log.d(TAG, "Insert new Object to DB");
                mapper.save(obj);
                Log.d(TAG, "Object inserted");
            
        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting your object");
            TypeChooserActivity.clientManager
                    .wipeCredentialsOnAuthError(ex);
        }
    }
    public static void registerUser(User user) {
        AmazonDynamoDBClient ddb = LoginActivity.clientManager
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        try {
                Log.d(TAG, "registering user: "+ user.getFirstName() + " " + user.getLastName());
                mapper.save(user);
                Log.d(TAG, "user successfuly inserted to DB");
            
        } catch (AmazonServiceException ex) {
            Log.e(TAG, "Error inserting your object");
            TypeChooserActivity.clientManager
                    .wipeCredentialsOnAuthError(ex);
        }
    }

    /*
     * Scans the table and returns the list of Service Providers by type filtering.
     */
    public static ArrayList<ServiceProvider> getServiceProvider (ServiceProviderType type) {

        AmazonDynamoDBClient ddb = TypeChooserActivity.clientManager
                .ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        Map<String, Condition> filter = new HashMap<String, Condition>();
        filter.put("type", new Condition().withComparisonOperator(ComparisonOperator.EQ).withAttributeValueList(new AttributeValue().withS(type.toString())));
	        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
	        scanExpression.setScanFilter(filter);
        
        try {
        	
            PaginatedScanList<ServiceProvider> result = mapper.scan(
                    ServiceProvider.class, scanExpression);

            ArrayList<ServiceProvider> resultList = new ArrayList<ServiceProvider>();
            for (ServiceProvider up : result) {
                resultList.add(up);
            }
            return resultList;

        } catch (AmazonServiceException ex) {
            TypeChooserActivity.clientManager
                    .wipeCredentialsOnAuthError(ex);
        }

        return null;
    }


    public static ArrayList<Task> getTasks() {

    	AmazonDynamoDBClient ddb = TypeChooserActivity.clientManager.ddb();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
        	
            PaginatedScanList<Task> result = mapper.scan(Task.class, scanExpression);

            ArrayList<Task> resultList = new ArrayList<Task>();
            for (Task up : result) {
                resultList.add(up);
            }
            return resultList;

        } catch (AmazonServiceException ex) {
        	TypeChooserActivity.clientManager
                    .wipeCredentialsOnAuthError(ex);
        }

        return null;
    }
}
