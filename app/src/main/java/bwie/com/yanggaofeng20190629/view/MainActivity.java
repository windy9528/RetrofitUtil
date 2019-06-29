package bwie.com.yanggaofeng20190629.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import bwie.com.yanggaofeng20190629.R;
import bwie.com.yanggaofeng20190629.adapter.MyAdapter;
import bwie.com.yanggaofeng20190629.common.ImplView;
import bwie.com.yanggaofeng20190629.common.MyApp;
import bwie.com.yanggaofeng20190629.entity.Result;
import bwie.com.yanggaofeng20190629.entity.SecondGoods;
import bwie.com.yanggaofeng20190629.presenter.SecondPresenter;

public class MainActivity extends AppCompatActivity implements ImplView<List<SecondGoods>> {

    private Unbinder bind;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private SecondPresenter secondPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    private void initView() {
        bind = ButterKnife.bind(this);
        secondPresenter = new SecondPresenter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);
    }

    private void initData() {
        String id = MyApp.getSecondId().getString("id", "1"); //拿到sp里的二级id
        Log.i("windy", "主页面获取到的二级id:" + id);
        secondPresenter.requestData(id, "1", "10");
    }

    @OnClick(R.id.go_cart)
    public void goCart(View v) {
        if (v.getId() == R.id.go_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
    }

    @Override
    public void success(Result data) {
        List<SecondGoods> result = (List<SecondGoods>) data.getResult();
        Log.i("windy", "主页面返回的二级商品信息" + result);
        myAdapter.addList(result);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(Result result) {
        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
