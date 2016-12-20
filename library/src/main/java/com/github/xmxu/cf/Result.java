package com.github.xmxu.cf;

/**
 * 回调实体
 * Created by Simon on 2016/11/22.
 */

public class Result {

    public static class Code {
        public static final int CANCEL = Integer.MIN_VALUE + 1;
        public static final int OK = 0;
        public static final int NO_CLIENT = Integer.MIN_VALUE + 2;
    }

    protected int errorCode = Code.CANCEL;
    protected String errorMsg;
    protected Object tag;

    public Result() {
    }

    public Result(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Result(int errorCode, String errorMsg, Object tag) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.tag = tag;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", errorMsg='").append(errorMsg).append('\'');
        sb.append(", tag=").append(tag);
        sb.append('}');
        return sb.toString();
    }
}
