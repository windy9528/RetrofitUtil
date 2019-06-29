package bwie.com.yanggaofeng20190629.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * date:2019/6/29
 * name:windy
 * function:  存储数据库
 */
@Entity
public class CartBean {

    @Id(autoincrement = true)
    private long id;
    private String json;
    @Generated(hash = 1160930839)
    public CartBean(long id, String json) {
        this.id = id;
        this.json = json;
    }
    @Generated(hash = 1446963280)
    public CartBean() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getJson() {
        return this.json;
    }
    public void setJson(String json) {
        this.json = json;
    }
}
