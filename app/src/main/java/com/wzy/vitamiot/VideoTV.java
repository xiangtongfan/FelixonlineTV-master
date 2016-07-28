package com.wzy.vitamiot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzy.vitamiot.adapters.TVAdapter;
import com.wzy.vitamiot.model.TVModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoTV extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private VideoView vv;
    private String url;

    private LinearLayout drawer;

    private DrawerLayout dr_ly;
    private ListView lv2;

    private TVAdapter adapter;
    private List<TVModel.TvListEntity> tvListEntities;
    private Button p1;
    private Button p2;
    private Button p3;
    private Button p4;
    private Button p5;
    private LinearLayout drawer_right;
    private ProgressBar probar;
    private TextView download_rate;
    private TextView load_rate;
    private GestureDetector mGestureDetector;


    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_tv);
        initView();
        vplay();
        initData();
    }

    private void initData() {
        Gson gson = new Gson();
        TVModel tvModel = gson.fromJson(loadJSONFromAsset(), TVModel.class);
        tvListEntities = tvModel.getTv_list();
        adapter.updateRes(tvListEntities);
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

    private void initView() {
        vv = (VideoView) findViewById(R.id.vv);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        vv.setMediaController(new MediaController(this));

        drawer = (LinearLayout) findViewById(R.id.drawer);

        dr_ly = (DrawerLayout) findViewById(R.id.dr_ly);

        lv2 = (ListView) findViewById(R.id.lv2);
        tvListEntities = new ArrayList<>();
        adapter = new TVAdapter(null, this, R.layout.item);
        lv2.setAdapter(adapter);
        lv2.setOnItemClickListener(this);

        p1 = (Button) findViewById(R.id.p1);
        p1.setOnClickListener(this);
        p2 = (Button) findViewById(R.id.p2);
        p2.setOnClickListener(this);
        p3 = (Button) findViewById(R.id.p3);
        p3.setOnClickListener(this);
        p4 = (Button) findViewById(R.id.p4);
        p4.setOnClickListener(this);
        p5 = (Button) findViewById(R.id.p5);
        p5.setOnClickListener(this);
        drawer_right = (LinearLayout) findViewById(R.id.drawer_right);
        probar = (ProgressBar) findViewById(R.id.probar);
        download_rate = (TextView) findViewById(R.id.download_rate);
        load_rate = (TextView) findViewById(R.id.load_rate);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

    }

    private void vplay() {


        vv.setVideoURI(Uri.parse(url));
        vv.requestFocus();
        bufferVideo();
        prepare();

        vv.start();
//        vv.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE,0);

    }

    private void bufferVideo() {
        vv.setOnInfoListener(this);
        vv.setOnBufferingUpdateListener(this);
    }

    private void prepare() {
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        url = tvListEntities.get(position).getUrl();
        vplay();
        dr_ly.closeDrawer(drawer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.p1:
                changeurl("1");
                break;
            case R.id.p2:
                changeurl("2");

                break;
            case R.id.p3:
                changeurl("3");

                break;
            case R.id.p4:
                changeurl("4");

                break;
            case R.id.p5:
                changeurl("5");

                break;
        }
    }

    private void changeurl(String s) {
        StringBuilder sb = new StringBuilder(url);
        sb.setCharAt(sb.length() - 6, s.charAt(0));
        url = sb.toString();
        vplay();
        dr_ly.closeDrawer(drawer_right);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (vv.isPlaying()) {
                    vv.pause();
                }
                probar.setVisibility(View.VISIBLE);
                download_rate.setText("");
                load_rate.setText("");
                download_rate.setVisibility(View.VISIBLE);
                load_rate.setVisibility(View.VISIBLE);

                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                vv.start();
                probar.setVisibility(View.GONE);
                download_rate.setVisibility(View.GONE);
                load_rate.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                download_rate.setText("" + extra + "kb/s" + " ");
                break;
        }

        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        load_rate.setText(percent + "%");
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mLayout++;
            if (mLayout == 4) {
                mLayout = 0;
            }
            switch (mLayout) {
                case 0:
                    mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
                    break;
                case 1:
                    mLayout = VideoView.VIDEO_LAYOUT_SCALE;
                    break;
                case 2:
                    mLayout = VideoView.VIDEO_LAYOUT_STRETCH;
                    break;
                case 3:
                    mLayout = VideoView.VIDEO_LAYOUT_ZOOM;

                    break;
            }
            vv.setVideoLayout(mLayout, 0);
            return true;
        }
    }
}
