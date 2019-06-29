package bwie.com.yanggaofeng20190629.entity;

import java.util.List;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public class Shops {

    private String categoryName;
    private List<Goods> shoppingCartList;
    public boolean ShopCheck;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Goods> getShoppingCartList() {
        return shoppingCartList;
    }

    public void setShoppingCartList(List<Goods> shoppingCartList) {
        this.shoppingCartList = shoppingCartList;
    }
}
