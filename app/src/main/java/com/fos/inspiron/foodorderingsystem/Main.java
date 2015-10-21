package com.fos.inspiron.foodorderingsystem;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.audiofx.BassBoost;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;


public class Main extends AppCompatActivity {
    private Button OrderB;
    public int Table =1;
    public String OrderID;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;


    JSONArray jsonArray;



    public String GetOrderID()
    {

        return OrderID;
    }
    public String  SetOrderID(String OrderID)
    {
        this.OrderID=OrderID;
        return this.OrderID;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrderB = (Button) findViewById(R.id.Order);
        new GetTableTask().execute(new ApiConnector());



    }

    public void Order(final View v) {

        final ProgressDialog p = new ProgressDialog(v.getContext()).show(v.getContext(), "Waiting for Server", "Accessing Server");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {


                    httpclient = new DefaultHttpClient();
                    httppost = new HttpPost("http://192.168.43.108/android/neworder.php"); // make sure the url is correct.

                    HttpEntity httpEntity = null;
                    //add your data
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                    nameValuePairs.add(new BasicNameValuePair("Table", Integer.toString(Table).trim()));  // $Edittext_value = $_POST['Edittext_value'];
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //Execute HTTP Post Request
                    HttpResponse httpResponse = httpclient.execute(httppost);

                    httpEntity = httpResponse.getEntity();

                    // Convert HttpEntity into JSON Array
                    JSONArray jsonArray = null;

                    if (httpEntity != null) {
                        try {
                            String entityResponse = EntityUtils.toString(httpEntity);

                            Log.e("httpEntity Response : ", entityResponse);

                            jsonArray = new JSONArray(entityResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    JSONObject JOrderID = jsonArray.getJSONObject(0);
                    SetOrderID(JOrderID.getString("OrderID"));


                    runOnUiThread(new Runnable() {
                        public void run() {
                            p.dismiss();
                        }
                    });

                } catch (
                        Exception e
                        )

                {

                    runOnUiThread(new Runnable() {


                        public void run() {


                            p.dismiss();
                        }
                    });

                }
            }
        };thread.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog show = builder
                .setTitle("Order")
                .setMessage("Table " + this.Table)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something


                                Toast toast = Toast.makeText(getApplicationContext(), "Please Make an Order",
                                        Toast.LENGTH_LONG);


                                toast.show();


                                //send to new class
                                Intent i = new Intent(Main.this, Order.class);
                                i.putExtra("Table", Integer.toString(Table));
                                i.putExtra("OrderID", GetOrderID());
                                startActivity(i);
                                Log.i("TAG", "New order");


                            }

                        }


                )
                .

                        setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("TAG", "Canceled");
                                        //Do nothing on no
                                    }
                                }

                        )
                .

                        show();


    }
    private void setItem(JSONArray jsonArray){
        this.jsonArray= jsonArray;



    }
    private class GetTableTask extends AsyncTask<ApiConnector, Long, JSONArray> {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return params[0].Table();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            setItem(jsonArray);


        }
    }



        @Override
        public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


            for(int i=1;i<=6;i++){
                menu.add(i,i,i,"Table "+Integer.toString(i));
            }
            return true;

    }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        for (int i = 1; i <= 6; i++) {
            if (id == i) {
                Toast toast = Toast.makeText(getApplicationContext(), "Table " + i,
                        Toast.LENGTH_LONG);
                toast.show();
                this.Table = i;
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }



}
