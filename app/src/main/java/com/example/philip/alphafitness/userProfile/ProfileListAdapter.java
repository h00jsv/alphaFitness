package com.example.philip.alphafitness.userProfile;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.philip.alphafitness.R;

import java.util.ArrayList;

public class ProfileListAdapter extends ArrayAdapter<UserData>{

        private ArrayList<UserData> dataSet;
        Context mContext;

        // View lookup cache
        private static class ViewHolder {
            TextView textViewName;
            TextView textViewValue;
        }

        public ProfileListAdapter(ArrayList<UserData> data, Context context) {
            super(context, R.layout.list_item, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            UserData userData = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder = new ViewHolder(); // view lookup cache stored in tag

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item, parent, false);

            }

            viewHolder.textViewName = convertView.findViewById(R.id.titleTextEdit);
            viewHolder.textViewValue = convertView.findViewById(R.id.valueTextEdit);

            convertView.setTag(viewHolder);

            if (userData.getValue() == null){

                convertView.setBackgroundColor(Color.LTGRAY);

                viewHolder.textViewName.setText(userData.getTitle());
                viewHolder.textViewName.setTextColor(Color.BLACK);
                viewHolder.textViewValue.setText("");
                return convertView;
            }
            Log.e("adapter", userData.getTitle());
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.textViewName.setText(userData.getTitle());
            viewHolder.textViewValue.setText(userData.getValue());
            viewHolder.textViewName.setTextColor(Color.DKGRAY);

            return convertView;
        }

}
