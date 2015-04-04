package com.muscleye.will.lookingforflyers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muscleye.will.lookingforflyers.model.Flower;

import java.util.List;

/**
 * Created by Will on 15-04-03.
 */
public class FlyerAdapter extends ArrayAdapter<Flower>
{
    private Context context;
    private List<Flower> flowerList;

    public FlyerAdapter(Context context, int resource, List<Flower> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flyer, parent, false);

        //Display flower name in the TextView widget
        Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        return view;
    }
}