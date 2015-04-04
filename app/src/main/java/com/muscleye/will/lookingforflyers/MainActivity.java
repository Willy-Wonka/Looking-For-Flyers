package com.muscleye.will.lookingforflyers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
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

public class MainActivity extends ActionBarActivity
{
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
        output = (TextView) findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());

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
//        output.append(str);
        if (flowerList != null)
        {
            for (Flower flower : flowerList)
            {
                output.append(flower.getName() + "\n");
            }
        }
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

    private class MyTask extends AsyncTask<String, String, String>
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
        protected String doInBackground(String... params)
        {
            String content = HttpManager.getData(params[0]);
            return content;
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
        protected void onPostExecute(String result)
        {
            // able to go to the main thread

            flowerList = FlowerJSONParser.parseFeed(result);
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
