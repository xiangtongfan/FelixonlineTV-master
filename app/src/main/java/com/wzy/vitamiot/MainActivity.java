package com.wzy.vitamiot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.wzy.vitamiot.adapters.TVAdapter;
import com.wzy.vitamiot.model.TVModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private String url;
    private ListView lv;
    private TVAdapter adapter;
    private List<TVModel.TvListEntity> tvListEntities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();
        initData();

    }

    private void initData() {
        Gson gson=new Gson();
        TVModel tvModel=gson.fromJson(loadJSONFromAsset(),TVModel.class);
        tvListEntities=tvModel.getTv_list();
        adapter.updateRes(tvListEntities);
    }


    private void initView() {



        lv = (ListView) findViewById(R.id.lv);
        tvListEntities=new ArrayList<>();
        adapter=new TVAdapter(null,this,R.layout.item);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("tv.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    private void intentgo() {
        Intent intent = new Intent();
        intent.setClass(this, VideoTV.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        url=tvListEntities.get(position).getUrl();
        intentgo();
    }
}
