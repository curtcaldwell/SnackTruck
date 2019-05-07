package com.curtcaldwell.snacktruck;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SnackAdapter adapter;
    private RecyclerView recyclerView;
    private Button submitButton;
    CheckBox checkbox;
    private List<Snack> snackList = new ArrayList<>();
    private Map<Integer,Boolean> checkedMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        checkbox = findViewById(R.id.snack_name);
        recyclerView = findViewById(R.id.snacks_recycler);
        recyclerView.setLayoutManager(layoutManager);
        submitButton = findViewById(R.id.submit);
        adapter = new SnackAdapter(this, snackList, new SnackListener() {
            @Override
            public Snack onSnackClick(Snack snack, Boolean checked) {
                checkedMap.put(snack.getId(), checked);
                return null;
            }
        });
        recyclerView.setAdapter(adapter);
        getSnackList();
        adapter.updateList(snackList);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClicked();
            }
        });


    }


    public List<Snack> getSnackList() {
        String json = null;

        try {
            InputStream is = getAssets().open("snacks.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();

        Type type = new TypeToken<List<Snack>>() {

        }.getType();
        snackList = gson.fromJson(json, type);

        for(Snack snack: snackList ) {
            checkedMap.put(snack.getId(), false);
        }

        return snackList;


    }

    public void showSummaryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order Summary");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

        for(Map.Entry<Integer, Boolean> entry : checkedMap.entrySet()) {
            if (entry.getValue()) {
                for (Snack snack : snackList) {
                    if (snack.getId().equals(entry.getKey())) {
                        arrayAdapter.add(snack.getName());
                    }
                }
            }
            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(null);
                    builderInner.setMessage(strName);
                    builderInner.setTitle("Your Selected Items");
                    builderInner.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderInner.show();
                }
            });
        }
            builder.show();

    }

//    private void changeColor() {
//        for(Snack snack : snackList) {
//            if(snack.getIsVeggie() == "yes");
//            checkbox.setTextColor(Color.GREEN);
//            else {
//                checkbox.setTextColor(Color.RED);
//
//            }
//
//        }
//
//
//
//    }

    private void submitButtonClicked() {
        showSummaryDialog();
    }


    public interface SnackListener {
        Snack onSnackClick(Snack snack, Boolean checked);
    }

}
