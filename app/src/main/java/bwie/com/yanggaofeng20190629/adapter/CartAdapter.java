package bwie.com.yanggaofeng20190629.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bwie.com.yanggaofeng20190629.R;
import bwie.com.yanggaofeng20190629.entity.Goods;
import bwie.com.yanggaofeng20190629.entity.Shops;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public class CartAdapter extends BaseExpandableListAdapter {

    private List<Shops> shopsList;
    private Context context;
    private ShowTotalPrice showTotalPrice;

    public void setShowTotalPrice(ShowTotalPrice showTotalPrice) {
        this.showTotalPrice = showTotalPrice;
    }

    public interface ShowTotalPrice {
        void showPrice(double totalPrice, int num, boolean status);
    }

    public List<Shops> getList() {
        return shopsList;
    }

    public CartAdapter(Context context) {
        this.context = context;
        shopsList = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return shopsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return shopsList.get(groupPosition).getShoppingCartList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return shopsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return shopsList.get(groupPosition).getShoppingCartList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ShopViewHolder shopViewHolder = null;
        if (convertView == null) {
            shopViewHolder = new ShopViewHolder();
            convertView = View.inflate(context, R.layout.cart_shop, null);
            shopViewHolder.tvName = convertView.findViewById(R.id.tv_name);  //商家名称
            shopViewHolder.shopCheckBox = convertView.findViewById(R.id.shop_checkbox);
            convertView.setTag(shopViewHolder);
        } else {
            shopViewHolder = (ShopViewHolder) convertView.getTag();
        }
        Shops shops = shopsList.get(groupPosition);
        shopViewHolder.tvName.setText(shops.getCategoryName());

        shopViewHolder.shopCheckBox.setChecked(shops.ShopCheck);
        shopViewHolder.shopCheckBox.setTag(shops);

        shopViewHolder.shopCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shops s = (Shops) v.getTag();
                s.ShopCheck = ((CheckBox) v).isChecked();
                if (s.ShopCheck) {
                    for (int i = 0; i < shopsList.get(groupPosition).getShoppingCartList().size(); i++) {
                        s.getShoppingCartList().get(i).GoodCheck = true;
                    }
                } else {
                    for (int i = 0; i < shopsList.get(groupPosition).getShoppingCartList().size(); i++) {
                        s.getShoppingCartList().get(i).GoodCheck = false;
                    }
                }
                calculate();//计算价格
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        GoodViewHolder goodViewHolder = null;
        if (convertView == null) {
            goodViewHolder = new GoodViewHolder();
            convertView = View.inflate(context, R.layout.cart_goods, null);
            goodViewHolder.tvName = convertView.findViewById(R.id.tv_name);  //商品名称
            goodViewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);  //商品图片
            goodViewHolder.tvPrice = convertView.findViewById(R.id.tv_price);  //商品价格
            goodViewHolder.tvNum = convertView.findViewById(R.id.tv_num);  //商品数量
            goodViewHolder.add = convertView.findViewById(R.id.add);
            goodViewHolder.minus = convertView.findViewById(R.id.minus);
            goodViewHolder.goodCheckBox = convertView.findViewById(R.id.good_checkbox);
            convertView.setTag(goodViewHolder);
        } else {
            goodViewHolder = (GoodViewHolder) convertView.getTag();
        }
        Goods goods = shopsList.get(groupPosition).getShoppingCartList().get(childPosition);
        Glide.with(context).load(goods.getPic()).into(goodViewHolder.ivIcon);
        goodViewHolder.tvName.setText(goods.getCommodityName());
        goodViewHolder.tvPrice.setText("￥" + goods.getPrice());
        goodViewHolder.tvNum.setText(String.valueOf(goods.getCount()));

        goodViewHolder.goodCheckBox.setChecked(goods.GoodCheck);

        goodViewHolder.goodCheckBox.setTag(goods);
        goodViewHolder.add.setTag(goods);
        goodViewHolder.minus.setTag(goods);

        goodViewHolder.goodCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goods g = (Goods) v.getTag();
                g.GoodCheck = ((CheckBox) v).isChecked();
                calculate();//计算价格
                notifyDataSetChanged();
            }
        });

        goodViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goods g = (Goods) v.getTag();
                int count = g.getCount();
                count++;
                g.setCount(count);
                calculate();//计算价格
                notifyDataSetChanged();
            }
        });

        goodViewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goods g = (Goods) v.getTag();
                int count = g.getCount();
                if (count > 1) {
                    count--;
                } else {
                    Toast.makeText(context, "不能减了", Toast.LENGTH_SHORT).show();
                }
                g.setCount(count);
                calculate();//计算价格
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ShopViewHolder {
        TextView tvName;
        CheckBox shopCheckBox;
    }

    class GoodViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvPrice;
        Button minus;
        TextView tvNum;
        Button add;
        CheckBox goodCheckBox;
    }


    public void addList(List<Shops> result) {
        if (result != null) {
            shopsList.addAll(result);
        }
    }

    /**
     * 计算价格操作
     */
    public void calculate() {
        double total = 0; //总价
        int num = 0;  //数量
        boolean status = true; //状态
        for (int i = 0; i < shopsList.size(); i++) {
            Shops shop = shopsList.get(i);
            shop.ShopCheck = true;
            for (int j = 0; j < shop.getShoppingCartList().size(); j++) {
                Goods goods = shop.getShoppingCartList().get(j);
                shop.ShopCheck = shop.ShopCheck && goods.GoodCheck; //得出父级复选框的状态
                if (goods.GoodCheck) {
                    total = total + goods.getCount() * goods.getPrice();
                    num = num + goods.getCount();
                }
            }
            if (!shop.ShopCheck) {
                status = false;
            }
            if (showTotalPrice != null) {
                showTotalPrice.showPrice(total, num, status);
            }
        }
    }
}
