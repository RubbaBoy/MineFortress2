package com.uddernetworks.tf2.utils;

import java.util.ArrayList;

public class QueryResults {

    private ArrayList<QueryResult> results = new ArrayList<>();

    public void addResult(QueryResult result) {
        results.add(result);
    }

    public ArrayList<QueryResult> getList() {
        return results;
    }

}
