package com.wzy.vitamiot.adapters;

import android.content.Context;
import android.widget.TextView;

import com.wzy.vitamiot.R;
import com.wzy.vitamiot.model.TVModel;

import java.util.List;

/**
 * Created by zy on 2016/4/9.
 */
public class TVAdapter extends MyBaseAdapter<TVModel.TvListEntity> {
    public TVAdapter(List<TVModel.TvListEntity> data, Context context, int layoutRes) {
        super(data, context, layoutRes);
    }

    @Override
    public void bindData(ViewHolder holder, TVModel.TvListEntity tvListEntity) {
        TextView title = (TextView)holder.getView(R.id.tv);
        title.setText(tvListEntity.getTitle());
    }
}
