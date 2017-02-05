package com.sudarshanhs.newsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.sudarshanhs.newsapp.Module.News;
import com.sudarshanhs.newsapp.config.Config;
import com.sudarshanhs.newsapp.config.Constants;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    static  final String TAG="newsactivity";
    private int lastExpandedPosition = -1;
    ExpandableListView elvNews;
    static  String url="";
    static String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            getSupportActionBar().setTitle("" + type);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            elvNews=(ExpandableListView) findViewById(android.R.id.list);
            elvNews.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastExpandedPosition != -1
                            && groupPosition != lastExpandedPosition) {
                        elvNews.collapseGroup(lastExpandedPosition);
                    }
                    lastExpandedPosition = groupPosition;
                }
            });

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
        {
            new NewsLoadAsy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
        }else
        {
            Log.d(TAG,"Before calling   >>"+url);
            new NewsLoadAsy().execute(url);
        }


        }catch (Exception e)
        {
            Log.d(TAG,"Exception   >>"+Log.getStackTraceString(e));
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent =new Intent(NewsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class NewsLoadAsy extends AsyncTask<String, Void, List<News> >
    {
    ProgressDialog pg;
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if(pg!=null)
        {
            pg.dismiss();
            pg=null;
        }
        pg = new ProgressDialog(NewsActivity.this);
        pg.setMessage("Loading...");
        pg.show();
    }

    @Override
    protected List<News> doInBackground(String... arg0)
    {
        List<News> newsList=new ArrayList<News>();
        NodeList nl;
        try{
            String url=arg0[0];

            Log.d(TAG,"arg   >>"+arg0);
            Log.d(TAG, "url in do in back nground" + url);

            String xml = Config.getXmlFromUrlGet(url);
            Log.d(TAG, "Xml string is"+xml);

            org.w3c.dom.Document doc =Config.getDomElement(xml); // getting DOM element
            Log.d(TAG, "document is "+doc.toString());
            nl = doc.getElementsByTagName(Constants.KEY_ITEM);

            for(int i=0;i<nl.getLength();i++)
            {
                News news=new News();

                Element e1= (Element) nl.item(i);
                news.setTitle(Config.getValue(e1, Constants.KEY_TITLE));
                news.setDescription(Config.getValue(e1, Constants.KEY_DESCRIPTION));

                newsList.add(news);
            }
        }
        catch(Exception e)
        {
            Log.d(TAG, "Exception >>" + Log.getStackTraceString(e));
            return null;
        }
        return newsList;
    }
    @Override
    protected void onPostExecute(List<News>  result)
    {
        pg.dismiss();
        super.onPostExecute(result);
        if(result==null)
        {
            Toast.makeText(getApplicationContext(), "Oops! Something went wrong!    ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            MyExpandableAdapter	 adapter =  new MyExpandableAdapter(NewsActivity.this,result);
            elvNews.setAdapter(adapter);

        }

    }
}

public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    List<News> newsList;



    public MyExpandableAdapter(Activity activity,List<News> newsList)
    {
        this.activity=activity;
        this.newsList = newsList;
    }




    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TextView tvDescription = null;

        if (convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.child, null);
        }

        tvDescription=(TextView)convertView.findViewById(R.id.tvDescription);




        News tb =newsList.get(groupPosition);
        String dis=tb.getDescription();
        tvDescription.setText("" + dis);

        convertView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
            }
        });

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        TextView tvTitle = null;
        ImageView myImgView=null;
        if (convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.parent, null);
        }

        tvTitle=(TextView)convertView.findViewById(R.id.tvTitle);
        myImgView=(ImageView)convertView.findViewById(R.id.ivImage);

        News tb =newsList.get(groupPosition);
        String title=tb.getTitle();
        tvTitle.setText("" + title);

        int imageResourceId = isExpanded ? R.drawable.ic_action_up : R.drawable.ic_action_down;
        myImgView.setImageResource(imageResourceId);


        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }


    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return newsList.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getChildrenCount(int arg0) {
        return 1;
    }

}

}
