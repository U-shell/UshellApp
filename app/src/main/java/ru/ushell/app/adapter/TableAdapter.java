package ru.ushell.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TableAdapter extends BaseAdapter {

    // Array to hold data in the table
    public ArrayList<HashMap<Integer, String>> data = new ArrayList<>();

    // Method to update data in a cell when it is clicked
    public void updateData(int row, int col, String value) {
        HashMap<Integer, String> rowData = data.get(row);
        rowData.put(col, value);
        notifyDataSetChanged();
    }

    // Method to generate JSON file
//    public void generateJson() {
//        JSONArray jsonArray = new JSONArray();
//        for (HashMap<Integer, String> row : data) {
//            JSONObject jsonObject = new JSONObject(row);
//            jsonArray.put(jsonObject);
//        }
//        // Write JSON to file
//    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    // Override methods to implement BaseAdapter

}