package com.mm.mmsolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class addoptionnew extends AppCompatActivity {

    EditText servicename;
    EditText servicedescription;
    EditText shortcode;
    EditText fullpath;
    Button submitbuton;

    ArrayList<serviceModel> serviceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addservicelayout);

        servicedescription =(EditText) findViewById(R.id.servicedescription);
        servicename =(EditText) findViewById(R.id.servicenametext);
        shortcode =(EditText) findViewById(R.id.ussdshortcode);
        fullpath =(EditText) findViewById(R.id.fullpathservice);
        submitbuton = (Button)findViewById(R.id.submitbutton);

        loadData();


        submitbuton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String servicenamevalue = servicename.getText().toString().trim();
                String servicedescriptionvalue = servicedescription.getText().toString().trim();
                String shortcodevalue = shortcode.getText().toString().trim();
                String fullpathvalue = fullpath.getText().toString().trim();
                if(!TextUtils.isEmpty(servicenamevalue) && !TextUtils.isEmpty(servicedescriptionvalue)&&!TextUtils.isEmpty(shortcodevalue) && !TextUtils.isEmpty(fullpathvalue)) {

                    String mainshortcode = shortcodevalue;
                    if(shortcodevalue.substring(shortcodevalue.length() - 1).equals("#")){
                        shortcodevalue = shortcodevalue.substring(0, shortcodevalue.length() - 1);
                    }

                    if(shortcodevalue.charAt(0)!='*'){

                        shortcodevalue = "*"+shortcodevalue;

                    }

                    if(fullpathvalue.charAt(0)=='*'){

                        fullpathvalue = fullpathvalue.substring(1);

                    }

                    if(fullpathvalue.substring(fullpathvalue.length() - 1).equals("#") || fullpathvalue.substring(fullpathvalue.length() - 1).equals("*")){

                        fullpathvalue = fullpathvalue.substring(0, fullpathvalue.length() - 1);

                    }

                    String fullussdpath = shortcodevalue+"*"+fullpathvalue;

                    serviceArrayList.add(new serviceModel(servicenamevalue,servicedescriptionvalue,mainshortcode, fullussdpath));

                    saveData();

                    Intent i = new Intent(addoptionnew.this,MainActivity.class);
                    startActivity(i);

                }else{


                    Toast.makeText(getApplicationContext(), "values cannot be empty",
                            Toast.LENGTH_LONG).show();

                }


                }

        });


    }


    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("services", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<serviceModel>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        serviceArrayList = gson.fromJson(json, type);

        // checking below if the array list is empty or not
        if (serviceArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            serviceArrayList = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(serviceArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("services", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }
}
