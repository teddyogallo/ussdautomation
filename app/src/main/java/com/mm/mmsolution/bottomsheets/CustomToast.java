package com.mm.mmsolution.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.mmsolution.R;

public class CustomToast extends Toast {

    public CustomToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast t = Toast.makeText(context,text,duration);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate(R.layout.custom_toast,null);

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);

        t.setView(layout);


        return t;
    }

}