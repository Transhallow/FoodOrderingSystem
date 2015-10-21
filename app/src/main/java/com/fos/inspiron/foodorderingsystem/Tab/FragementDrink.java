package com.fos.inspiron.foodorderingsystem.Tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fos.inspiron.foodorderingsystem.R;

/**
 * Created by Inspiron on 8/29/2015.
 */
public class FragementDrink extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragement_drink, container,false);

    }
}