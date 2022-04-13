package com.bastion.cyber.exception;


import com.bastion.cyber.constant.ReturnNo;

public class ExceptionCast {
    public static void cast(ReturnNo resultCode) {
        throw new BastException(resultCode);
    }
    public static void castStr(String msg) {
        throw new CustomStrException(msg);
    }
}
