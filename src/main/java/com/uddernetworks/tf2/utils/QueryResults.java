package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;

import java.util.ArrayList;

public class QueryResults {

    private ArrayList<QueryResult> results = new ArrayList<>();

    public void addResult(QueryResult result) {
        try {
            results.add(result);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
        }
    }

    public ArrayList<QueryResult> getList() {
        try {
            return results;
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

}
