package com.mm.mmsolution.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hover.sdk.api.Hover;
import com.mm.mmsolution.MainActivity;
import com.mm.mmsolution.R;
import com.hover.sdk.api.HoverParameters;

import com.hover.sdk.api.Hover;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PaymentsBottomSheet extends BottomSheetDialogFragment {
    @Override public int getTheme() {
        return R.style.BottomSheetDialog;
    }
    private TextInputLayout phonenumlay;
    private TextInputLayout amountlay;
    private EditText phonenum;
    private EditText amount;

    private String amounts;
    AppCompatButton proceed;
    private Context mContext;
    private ImageView close;
    String amountToBeSent;
    String phonenumber;
    String agent;
    String requesttype;

    String getLastThreeCharacters(String word) {
        if (word.length() == 3) {
            return word;
        } else if (word.length() > 3) {
            return word.substring(word.length() - 3);
        } else {
            // whatever is appropriate in this case
            Log.e("less than 3", "word has fewer than 3 characters!");
            return "nll";
        }
    }







   @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView;
       rootView = inflater.inflate(R.layout.amounts_screen, container, false);
       mContext =  getContext();
       Hover.initialize(mContext);
        if (getDialog() != null && getDialog().getWindow() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getColor(R.color.black_over)));
            }
            getDialog().setCanceledOnTouchOutside(true);
        }
        close = rootView.findViewById(R.id.close);
        amountlay = rootView.findViewById(R.id.amounttobepaidlay);
        amount = rootView.findViewById(R.id.amounttobepaid);
        phonenumlay = rootView.findViewById(R.id.phoneNumLay);

        phonenum = rootView.findViewById(R.id.phoneNum);
        proceed = rootView.findViewById(R.id.proceedtwo);
        amount.setImeOptions(EditorInfo.IME_ACTION_DONE);
        amount.setRawInputType(InputType.TYPE_CLASS_TEXT);

        if (getArguments() != null) {
            if (getArguments().getString("amounts")!=null) {
                amount.setText(getArguments().getString("amount"));
                if(getArguments().getString("type")!=null && getArguments().getString("type").equals("tnmagent") )
                {
                    requesttype = getArguments().getString("type");
                    Log.e("type is ", "agent");
                    phonenumlay.setHint("Agent");
                    agent=getArguments().getString("agent");
                    phonenum.setText(getArguments().getString("agent"));
                }
                else {
                    phonenumlay.setHint("Phone number");
                    phonenum.setText(getArguments().getString("phoneNum"));
                }


            }
        }

        proceed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //These two variables(agent & phonenumber) access and store values from the  same Input box, and the logic has already been written. To access the values, reference these variables as they are here:-
                        agent=phonenum.getText().toString();
                        phonenumber=phonenum.getText().toString();
                        amounts=amount.getText().toString();

                        //These three values are for debugging purposes, see if you are feeding the bottom sheet null values, or properly stores values. If null, they'll be false, if not null, they'll be true
                        Boolean phoneNull= getArguments().getString("phoneNum") != null && !getArguments().getString("phoneNum").equalsIgnoreCase("");
                        Boolean amountNull= getArguments().getString("agent") != null&& !getArguments().getString("agent").equalsIgnoreCase("");
                        Boolean agentNull= getArguments().getString("amount") != null&& !getArguments().getString("amount").equalsIgnoreCase("");
                       //Higher value, longer Toast display toast length, lower value, shorter Toast display time
                        /*int toastLength=5;
                        for (int i=0; i < toastLength; i++)
                        {
                            CustomToast.makeText(mContext, "False means it was sent via intent as null, true means not null\n "+"Amount= "+amountNull+"\n Agent= "+agentNull+"\n Phone number= "+phoneNull, Toast.LENGTH_LONG).show();

                        }*/

                        if(requesttype.equals("tnmagent")&& agentNull && amountNull){
                            Objects.requireNonNull(getDialog()).dismiss();

                                 Intent i = new HoverParameters.Builder(mContext)
                      .request("d47d2d5c")
                       .extra("agent", agent) // Only if your action has variables
                       .extra("amount", amounts)
                       .buildIntent();
                startActivity(i);

                        }else if(requesttype.equals("airtelgent")&& phoneNull && amountNull){
                            Objects.requireNonNull(getDialog()).dismiss();
                            Intent i = new HoverParameters.Builder(mContext)
                                                    .request("bb85d070")
                                       .extra("phone", phonenumber) // Only if your action has variables
                                       .extra("amount", amounts)
                                       .buildIntent();
                                     startActivity(i);

                        }else if(requesttype.equals("tnmphone")&& phoneNull && amountNull){
                            Objects.requireNonNull(getDialog()).dismiss();
                                          Intent i = new HoverParameters.Builder(mContext)
                       .request("fe001af2")
                        .extra("phone", phonenumber) // Only if your action has variables
                        .extra("amount", amounts)
                       .buildIntent();
                startActivity(i);


                        }else if(requesttype.equals("airtelphone")&& phoneNull && amountNull){
                            Objects.requireNonNull(getDialog()).dismiss();

                            Intent i = new HoverParameters.Builder(mContext)
                                  .request("8bd0fd72")
                        .extra("phone", phonenumber) // Only if your action has variables
                       .extra("amount", amounts)
                        .buildIntent();
                   startActivity(i);


                        }else{

                            CustomToast.makeText(mContext, "Please fill all values", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        close.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                });



        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    interface UsersSelectedListener {
        void onUserSelected(boolean selected, String userId);
    }


}
