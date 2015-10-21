package com.fos.inspiron.foodorderingsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Inspiron on 8/10/2015.
 */
public class DrinkDetailActivity extends AppCompatActivity {
    private TextView Name;
    private TextView Price;
    private TextView Discription;
    private String Table,Tittle;
    private String OrderID;


    private static final String baseUrlforImage ="http://192.168.43.108/";
    private ImageView imageView;
    private int DrinkID;
    private String drink;


    //drinkorder
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;







    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fooddetail);
        this.Name = (TextView) this.findViewById(R.id.Name);
        this.Price = (TextView) this.findViewById(R.id.Price);
        this.Discription = (TextView) this.findViewById(R.id.Discription);
        this.imageView =(ImageView) this.findViewById(R.id.imageView);



        this.Table= getIntent().getStringExtra("Table");
        this.OrderID= getIntent().getStringExtra("OrderID");

        Log.i("Table Detail","Table :"+this.Table);
        Log.i("OrderID Detail","OrderID :"+this.OrderID);

        //get FoodID
        this.DrinkID = getIntent().getIntExtra("DrinkID", -1);

        if (this.DrinkID > 0) {
            //we have a customer ID passed correctly
            new GetDrinkDetails().execute(new ApiConnector());
        }

    }

    private class GetDrinkDetails extends AsyncTask<ApiConnector, Long, JSONArray> {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return params[0].GetDrinkDetail(DrinkID);

        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            try
            {
                JSONObject Drinkdetails = jsonArray.getJSONObject(0);

                Name.setText(Drinkdetails.getString("Name"));
                Price.setText(Drinkdetails.getString("Price"));
                Discription.setText(Drinkdetails.getString("Description"));
                setTitle(Drinkdetails.getString("Name"));
                Tittle=Drinkdetails.getString("Name");
                drink=Drinkdetails.getString("DrinkID");






                //name of image
                final String nameOfImage = Drinkdetails.getString("Image");
                //full url of image
                String UrlOfImage =baseUrlforImage + nameOfImage;
                Log.i("Image URL", UrlOfImage );
                //new async task

                new AsyncTask<String, Void, Bitmap>(){

                    protected Bitmap doInBackground(String... params){
                        String url =params[0];
                        Bitmap icon= null;

                        try {
                            InputStream in = new java.net.URL(url).openStream();
                            icon = BitmapFactory.decodeStream(in);
                        }catch(MalformedInputException e){
                            e.printStackTrace();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                        return icon;

                    }
                    protected void onPostExecute(Bitmap result){
                        imageView.setImageBitmap(result);
                    }
                }.execute(UrlOfImage);


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            //send to new class
            Intent i = new Intent(DrinkDetailActivity.this, Order.class);
            i.putExtra("Table", Table);
            i.putExtra("OrderID", OrderID);
            startActivity(i);

        }
        else if (id == R.id.Call) {
            //
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog show = builder
                    .setTitle("Table "+Table)
                    .setMessage("Do you wish to call waiter")
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            final ProgressDialog p = ProgressDialog.show(DrinkDetailActivity.this, "Waiting for Server", "Accessing Server");
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {



                                        httpclient = new DefaultHttpClient();
                                        httppost = new HttpPost("http://192.168.43.108/android/Call%20Waiter.php"); // make sure the url is correct.

                                        HttpEntity httpEntity = null;
                                        //add your data
                                        nameValuePairs = new ArrayList<NameValuePair>(1);
                                        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                                        nameValuePairs.add(new BasicNameValuePair("Table", Table.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];

                                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                        //Execute HTTP Post Request
                                        HttpResponse httpResponse = httpclient.execute(httppost);

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

                            Toast toast = Toast.makeText(getApplicationContext(), "Thank you for your patients, a waiter will be coming shortly",
                                    Toast.LENGTH_LONG);


                            toast.show();



                            Log.i("TAG", "Waiter is being Called");

                        }
                    })
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("TAG", "Canceled");
                            //Do nothing on no
                        }
                    })
                    .show();

            return true;
        }else if(id == R.id.Pay){
            //makes alert box and handles the http post
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog show = builder
                    .setTitle("Table "+Table)
                    .setMessage("Do you wish to do payment")
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            final ProgressDialog p = ProgressDialog.show(DrinkDetailActivity.this, "Waiting for Server", "Accessing Server");
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    try {



                                        httpclient = new DefaultHttpClient();
                                        httppost = new HttpPost("http://192.168.43.108/android/Payment.php"); // make sure the url is correct.

                                        HttpEntity httpEntity = null;
                                        //add your data
                                        nameValuePairs = new ArrayList<NameValuePair>(1);
                                        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                                        nameValuePairs.add(new BasicNameValuePair("OrderID", OrderID.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];

                                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                        //Execute HTTP Post Request
                                        HttpResponse httpResponse = httpclient.execute(httppost);

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

                            Toast toast = Toast.makeText(getApplicationContext(), "Thank you for coming a bill will be coming shortly",
                                    Toast.LENGTH_LONG);


                            toast.show();


                            Log.i("Payment", "Payment has been Notified");

                            //Back to Main Page
                            Intent i = new Intent(DrinkDetailActivity.this, Main.class);
                            startActivity(i);

                        }
                    })
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("TAG", "Canceled");
                            //Do nothing on no
                        }
                    })
                    .show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //button onclick Order
    //Send Post to myphp and update Database

    public void FoodOrder(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog show = builder
                .setTitle("Table "+Table)
                .setMessage("Are you sure you wish to order  " + Tittle)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                        final ProgressDialog p = new ProgressDialog(v.getContext()).show(v.getContext(), "Waiting for Server", "Accessing Server");
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {


                                    httpclient = new DefaultHttpClient();
                                    httppost = new HttpPost("http://192.168.43.108/android/DrinkOrder.php"); // make sure the url is correct.

                                    HttpEntity httpEntity = null;
                                    //add your data
                                    nameValuePairs = new ArrayList<NameValuePair>(2);
                                    // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                                    nameValuePairs.add(new BasicNameValuePair("DrinkID", drink.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
                                    nameValuePairs.add(new BasicNameValuePair("OrderID", OrderID.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
                                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                    //Execute HTTP Post Request
                                    HttpResponse httpResponse = httpclient.execute(httppost);

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

                        Toast toast = Toast.makeText(getApplicationContext(), "Order has been place,please wait for your Drink to be served",
                                Toast.LENGTH_LONG);


                        toast.show();



                        Log.i("TAG", "Order has Been place");

                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("TAG", "Canceled");
                        //Do nothing on no
                    }
                })
                .show();


    }
}