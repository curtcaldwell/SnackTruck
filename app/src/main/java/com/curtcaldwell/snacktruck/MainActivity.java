package com.curtcaldwell.snacktruck;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_FILE = "PREF_FILE";
    private SnackAdapter adapter;
    private RecyclerView recyclerView;
    private Button submitButton;
    CheckBox checkboxVeggieFilter;
    CheckBox checkboxNonVeggieFilter;
    TextView error;
    private List<Snack> snackList = new ArrayList<>();
    private List<Snack> veggieList = new ArrayList<>();
    private List<Snack> nonVeggieList = new ArrayList<>();
    private List<Snack> checkedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        checkboxVeggieFilter = findViewById(R.id.veggieCheckBox);
        checkboxNonVeggieFilter = findViewById(R.id.nonVeggieCheckBox);
        checkboxVeggieFilter.setChecked(true);
        error = findViewById(R.id.error);
        checkboxNonVeggieFilter.setChecked(true);
        recyclerView = findViewById(R.id.snacks_recycler);
        recyclerView.setLayoutManager(layoutManager);
        submitButton = findViewById(R.id.submit);
        adapter = new SnackAdapter(this, snackList, new SnackListener() {
            @Override
            public Snack onSnackClick(Snack snack, Boolean checked) {
                if (checked) {
                    checkedList.add(snack);
                } else {
                    checkedList.remove(snack);

                }
                toggleSubmitButton();
                return null;
            }

        });
        recyclerView.setAdapter(adapter);
        getSnackList();
        toggleSubmitButton();
        adapter.updateList(snackList);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClicked();
            }
        });


        checkboxVeggieFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (!checkboxNonVeggieFilter.isChecked() && checkboxVeggieFilter.isChecked()) {
                    adapter.updateList(veggieList);
                    toggleEmptyState(false);

                } else if (checkboxNonVeggieFilter.isChecked() && !checkboxVeggieFilter.isChecked()) {
                    adapter.updateList(nonVeggieList);
                    toggleEmptyState(false);


                } else if (checkboxNonVeggieFilter.isChecked() && checkboxVeggieFilter.isChecked()) {
                    adapter.updateList(snackList);
                    toggleEmptyState(false);

                } else if (!checkboxNonVeggieFilter.isChecked() && !checkboxVeggieFilter.isChecked()) {
                    toggleEmptyState(true);

                }


            }
        });

        checkboxNonVeggieFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (checkboxNonVeggieFilter.isChecked() && !checkboxVeggieFilter.isChecked()) {
                    adapter.updateList(nonVeggieList);
                    toggleEmptyState(false);

                } else if (!checkboxNonVeggieFilter.isChecked() && checkboxVeggieFilter.isChecked()) {
                    adapter.updateList(veggieList);
                    toggleEmptyState(false);
                } else if (checkboxNonVeggieFilter.isChecked() && checkboxVeggieFilter.isChecked()) {
                    adapter.updateList(snackList);
                    toggleEmptyState(false);

                } else if (!checkboxNonVeggieFilter.isChecked() && !checkboxVeggieFilter.isChecked()) {
                    toggleEmptyState(true);

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addbutton) {

        }
        showAddDialog();

        return super.onOptionsItemSelected(item);
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


        final SharedPreferences sharedPref = this.getSharedPreferences(
                PREF_FILE, Context.MODE_PRIVATE);


        Map<String, ?> prefMap = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : prefMap.entrySet()) {
            String string = sharedPref.getString(entry.getKey(), "");
            if (!string.isEmpty()) {
                snackList.add(gson.fromJson(string, Snack.class));

            }
        }


        veggieList.clear();
        nonVeggieList.clear();
        for (Snack snack : snackList) {


            if (snack.getIsVeggie()) {

                veggieList.add(snack);

            } else {

                nonVeggieList.add(snack);
            }
        }

        return snackList;
    }


    public void showSummaryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.show_summary_dialog_title));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);

        for (int i = 0; i <= checkedList.size() - 1; i++) {
            arrayAdapter.add(checkedList.get(i).getName());

        }
        getSnackList();


        builder.setPositiveButton(getString(R.string.summary_dialog_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                adapter.updateList(snackList);
                dialog.dismiss();
                checkedList.clear();
                checkboxNonVeggieFilter.setChecked(true);
                checkboxVeggieFilter.setChecked(true);
                toggleSubmitButton();

            }
        });

        builder.setAdapter(arrayAdapter, null);
        builder.show();
    }


    public void toggleEmptyState(boolean showEmpty) {
        if (showEmpty) {
            recyclerView.setVisibility(View.INVISIBLE);
            error.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);

        }

    }


    private void submitButtonClicked() {

        showSummaryDialog();
        sendOrder(checkedList);
    }

    private void toggleSubmitButton() {
        if (checkedList.size() == 0) {
            submitButton.setEnabled(false);


        } else {
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true);

        }
    }

    private void showAddDialog() {

        final Dialog dialog = new Dialog(this);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.add_dialog_title_message));
        dialog.setContentView(R.layout.radio_buttondialog);

        Context context = this;

        final SharedPreferences sharedPref = context.getSharedPreferences(
                PREF_FILE, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPref.edit();

        final Gson gson = new Gson();
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        final Button saveButton = dialog.findViewById(R.id.save_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        final EditText inputText = dialog.findViewById(R.id.input_text);

        inputText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String snackName = inputText.getText().toString();
                Snack snack = new Snack();

                if (inputText.getText().toString().trim().equalsIgnoreCase("")) {
                    inputText.setError(getString(R.string.edit_text_error_message));
                } else {


                    if (rg.getCheckedRadioButtonId() == R.id.veggie_radio) {
                        snack.setIsVeggie(true);
                    } else {
                        snack.setIsVeggie(false);
                    }

                    snack.setName(snackName);


                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(snackName.toLowerCase(), new Gson().toJson(snack));


                    editor.commit();
                    adapter.updateList(getSnackList());

                    dialog.dismiss();
                }
            }

        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();


    }


    public interface SnackListener {
        Snack onSnackClick(Snack snack, Boolean checked);
    }

    public void sendOrder(List<Snack> list) {

        //this is where the Api call would exist to send the order.
    }

}
