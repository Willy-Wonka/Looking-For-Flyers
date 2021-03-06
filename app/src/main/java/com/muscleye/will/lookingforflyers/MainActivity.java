package com.muscleye.will.lookingforflyers;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.muscleye.will.lookingforflyers.model.Flower;
import com.muscleye.will.lookingforflyers.parsers.FlowerJSONParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity
{
    @SuppressWarnings("unuserd")
    public static String PHOTOS_BASE_URL = "http://services.hanselandpetal.com/photos/";
    String str1;
    TextView output;
    ProgressBar pb;
    List<MyTask> tasks;

    List<Flower> flowerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TextView for vertical scrolling
//        output = (TextView) findViewById(R.id.textView);
//        output.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        //startActivity(browserIntent);
        Log.d("url", "start getPage");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.test)
        {
            if (isOnline())
            {
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                //https://local.flyerservices.com/LCL/RCSSW/en/a27b7bfe-792f-43dd-85cd-b61f11604509/Product/List?tagId=1597&storeId=0c2a5048-cc47-4dcf-99b7-7674345b06c1
            }
            else
            {
                Toast.makeText(this, "Network isn't available",  Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    private void requestData(String uri)
    {
        MyTask task = new MyTask();
        task.execute(uri);//not Parallel processing
        //task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "p1", "p2", "p3");//Parallel
    }

    protected void updateDisplay()
    {
        //User FlowerAdapter to display data
        FlyerAdapter adapter = new FlyerAdapter(this, R.layout.item_flyer, flowerList);
        setListAdapter(adapter);
//        output.append(str);
//        if (flowerList != null)
//        {
//            for (Flower flower : flowerList)
//            {
//                output.append(flower.getName() + "\n");
//            }
//        }
    }

    protected boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private class MyTask extends AsyncTask<String, String, List<Flower>>
    {
        @Override
        protected void onPreExecute()
        {
//            updateDisplay("Starting task");

            if (tasks.size() == 0)
            {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Flower> doInBackground(String... params)
        {
            String content = HttpManager.getData(params[0]);
            flowerList = FlowerJSONParser.parseFeed(content);

//            for (Flower flower : flowerList)
//            {
//                try
//                {
//                    String imageUrl = PHOTOS_BASE_URL + flower.getPhoto();
//                    InputStream in = (InputStream) new URL(imageUrl).getContent();
//                    Bitmap bitmap = BitmapFactory.decodeStream(in);
//                    flower.setBigmap(bitmap);
//                    in.close();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

            return flowerList;
            /*
            for (int i = 0; i < params.length; i++)
            {
                publishProgress("Working with " + params[i]);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // will be send to onPostExecute String
            return "Task Complete";
            */
        }

        @Override
        protected void onPostExecute(List<Flower> result)
        {
            // able to go to the main thread

            //flowerList = FlowerJSONParser.parseFeed(result);
            updateDisplay();

            tasks.remove(this);
            if (tasks.size() == 0)
            {
                pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
//            updateDisplay(values[0]);
        }
    }
}
