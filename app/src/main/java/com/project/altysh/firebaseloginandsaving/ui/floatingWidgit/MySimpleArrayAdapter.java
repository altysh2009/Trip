package com.project.altysh.firebaseloginandsaving.ui.floatingWidgit;

/**
 * Created by Altysh on 3/13/2018.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.altysh.firebaseloginandsaving.R;

import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<NoteObj> {
    private final Context context;
    private final ArrayList<NoteObj> values;

    public MySimpleArrayAdapter(Context context, ArrayList<NoteObj> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
            TextView textView = convertView.findViewById(R.id.note);
            holder.setItemName(textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            LinearLayout linearLayout = convertView.findViewById(R.id.itemselct);

            if (values.get(position).selected)
                linearLayout.setBackground(context.getDrawable(R.drawable.roundednotsel));

            else linearLayout.setBackground(context.getDrawable(R.drawable.rounded));

        }

        holder.getItemName().setText(values.get(position).item);
        //textView.setText(values.get(position).item);


        return convertView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    private class ViewHolder {

        protected TextView itemName;

        public TextView getItemName() {
            return itemName;
        }

        public void setItemName(TextView itemName) {
            this.itemName = itemName;
        }

    }
}
