package com.ald.asjauthlib.event;

/**
 * Created by Yangyang on 2018/4/16.
 * desc:有时候粘性的用同一个event会干掉前一个,故使用第二个在发射
 */

public class SecondEvent<T> {
    private int code;
    private T data;

    public SecondEvent() {
    }

    public SecondEvent(int code) {
        this.code = code;
    }

    public SecondEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
