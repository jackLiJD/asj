package com.ald.asjauthlib.event;

/**
 * Created by Yangyang on 2018/4/16.
 * desc:
 */

public class Event<T> {
    private int code;
    private T data;

    public Event() {
    }

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, T data) {
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
