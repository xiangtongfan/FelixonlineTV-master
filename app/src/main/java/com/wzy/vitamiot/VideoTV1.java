//package com.wzy.vitamiot;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.MediaController;
//import android.widget.TextView;
//import android.widget.VideoView;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//
//public class VideoTV1 extends AppCompatActivity {
//
//    private VideoView vv;
//    private TextView cachetext;
//    private static final String TAG="UDPFileMPlayer";
//    private String remoteUrl;
//    private String localUrl;
//    private ProgressDialog progressDialog = null;
//    private Thread receiveThread=null;
//    /**
//     * 定义了初始缓存区的大小，当视频加载到初始缓存区满的时候，播放器开始播放，
//     */
//    private static final int READY_BUFF = 1316 * 1024*10;
//
//    private static final String FILE_DIR= Environment.getExternalStorageDirectory().getAbsolutePath()+"/VideoCache/";
//
//    /**
//     * 核心交换缓存区，主要是用来动态调节缓存区，当网络环境较好的时候，该缓存区为初始大小，
//     * 当网络环境差的时候，该缓存区会动态增加，主要就是为了避免视频播放的时候出现一卡一卡的现象。
//     */
//    private static final int CACHE_BUFF = 10 * 1024;
//    /**
//     * 单播或组播端口
//     */
//    private static final int PORT = 1234;
//
//    private boolean isready = false;
//    private boolean iserror = false;
//    private int errorCnt = 0;
//    private int curPosition = 0;
//    private long mediaLength = 0;
//    private long readSize = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
//        setContentView(R.layout.activity_video_tv);
//        initView();
//        vplay();
//    }
//
//    private void initView() {
//        vv = (VideoView) findViewById(R.id.vv);
//        Intent intent = getIntent();
//        remoteUrl = intent.getStringExtra("url");
//
//        cachetext = (TextView) findViewById(R.id.cachetext);
//        vv.setMediaController(new MediaController(this));
//        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                dismissProgressDialog();
//                vv.seekTo(curPosition);
//                mp.start();
//            }
//        });
//        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                if(localUrl.endsWith("1.mp4")){
//                    localUrl=localUrl.replace("1.mp4", "2.mp4");
//                    vv.setVideoPath(localUrl);
//                    vv.start();
//                }else if(localUrl.endsWith("2.mp4")){
//                    localUrl=localUrl.replace("2.mp4", "3.mp4");
//                    vv.setVideoPath(localUrl);
//                    vv.start();
//                }else{
//                    localUrl=localUrl.replace("3.mp4", "1.mp4");
//                    vv.setVideoPath(localUrl);
//                    vv.start();
//                }
//            }
//        });
//        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                iserror = true;
//                errorCnt++;
//                vv.pause();
//                showProgressDialog();
//                return true;
//            }
//        });
//    }
//
//    private void showProgressDialog() {
//        mHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                if (progressDialog == null) {
//                    progressDialog = ProgressDialog.show(VideoTV1.this,
//                            "视频缓存", "正在努力加载中 ...", true, false);
//                }
//            }
//        });
//    }
//
//    private void dismissProgressDialog() {
//        mHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                if (progressDialog != null) {
//                    progressDialog.dismiss();
//                    progressDialog = null;
//                }
//            }
//        });
//    }
//
//    private void vplay() {
//        showProgressDialog();
//
//        receiveThread=new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                FileOutputStream out = null;
//                DatagramSocket dataSocket=null;
//                DatagramPacket dataPacket=null;
//                try {
//                    dataSocket = new DatagramSocket(PORT);
//                    byte[] receiveByte = new byte[8192];
//                    dataPacket = new DatagramPacket(receiveByte, receiveByte.length);
//                    Log.i(TAG, "UDP服务启动...");
//                    if (localUrl == null) {
//                        localUrl = FILE_DIR+"1.mp4";
//                    }
//                    Log.i(TAG, "localUrl="+localUrl);
//                    File cacheFile = new File(localUrl);
//
//                    if (!cacheFile.exists()) {
//                        cacheFile.getParentFile().mkdirs();
//                        cacheFile.createNewFile();
//                    }
//
//                    out = new FileOutputStream(cacheFile, true);
//                    int size = 0;
//                    long lastReadSize = 0;
//                    int number=0;
//
//                    int fileNum=0;
////                  mHandler.sendEmptyMessage(VIDEO_STATE_UPDATE);
//
//                    while(size==0){
//                        // 无数据，则循环
//                        dataSocket.receive(dataPacket);
//                        size = dataPacket.getLength();
//                        if (size > 0) {
//                            try {
//                                if(readSize>=READY_BUFF){
//                                    fileNum++;
//
//                                    switch(fileNum%3){
//                                        case 0:
//                                            out=new FileOutputStream(FILE_DIR+"1.mp4");
//                                            break;
//                                        case 1:
//                                            out=new FileOutputStream(FILE_DIR+"2.mp4");
//                                            break;
//                                        case 2:
//                                            out=new FileOutputStream(FILE_DIR+"3.mp4");
//                                            break;
//                                    }
//
//                                    readSize=0;
//                                    if (!isready) {
//                                        mHandler.sendEmptyMessage(CACHE_VIDEO_READY);
//                                    }
//                                }
//                                out.write(dataPacket.getData(), 0, size);
//                                out.flush();
//                                readSize += size;
//                                size = 0;// 循环接收
//
//
//                            } catch (Exception e) {
//                                Log.e(TAG, "出现异常0",e);
//                            }
//
//                        }else{
//                            Log.i(TAG, "TS流停止发送数据");
//                        }
//
//                    }
//
//                    mHandler.sendEmptyMessage(CACHE_VIDEO_END);
//                } catch (Exception e) {
//                    Log.e(TAG, "出现异常",e);
//                } finally {
//                    if (out != null) {
//                        try {
//                            out.close();
//                        } catch (IOException e) {
//                            //
//                            Log.e(TAG, "出现异常1",e);
//                        }
//                    }
//
//                    if (dataSocket != null) {
//                        try {
//                            dataSocket.close();
//                        } catch (Exception e) {
//                            Log.e(TAG, "出现异常2",e);
//                        }
//                    }
//                }
//
//            }
//        });
//        receiveThread.start();
//
//
////        String proxyUrl = App.getProxy(this).getProxyUrl(url.toString());
////        try {
////            Cache cache = new FileCache(new File(getExternalCacheDir(), getPackageName()));
////            HttpUrlSource source = new HttpUrlSource(url);
////            proxyCache = new HttpProxyCache(source, cache);
////            vv.setVideoPath(proxyCache.getUrl());
////            vv.start();
////        } catch (ProxyCacheException e) {
////            Log.e("log", "Error playing video", e);
////        }
////        HttpProxyCacheServer proxy=getProxy();
////        String proxyUrl=proxy.getProxyUrl(url);
////        final Handler handler = new Handler();
////        Runnable runnable = new Runnable() {
////            public void run() {
////                int duration = vv.getCurrentPosition();
////                if (old_duration == duration && vv.isPlaying()) {
////                    videoMessage.setVisibility(View.VISIBLE);
////                } else {
////                    videoMessage.setVisibility(View.GONE);
////                }
////                old_duration = duration;
////
////                handler.postDelayed(runnable, 1000);
////            }
////        };
////        handler.postDelayed(runnable, 0);
////        vv.setVideoURI(Uri.parse(proxyUrl));
////        vv.requestFocus();
////        vv.start();
//    }
////    private HttpProxyCacheServer getProxy(){
////        return App.getProxy(getApplicationContext());
////
////    }
//private final static int VIDEO_STATE_UPDATE = 0;
//    /**
//     * 缓存准备
//     */
//    private final static int CACHE_VIDEO_READY = 1;
//    /**
//     * 缓存修改
//     */
//    private final static int CACHE_VIDEO_UPDATE = 2;
//    /**
//     * 缓存结束
//     */
//    private final static int CACHE_VIDEO_END = 3;
//    /**
//     * 缓存播放
//     */
//    private final static int CACHE_VIDEO_PLAY = 4;
//
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case VIDEO_STATE_UPDATE:
//                    boolean isPlay=vv.isPlaying();
//                    Log.i(TAG, " update 显示 isPlay="+isPlay);
//                    double cachepercent = readSize * 100.00 / mediaLength * 1.0;
//                    String s = String.format("已缓存: [%.2f%%]", cachepercent);
//                    if (isPlay) {
//                        curPosition = vv.getCurrentPosition();
//                        int duration = vv.getDuration();
//                        duration = duration == 0 ? 1 : duration;
//
//                        double playpercent = curPosition * 100.00 / duration * 1.0;
//
//                        int i = curPosition / 1000;
//                        int hour = i / (60 * 60);
//                        int minute = i / 60 % 60;
//                        int second = i % 60;
//
//                        s += String.format(" 播放: %02d:%02d:%02d [%.2f%%]", hour,
//                                minute, second, playpercent);
//                    }
////
////              tvcache.setText(s);
//                    cachetext.setVisibility(View.GONE);
//                    mHandler.sendEmptyMessageDelayed(VIDEO_STATE_UPDATE, 1000);
//
//
//
//                    break;
//
//                case CACHE_VIDEO_READY:
//                    Log.i(TAG, "缓存准备");
//                    isready = true;
//                    vv.setVideoPath(localUrl);
//                    vv.start();
//
//                    break;
//
//                case CACHE_VIDEO_UPDATE:
//                    Log.i(TAG, "缓存修改"+iserror);
//                    if (iserror) {
//                        vv.setVideoPath(localUrl);
//                        vv.start();
//                        iserror = false;
//                    }
//                    break;
//
//                case CACHE_VIDEO_END:
//                    Log.i(TAG, "缓存结束"+iserror);
//                    if (iserror) {
//
//                        vv.setVideoPath(localUrl);
//                        vv.start();
//                        iserror = false;
//                    }
//                    break;
//                case CACHE_VIDEO_PLAY:
//                    Log.i(TAG, "CACHE_VIDEO_PLAY");
//                    vv.setVideoPath(localUrl);
//                    vv.start();
//                    mHandler.sendEmptyMessageDelayed(CACHE_VIDEO_PLAY, 5000);
//                    break;
//            }
//
//            super.handleMessage(msg);
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        if(vv!=null){
//            vv.stopPlayback();
//        }
//        super.onDestroy();
//    }
//
//}
