package com.wzy.vitamiot.model;

import java.util.List;

/**
 * Created by zy on 2016/4/9.
 */
public class TVModel {

    /**
     * title : CCTV1
     * url : http://ktv005.cdnak.ds.kylintv.net/nlds/kylin/cctv1hd/as/live/cctv1hd_3.m3u8
     */

    private List<TvListEntity> tv_list;

    public List<TvListEntity> getTv_list() {
        return tv_list;
    }

    public void setTv_list(List<TvListEntity> tv_list) {
        this.tv_list = tv_list;
    }

    public static class TvListEntity {
        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
