package com.zhuchao.android.tianpu.recycler;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuchao.android.tianpu.R;

/**
 * Created by Oracle on 2017/11/29.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum ITEM_TYPE {
        ITEM_TYPE_1, ITEM_TYPE_2
    }

    private LayoutInflater layoutInflater;
    private Context context;
    private SparseArray<Data> dataSparseArray;

    public HomeAdapter(Context context, SparseArray<Data> dataSparseArray) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.dataSparseArray = dataSparseArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new HomeItemHolder(
                        layoutInflater.inflate(R.layout.home_item, parent, false));
                break;
            case 1:
                holder = new HomeItemHolder(
                        layoutInflater.inflate(R.layout.home_item1, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeItemHolder) {

        } else if (holder instanceof HomeItemHolder1) {

        }
    }

    @Override
    public int getItemCount() {
        return dataSparseArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        Data data = dataSparseArray.get(position);
        return data.getType();
    }

    public static class HomeItemHolder extends RecyclerView.ViewHolder {

        public HomeItemHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HomeItemHolder1 extends RecyclerView.ViewHolder {

        public HomeItemHolder1(View itemView) {
            super(itemView);
        }
    }
}
