package com.amonimus.markbooker;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Memory {
    private static Memory instance = null;
    JSONObject data = new JSONObject();

    protected Memory() {
        // Exists only to defeat instantiation.
    }

    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }

    public void init() throws JSONException {
        data.put("version", "0.1");
        data.put("records", new JSONArray());
    }

    @NonNull
    public String toString() {
        return data.toString();
    }

    public JSONArray getRecords() throws JSONException {
        return data.getJSONArray("records");
    }

    public void overrideJson(String jsonString) throws JSONException {
        data = new JSONObject(jsonString);
    }

    void addLink(JSONObject record, String LinkUrl, String DisplayName) throws JSONException {
        JSONObject jsonLink = new JSONObject();
        jsonLink.put("LinkName", DisplayName);
        jsonLink.put("LinkUrl", LinkUrl);
        JSONArray links = record.getJSONArray("Links");
        links.put(jsonLink);
    }

    JSONObject addRecord(String name) throws JSONException {
        JSONObject jsonRecord = new JSONObject();
        jsonRecord.put("DisplayName", name);
        JSONArray links = new JSONArray();
        jsonRecord.put("Links", links);
        JSONArray records = getRecords();
        records.put(jsonRecord);
        return jsonRecord;
    }

    JSONObject getRecord(String DisplayName) throws JSONException {
        JSONArray records = getRecords();
        for (int i = 0; i < records.length(); i++) {
            JSONObject record = records.getJSONObject(i);
            if (record.get("DisplayName").equals(DisplayName)) {
                return record;
            }
        }
        return null;
    }
}
