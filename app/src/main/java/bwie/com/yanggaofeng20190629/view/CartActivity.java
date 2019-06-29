package bwie.com.yanggaofeng20190629.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import bwie.com.yanggaofeng20190629.R;
import bwie.com.yanggaofeng20190629.adapter.CartAdapter;
import bwie.com.yanggaofeng20190629.adapter.MyAdapter;
import bwie.com.yanggaofeng20190629.common.ImplView;
import bwie.com.yanggaofeng20190629.common.MyApp;
import bwie.com.yanggaofeng20190629.entity.CartBean;
import bwie.com.yanggaofeng20190629.entity.Goods;
import bwie.com.yanggaofeng20190629.entity.Result;
import bwie.com.yanggaofeng20190629.entity.SecondGoods;
import bwie.com.yanggaofeng20190629.entity.Shops;
import bwie.com.yanggaofeng20190629.green.CartBeanDao;
import bwie.com.yanggaofeng20190629.green.DaoMaster;
import bwie.com.yanggaofeng20190629.presenter.CartPresenter;
import bwie.com.yanggaofeng20190629.presenter.SecondPresenter;

public class CartActivity extends AppCompatActivity implements ImplView<List<Shops>> {

    private Unbinder bind;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.checkboxAll)
    CheckBox checkboxAll;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.go_pay)
    Button goPay;
    private CartAdapter cartAdapter;
    private CartPresenter cartPresenter;
    private CartBeanDao cartBeanDao;
    private CartBean cartBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();

        initData();
    }

    private void initView() {
        bind = ButterKnife.bind(this);
        cartBeanDao = DaoMaster
                .newDevSession(this, "cartBean").getCartBeanDao();
        cartAdapter = new CartAdapter(this);
        cartPresenter = new CartPresenter(this);
        expandableListView.setAdapter(cartAdapter);
    }

    private void initData() {
        if (NetworkUtils.isConnected()) {//连接
            cartPresenter.requestData();  //请求购物车
        } else {
            List<CartBean> list = cartBeanDao.queryBuilder().list();
            for (int i = 0; i < list.size(); i++) {
                CartBean cartBean = list.get(i);
                String json = cartBean.getJson();
                Gson gson = new Gson();
                Type type = new TypeToken<Result<List<Shops>>>() {
                }.getType();
                Result<List<Shops>> result = gson.fromJson(json, type);
                List<Shops> data = result.getResult();
                cartAdapter.addList(data);
                //expandableListView  默认展开
                for (int j = 0; j < cartAdapter.getGroupCount(); j++) {
                    expandableListView.expandGroup(j);
                }
                cartAdapter.notifyDataSetChanged();
            }

        }

        //expandableListView  不可伸展
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        /**
         * 通过接口回调 回显数据
         */
        cartAdapter.setShowTotalPrice(new CartAdapter.ShowTotalPrice() {
            @Override
            public void showPrice(double price, int num, boolean status) {
                totalPrice.setText("￥" + price);
                goPay.setText("去结算(" + num + ")");
                checkboxAll.setChecked(status);
            }
        });
    }

    /**
     * 点击全选  实现全选和反选
     */
    @OnClick(R.id.checkboxAll)
    public void clickCheckAll() {
        List<Shops> shops = cartAdapter.getList();
        for (int i = 0; i < shops.size(); i++) {
            Shops shop = shops.get(i);
            shop.ShopCheck = checkboxAll.isChecked();  //把当前全选框的状态给父级复选框
            for (int j = 0; j < shop.getShoppingCartList().size(); j++) {
                Goods goods = shop.getShoppingCartList().get(j);
                goods.GoodCheck = shop.ShopCheck;
            }
        }
        cartAdapter.calculate();
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void success(Result data) {
        List<Shops> result = (List<Shops>) data.getResult();
        Log.i("windy", "购物车页面父类数据:" + result);
        //Log.i("windy", "=======================华丽分割线=======================");
        //存储数据库
        cartBean = new CartBean();
        String s = new Gson().toJson(data);
        cartBean.setJson(s);
        cartBeanDao.insertOrReplaceInTx(cartBean);

        cartAdapter.addList(result);
        //expandableListView  默认展开
        for (int i = 0; i < cartAdapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(Result result) {
        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
