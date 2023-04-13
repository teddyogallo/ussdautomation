package com.mm.mmsolution;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class serviceAdapter extends ArrayAdapter<serviceModel> {
    private Context context;
    public serviceAdapter(@NonNull Context context, ArrayList<serviceModel> serviceModelArrayList) {
        super(context, 0, serviceModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.individualcard, parent, false);
        }

        context = parent.getContext();

        serviceModel courseModel = getItem(position);
        TextView servicenametext = listitemView.findViewById(R.id.actualdialcode);
        TextView dialcodetext = listitemView.findViewById(R.id.dialcodestring);
        TextView servicedescription = listitemView.findViewById(R.id.airtedepositmoneydescription);
        MaterialCardView cardbutton = listitemView.findViewById(R.id.airteltoagent);


        servicenametext.setText(courseModel.getShortcode());
        dialcodetext.setText(courseModel.getUssdcode());
        servicedescription.setText(courseModel.getDescription());


        cardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callussd(courseModel.getUssdcode());

            }});

        return listitemView;
    }



    private void callussd(String ussdcode){


        String encodedHash = Uri.encode("#");
        String ussd = ussdcode + encodedHash ;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ ussd));
        context.startActivity(callIntent);
    }
}
