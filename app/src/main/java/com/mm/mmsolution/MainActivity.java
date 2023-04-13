package com.mm.mmsolution;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.hover.sdk.api.HoverParameters;
import com.mm.mmsolution.R;
import com.hover.sdk.api.Hover;
import com.hover.sdk.permissions.PermissionActivity;
import com.mm.mmsolution.bottomsheets.PaymentsBottomSheet;

import java.io.IOException;

import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.autoussd.AutoUssd;


public class MainActivity extends AppCompatActivity {

    MaterialCardView airteldeposit;
    MaterialCardView airtelsendmoney;
    MaterialCardView tnmdeposit;
    MaterialCardView tnmsendmoney;
    MaterialCardView safaricomoptions;
    MaterialCardView addservicebutton;


    private String phonetext = "";
    private String amounttextt = "";
    private String agenttext = "";
    Button telco1button;
    Button telco2button;

    private final int REQUEST_PERMISSION_PHONE_STATE=1;

    int subcriptionid1;
    int subcriptionid2;

    int activesubscriptionid;
    int subslot =0;

    GridView servicenameGrid;
    ArrayList<serviceModel> serviceArrayList;

    private static final String TAG = "MainActivity";

    private final String RESULT_CALLBACK_KEY = "result-callback-key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hover.initialize(MainActivity.this);

        startActivityForResult(new Intent(this, PermissionActivity.class), 0);

        //start auto USSD
        /*AutoUssd.Companion.init(this);

        AutoUssd.Companion.getInstance().registerSessionResultListener(RESULT_CALLBACK_KEY, result -> {
            switch (result.getStatus()) {
                case COMPLETED:
                    Log.d(TAG, "Completed");
                    Toast.makeText(getApplicationContext(), "Completed",
                            Toast.LENGTH_LONG).show();
                    break;
                case PARSED:
                    Log.d(TAG, "Parsed");
                    Toast.makeText(getApplicationContext(), "Parsed",
                            Toast.LENGTH_LONG).show();
                    break;
                case INVALID_SESSION:
                    Log.d(TAG, "Invalid session Id");
                    Toast.makeText(getApplicationContext(), "Invalid session id",
                            Toast.LENGTH_LONG).show();
                    break;
                case UNSUPPORTED_SIM:
                    Log.d(TAG, "Unsupported SIM");
                    Toast.makeText(getApplicationContext(), "Unsupported SIM",
                            Toast.LENGTH_LONG).show();
                    break;
                case SESSION_TIMEOUT:
                    Log.d(TAG, "Session timed-out");
                    Toast.makeText(getApplicationContext(), "Session time out",
                            Toast.LENGTH_LONG).show();
                    break;
                case MENU_CONTENT_MISMATCH:
                    Log.d(TAG, "USSD content did not match menu content");
                    Toast.makeText(getApplicationContext(), "Menu did not match",
                            Toast.LENGTH_LONG).show();
                    break;
                case ACCOUNT_SUBSCRIPTION_EXPIRED:
                    Log.d(TAG, "Account subscription expired");
                    Toast.makeText(getApplicationContext(), "Expired setup",
                            Toast.LENGTH_LONG).show();
                    break;
                case UNKNOWN_ERROR:
                    Log.d(TAG, "Unknown error occurred");
                    Toast.makeText(getApplicationContext(), "unknown error",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        });
         */
        //end auto USSD

        servicenameGrid = findViewById(R.id.idServiceNames);
        /*if (null == serviceArrayList) {
            serviceArrayList = new ArrayList<serviceModel>();
        }*/

        // load tasks from preference
        loadData();

        //serviceArrayList.add(new serviceModel("*100#","support options","*100#", "*100*1#"));

        serviceAdapter adapter = new serviceAdapter(this, serviceArrayList);
        servicenameGrid.setAdapter(adapter);

        airteldeposit = (MaterialCardView) findViewById(R.id.airteltoagent);
        airtelsendmoney = (MaterialCardView) findViewById(R.id.airtelsendmoneybutton);
        tnmdeposit = (MaterialCardView) findViewById(R.id.tnmdepositbutton);
        tnmsendmoney = (MaterialCardView) findViewById(R.id.tnmsendmoneybutton);
        safaricomoptions = (MaterialCardView) findViewById(R.id.safaricombutton);
        telco1button = (Button) findViewById(R.id.simselect1);
        telco2button = (Button) findViewById(R.id.simselect2);
        addservicebutton=(MaterialCardView)findViewById(R.id.addservicebutton);

        telco1button.setVisibility(View.GONE);
        telco2button.setVisibility(View.GONE);


        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            String permissiontitle = "Allow permissions";
            String permissionexplanation = "Please give permissions to allow the app to check your mobile network state";
            String permission_name = android.Manifest.permission.READ_PHONE_STATE;
            int permisisoncode = REQUEST_PERMISSION_PHONE_STATE;
            showExplanation(permissiontitle,permissionexplanation,permission_name,permisisoncode);
            return;
        }
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            Log.d("Sims", "subscriptionId:" + subscriptionId);
        }

        if (subscriptionInfoList != null) {
            if (subscriptionInfoList.size() == 1) {
                String sim1 = subscriptionInfoList.get(0).getDisplayName().toString();
                subcriptionid1 =subscriptionInfoList.get(0).getSubscriptionId();
                telco1button.setVisibility(View.VISIBLE);
                telco1button.setText(sim1);
            } else {
                String sim1 = subscriptionInfoList.get(0).getDisplayName().toString();
                String sim2 = subscriptionInfoList.get(1).getDisplayName().toString();
                subcriptionid1 =subscriptionInfoList.get(0).getSubscriptionId();
                subcriptionid2 =subscriptionInfoList.get(1).getSubscriptionId();
                telco1button.setVisibility(View.VISIBLE);
                telco1button.setText(sim1);

                telco2button.setVisibility(View.VISIBLE);
                telco2button.setText(sim2);
            }

        }

        telco1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activesubscriptionid = subcriptionid1;
                telco2button.setBackgroundColor((getResources().getColor(R.color.white)));
                telco1button.setBackgroundColor((getResources().getColor(R.color.green)));
                subslot=0;
            }
        });

        telco2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activesubscriptionid = subcriptionid2;
                telco1button.setBackgroundColor((getResources().getColor(R.color.white)));
                telco2button.setBackgroundColor((getResources().getColor(R.color.green)));
                subslot=1;

            }
        });

        addservicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, addoptionnew.class);
                startActivity(i);

            }
        });

        safaricomoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent i = new HoverParameters.Builder(MainActivity.this)
                        .request("5dbfa5bb")
                        .setEnvironment(HoverParameters.TEST_ENV)
                        .buildIntent();
                startActivity(i);*/

                /*TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    String permissiontitle = "Allow permissions";
                    String permissionexplanation = "Please give permissions to allow the app to automate your USSD code";
                    String permission_name = android.Manifest.permission.CALL_PHONE;
                    int permisisoncode = REQUEST_PERMISSION_PHONE_STATE;
                    showExplanation(permissiontitle,permissionexplanation,permission_name,permisisoncode);
                    return;
                }


                manager.sendUssdRequest("*544*11#", new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                        super.onReceiveUssdResponse(telephonyManager, request, response);
                        Toast.makeText(MainActivity.this, "USD Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                        Toast.makeText(MainActivity.this, "USD Request failed!", Toast.LENGTH_SHORT).show();
                    }
                }, new Handler());*/




                /*String ussd = "*544*11";
                callussd(ussd);*/

                /*final Map<String, String> variables = new HashMap<>();

                variables.put("phone", null);
                variables.put("amount", null);

                AutoUssd.Companion.getInstance().executeSession(
                        "64327b34757f1dc41cfddfeb",
                        null,
                        null,
                        null
                );*/




                Intent i = new HoverParameters.Builder(MainActivity.this)
                        .request("5dbfa5bb")
                        .buildIntent();
                startActivity(i);

            }
        });

        airteldeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String requestype = "airtelgent";
                showAgentsBottomSheet(requestype);*/


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View textEntryView = inflater.inflate(R.layout.alertdialogue, null);
                builder.setTitle("Request");
                builder.setMessage("Fill details to complete");
                builder.setView(textEntryView);
                final EditText phonenumberEdit = (EditText) textEntryView .findViewById(R.id.phoneText);
                final EditText amountEdit = (EditText) textEntryView .findViewById(R.id.amountText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phonevalue = phonenumberEdit.getText().toString().trim();
                        String amountvalue = amountEdit.getText().toString().trim();
                        if(!TextUtils.isEmpty(phonevalue) && !TextUtils.isEmpty(amountvalue)){


                            Intent i = new HoverParameters.Builder(MainActivity.this)
                                    .request("bb85d070")
                                    .extra("phone", phonevalue) // Only if your action has variables
                                    .extra("amount", amountvalue)
                                    .buildIntent();
                            startActivity(i);
                            String ussd = "*211*2*"+phonevalue+"*"+amountvalue;
                            airteldeposit.setBackgroundColor((getResources().getColor(R.color.greyblue)));
                            airtelsendmoney.setBackgroundColor((getResources().getColor(R.color.white)));
                            tnmdeposit.setBackgroundColor((getResources().getColor(R.color.white)));
                            tnmsendmoney.setBackgroundColor((getResources().getColor(R.color.white)));

                            /*callussd(ussd);

                            dialog.cancel();*/

                            /*final Map<String, String> variables = new HashMap<>();

                            variables.put("phone", phonevalue);
                            variables.put("amount", amountvalue);

                            AutoUssd.Companion.getInstance().executeSession(
                                    "64327180757f1dc41cfddfe9",
                                    variables,
                                    null,
                                    null
                            );*/

                        }else{

                            Toast.makeText(getApplicationContext(), "values cant be empty",
                                    Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }



                    }
                });
                builder.show();


                //showAmountsBottomSheet(amounttextt, phonetext);

            }
        });


        airtelsendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String requestype = "airtelphone";
                showAgentsBottomSheet(requestype);*/
                //showAmountsBottomSheet(amounttextt, phonetext);


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View textEntryView = inflater.inflate(R.layout.alertdialogue, null);
                builder.setTitle("Request");
                builder.setMessage("Fill details to complete");
                builder.setView(textEntryView);
                final EditText phonenumberEdit = (EditText) textEntryView .findViewById(R.id.phoneText);
                final EditText amountEdit = (EditText) textEntryView .findViewById(R.id.amountText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phonevalue = phonenumberEdit.getText().toString().trim();
                        String amountvalue = amountEdit.getText().toString().trim();
                        if(!TextUtils.isEmpty(phonevalue) && !TextUtils.isEmpty(amountvalue)){

                            airteldeposit.setBackgroundColor((getResources().getColor(R.color.white)));
                            airtelsendmoney.setBackgroundColor((getResources().getColor(R.color.greyblue)));
                            tnmdeposit.setBackgroundColor((getResources().getColor(R.color.white)));
                            tnmsendmoney.setBackgroundColor((getResources().getColor(R.color.white)));
                            String ussd = "*211*2*"+phonevalue+"*"+amountvalue;
                            /*callussd(ussd);*/
                            Intent i = new HoverParameters.Builder(MainActivity.this)
                                    .request("8bd0fd72")
                                    .extra("phone", phonevalue) // Only if your action has variables
                                    .extra("amount", amountvalue)
                                    .buildIntent();
                            startActivity(i);

                            dialog.cancel();

                        }else{

                            Toast.makeText(getApplicationContext(), "values cant be empty",
                                    Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }



                    }
                });
                builder.show();


            }
        });


        tnmdeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String requestype = "tnmagent";
                showAgentsBottomSheet(requestype);*/
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View textEntryView = inflater.inflate(R.layout.agentdialogue, null);
                builder.setTitle("Request");
                builder.setMessage("Fill details to complete");
                builder.setView(textEntryView);
                final EditText agentEdit = (EditText) textEntryView .findViewById(R.id.agentText);
                final EditText amountEdit = (EditText) textEntryView .findViewById(R.id.amountText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String agentvalue = agentEdit.getText().toString().trim();
                        String amountvalue = amountEdit.getText().toString().trim();
                        if(!TextUtils.isEmpty(agentvalue) && !TextUtils.isEmpty(amountvalue)){
                            airteldeposit.setBackgroundColor((getResources().getColor(R.color.white)));
                            airtelsendmoney.setBackgroundColor((getResources().getColor(R.color.white)));
                            tnmdeposit.setBackgroundColor((getResources().getColor(R.color.greyblue)));
                            tnmsendmoney.setBackgroundColor((getResources().getColor(R.color.white)));
                            String ussd = "*444*1*1*2*"+agentvalue+"*"+amountvalue;

                            /*callussd(ussd);*/
                            Intent i = new HoverParameters.Builder(MainActivity.this)
                                    .request("d47d2d5c")
                                    .extra("agent", agentvalue) // Only if your action has variables
                                    .extra("amount", amountvalue)
                                    .buildIntent();
                            startActivity(i);
                            dialog.cancel();

                        }else{

                            Toast.makeText(getApplicationContext(), "values cant be empty",
                                    Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }



                    }
                });
                builder.show();

            }
        });


        tnmsendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String requestype = "tnmphone";
                showAgentsBottomSheet(requestype);*/
                //showAmountsBottomSheet(amounttextt, phonetext);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View textEntryView = inflater.inflate(R.layout.alertdialogue, null);
                builder.setTitle("Request");
                builder.setMessage("Fill details to complete");
                builder.setView(textEntryView);
                final EditText phonenumberEdit = (EditText) textEntryView .findViewById(R.id.phoneText);
                final EditText amountEdit = (EditText) textEntryView .findViewById(R.id.amountText);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phonevalue = phonenumberEdit.getText().toString().trim();
                        String amountvalue = amountEdit.getText().toString().trim();
                        if(!TextUtils.isEmpty(phonevalue) && !TextUtils.isEmpty(amountvalue)){
                            airteldeposit.setBackgroundColor((getResources().getColor(R.color.white)));
                            airtelsendmoney.setBackgroundColor((getResources().getColor(R.color.white)));
                            tnmdeposit.setBackgroundColor((getResources().getColor(R.color.white)));
                            tnmsendmoney.setBackgroundColor((getResources().getColor(R.color.greyblue)));
                            String ussd = "*444*1*1*1*"+phonevalue+"*"+amountvalue;
                            /*callussd(ussd);*/
                            Intent i = new HoverParameters.Builder(MainActivity.this)
                                    .request("fe001af2")
                                    .extra("phone", phonevalue) // Only if your action has variables
                                    .extra("amount", amountvalue)
                                    .buildIntent();
                            startActivity(i);
                            dialog.cancel();

                        }else{

                            Toast.makeText(getApplicationContext(), "values cant be empty",
                                    Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }



                    }
                });
                builder.show();


            }
        });







    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO #3: Dispose AutoUssd SDK
        AutoUssd.Companion.getInstance().unregisterSessionResultListener(RESULT_CALLBACK_KEY);
        AutoUssd.Companion.getInstance().dispose();
    }

    public void showAgentsBottomSheet(String requesttype) {
        PaymentsBottomSheet bottomSheetFragment = new PaymentsBottomSheet();

        Bundle args = new Bundle();
        args.putString("type", requesttype);
        bottomSheetFragment.setArguments(args);
        bottomSheetFragment.show(((AppCompatActivity) this).getSupportFragmentManager(), bottomSheetFragment.getTag());
    }
    public void showAmountsBottomSheet(String amount, String phonetext) {
        PaymentsBottomSheet bottomSheetFragment = new PaymentsBottomSheet();

        Bundle args = new Bundle();
        args.putString("amount", amount);
        args.putString("phoneNum", phonetext);

        bottomSheetFragment.setArguments(args);
        bottomSheetFragment.show(((AppCompatActivity) this).getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            String[] sessionTextArr=null;
            String uuid=null;
            String actionidthis=null;
            String actionnamethis = null;


            try {
                if (data.getStringArrayExtra("session_messages") != null) {
                    sessionTextArr = data.getStringArrayExtra("session_messages");
                }
            }catch (NullPointerException e ){

            }


            try {
            if (data.getStringExtra("uuid") != null) {
                uuid = data.getStringExtra("uuid");
            } }catch (NullPointerException e ){

            }



                try {
            if (data.getStringExtra("action_id") != null) {
                actionidthis = data.getStringExtra("action_id");
            } }catch (NullPointerException e ){

                }


                    try {
            if (data.getStringExtra("action_name") != null) {
                actionnamethis = data.getStringExtra("action_name");
            } }catch (NullPointerException e ){

                    }

                        try {
            if (data.getStringExtra("uuid") == null) {
                uuid = data.getStringExtra("uuid");
            } }catch (NullPointerException e ){

                        }

            if(actionnamethis!=null){
                Toast.makeText(this, "Success for "+actionnamethis+"  for" + sessionTextArr, Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Error: " + data.getStringExtra("error"), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void callussd(String ussdcode){


        String encodedHash = Uri.encode("#");
        String ussd = ussdcode + encodedHash ;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ ussd));
        startActivity(callIntent);
    }
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
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