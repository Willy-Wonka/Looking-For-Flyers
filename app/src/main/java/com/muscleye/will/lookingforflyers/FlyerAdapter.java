package com.muscleye.will.lookingforflyers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.muscleye.will.lookingforflyers.model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Will on 15-04-03.
 */
public class FlyerAdapter extends ArrayAdapter<Flower> {
    private Context context;
    private List<Flower> flowerList;

    private LruCache<Integer, Bitmap> imageCache;

    public FlyerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 100;
        imageCache = new LruCache<>(cacheSize);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flyer, parent, false);

        //Display flower name in the TextView widget
        Flower flower = flowerList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(flower.getName());

        //Display flower photo in ImageView widget
        Bitmap bitmap = imageCache.get(flower.getProductId());
        if (bitmap != null)
        {
            ImageView image = (ImageView) view.findViewById(R.id.imageView1);
            image.setImageBitmap(flower.getBigmap());
        }
        else
        {
            FlyerAndView container = new FlyerAndView();
            container.flower = flower;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }

        return view;
    }

    class FlyerAndView {
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<FlyerAndView, Void, FlyerAndView> {

        @Override
        protected FlyerAndView doInBackground(FlyerAndView... params) {

            FlyerAndView container = params[0];
            Flower flower = container.flower;


            try {
                String imageUrl = MainActivity.PHOTOS_BASE_URL + flower.getPhoto();
                InputStream in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBigmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(FlyerAndView result) {
            //Display flower photo in ImageView widget
            ImageView image = (ImageView) result.view.findViewById(R.id.imageView1);
//            image.setAlpha(100);
            image.setImageBitmap(result.bitmap);
            image.setImageAlpha(200);
//            result.flower.setBigmap(result.bitmap);
            imageCache.put(result.flower.getProductId(), result.bitmap);
        }
    }
}
