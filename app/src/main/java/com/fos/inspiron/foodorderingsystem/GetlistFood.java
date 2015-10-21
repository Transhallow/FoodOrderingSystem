package com.fos.inspiron.foodorderingsystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Inspiron on 8/9/2015.
 */
public class GetlistFood extends BaseAdapter {
    private JSONArray dataArray;
    private Activity activity;

    private static final String baseUrlforImage ="http://192.168.43.108/";

    private static LayoutInflater inflator= null;

    public GetlistFood(JSONArray jsonArray, Activity a){

        this.dataArray= jsonArray;
        this.activity=a;


        inflator=(LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //setup convert view if it is null
        final ListCell cell;
        if(convertView == null){
            convertView= inflator.inflate(R.layout.get_all_food,null);
                    cell= new ListCell();

            cell.Name= (TextView) convertView.findViewById(R.id.Name);
            cell.Price= (TextView) convertView.findViewById(R.id.Price);

            convertView.setTag(cell);
        }
        else{
            cell =(ListCell) convertView.getTag();
        }
        //change the data of the cell
        try {


            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.Name.setText("Name :"+jsonObject.getString("Name"));
            cell.Price.setText("Price :" + jsonObject.getDouble("Price"));






        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
    private class ListCell{
        private TextView Name;
        private TextView Price;

    }



}

