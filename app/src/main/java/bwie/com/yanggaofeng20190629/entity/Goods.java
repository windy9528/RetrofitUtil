package bwie.com.yanggaofeng20190629.entity;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public class Goods {

    private String commodityName;
    private String pic;
    private int count;
    private double price;
    public boolean GoodCheck;

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
