package bwie.com.yanggaofeng20190629.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import bwie.com.yanggaofeng20190629.R;
import bwie.com.yanggaofeng20190629.common.ImplView;
import bwie.com.yanggaofeng20190629.entity.Result;
import bwie.com.yanggaofeng20190629.entity.SecondGoods;
import bwie.com.yanggaofeng20190629.entity.SyncGoods;
import bwie.com.yanggaofeng20190629.presenter.SyncPresenter;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Viewholder> {

    private Context context;
    private List<SecondGoods> list;

    public MyAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_goods, viewGroup, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        final SecondGoods secondGoods = list.get(i);

        Glide.with(context)
                .load(secondGoods.getMasterPic())
                .into(viewholder.ivIcon);
        viewholder.tvName.setText(secondGoods.getCommodityName());

        //条目点击 同步购物车
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int commodityId = secondGoods.getCommodityId();
                int saleNum = secondGoods.getSaleNum();
                SyncGoods syncGoods = new SyncGoods();
                syncGoods.setCommodityId(commodityId);
                syncGoods.setCount(saleNum);
                List<SyncGoods> list = new ArrayList<>();
                list.add(syncGoods);
                String s = new Gson().toJson(list);
                //同步购物车
                new SyncPresenter(new ImplView() {
                    @Override
                    public void success(Result result) {
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(Result result) {
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).requestData(s);
                Log.i("windy", "=======================华丽分割线=======================");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<SecondGoods> result) {
        if (result != null) {
            list.addAll(result);
        }
    }

    class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
