package com.sudarshanhs.newsapp;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView tvRssLink;
    private GridView gvRss;
    static final String TAG = "mainactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            tvRssLink = (TextView) findViewById(R.id.tvLinkRss);
            tvRssLink.setText(R.string.link);
            gvRss = (GridView) findViewById(R.id.gvRss);


            Map<String,String> category = new HashMap<String,String>();
            category=loadNewsMap();

            List<String> typeList = new ArrayList<String>(category.keySet());

            CategoryGridAdapter categoryGridAdapter = new CategoryGridAdapter(MainActivity.this, typeList);
            gvRss.setAdapter(categoryGridAdapter);
        } catch (Exception e) {
            Log.d(TAG, "Exception   >>" + Log.getStackTraceString(e));
        }
    }


    public class CategoryGridAdapter extends BaseAdapter {

        Context context;
        private LayoutInflater inflater = null;
        private List<String> catList;

        public CategoryGridAdapter(Context c, List<String> category) {
            // TODO Auto-generated constructor stub
            context = c;
            catList = category;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return catList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder {
            Button button;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.grid_but_adapter, null);
            holder.button = (Button) rowView.findViewById(R.id.bGridBut);

            holder.button.setText(catList.get(position));

            holder.button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(0xe0303F9F, PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }

                    }
                    return false;
                }
            });

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Map<String, String> category = new HashMap<String, String>();
                    category = loadNewsMap();
                    List<String> typeList = new ArrayList<String>(category.keySet());

                    String type = typeList.get(position);
                    String url = category.get(type);


                    Log.d(TAG, " url  >>" + url);
                    Log.d(TAG, "type   >>" + type);

                    NewsActivity.type = type;
                    NewsActivity.url = url;


                        Intent i = new Intent(MainActivity.this, NewsActivity.class);
                        startActivity(i);
                        finish();
                }
            });
            return rowView;
        }
    }
    public Map<String,String> loadNewsMap()
    {
        Map<String,String> category = new HashMap<String,String>();
        category.put("English", "http://oneindia.com/rss/news-india-fb.xml");
        category.put("বাংলা", "http://bengali.oneindia.com/rss/news-fb.xml");
        category.put("हिन्दी", "http://hindi.oneindia.com/rss/hindi-fb.xml");
        category.put("ಕನ್ನಡ", "http://kannada.oneindia.com/rss/kannada-news-fb.xml");
        category.put("മലയാളം ","http://malayalam.oneindia.com/rss/malayalam-news-fb.xml");
        category.put("తెలుగు", "http://telugu.oneindia.com/rss/telugu-news-fb.xml");
        category.put("Business", "http://oneindia.com/rss/news-business-fb.xml");
        category.put("Sports", "http://oneindia.com/rss/news-sports-fb.xml");
        return category;
    }



}