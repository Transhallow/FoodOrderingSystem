package com.fos.inspiron.foodorderingsystem;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.fos.inspiron.foodorderingsystem.Tab.FragementDrink;
import com.fos.inspiron.foodorderingsystem.Tab.SlidingTabLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity {

    private ViewPager nPager;
    private SlidingTabLayout nTabs;

    private String OrderID;

    //Call
    HttpPost httppost;
    private String Table;

    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        nPager = (ViewPager) findViewById(R.id.pager);
        nPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        nTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        nTabs.setDistributeEvenly(true);

        nTabs.setViewPager(nPager);


        this.Table =getIntent().getStringExtra("Table");
        this.OrderID= getIntent().getStringExtra("OrderID");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.Call) {
            //makes alert box and handles the http post
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog show = builder
                    .setTitle("Table "+Table)
                    .setMessage("Do you wish to call waiter")
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            final ProgressDialog p = ProgressDialog.show(Order.this, "Waiting for Server", "Accessing Server");
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
        }
        else if(id == R.id.Pay){
            //makes alert box and handles the http post
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog show = builder
                    .setTitle("Table "+Table)
                    .setMessage("Do you wish to do payment")
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something
                            final ProgressDialog p = ProgressDialog.show(Order.this, "Waiting for Server", "Accessing Server");
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
                            Intent i = new Intent(Order.this, Main.class);
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

    class MyPageAdapter extends FragmentPagerAdapter {

        String[] tabs;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment myFragment = null;

            if (position == 1) {

                myFragment = new FragementFood();

            }
            if (position == 0) {
                myFragment = new com.fos.inspiron.foodorderingsystem.FragementDrink();
            }
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

    }




    }

