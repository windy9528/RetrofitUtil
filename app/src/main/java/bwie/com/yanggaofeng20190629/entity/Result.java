package bwie.com.yanggaofeng20190629.entity;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public class Result<T> {

    private String message = "网络异常";
    private String status = "-1";
    private T result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
