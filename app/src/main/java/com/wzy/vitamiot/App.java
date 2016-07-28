//package com.wzy.vitamiot;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.danikula.videocache.HttpProxyCacheServer;
//
///**
// * Created by zy on 2016/4/7.
// */
//public class App extends Application {
//    private HttpProxyCacheServer proxy;
//
//    public static HttpProxyCacheServer getProxy(Context context) {
//        App app = (App) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }
//
//    private HttpProxyCacheServer newProxy() {
////        return new HttpProxyCacheServer(this);
//        return new HttpProxyCacheServer.Builder(this)
//                .maxCacheSize( 1024 * 1024)       // 1 Gb for cache
//                .build();
//    }
//}
