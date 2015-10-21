package com.fos.inspiron.foodorderingsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Inspiron on 8/29/2015.
 */
public class FragementFood extends Fragment{



        private ListView GetAllFoodListview;
        private JSONArray jsonArray;
        private String Table;
        private String OrderID;


        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup Cointainer,Bundle savedInstanceState){
            return inflater.inflate(R.layout.fragement_food, Cointainer,false);
        }
        @Override
        public void onStart() {
            super.onStart();



            this.GetAllFoodListview = (ListView) this.getView().findViewById(R.id.GetAllFood);

            new GetAllCustomerTask().execute(new ApiConnector());

            this.GetAllFoodListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        // GEt the customer which was clicked
                        JSONObject customerClicked = jsonArray.getJSONObject(position);
                        // Send Customer ID
                        Intent showDetails = new Intent(getActivity().getApplicationContext(), FoodDetailActivity.class);
                        showDetails.putExtra("FoodID", customerClicked.getInt("FoodID"));
                        showDetails.putExtra("Table",GetTable());
                        showDetails.putExtra("OrderID",GetOrderID());



                        startActivity(showDetails);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


        public void setListAdapter(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
            this.GetAllFoodListview.setAdapter(new GetlistFood(jsonArray, getActivity()));
        }


        private class GetAllCustomerTask extends AsyncTask<ApiConnector, Long, JSONArray> {
            @Override
            protected JSONArray doInBackground(ApiConnector... params) {

                // it is executed on Background thread

                return params[0].GetFOOD();
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {

                setListAdapter(jsonArray);


            }
        }


        @Override
        public void onResume() {
            super.onResume();

            new GetAllCustomerTask().execute(new ApiConnector());
        }
    public String GetTable(){
        Intent intent = getActivity().getIntent();
        this.Table=intent.getStringExtra("Table");
        return this.Table;
    }
    public String GetOrderID(){
        Intent intent = getActivity().getIntent();
        this.OrderID=intent.getStringExtra("OrderID");


        return  this.OrderID;
    }



    }
