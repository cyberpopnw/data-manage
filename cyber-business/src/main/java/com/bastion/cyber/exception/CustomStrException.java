package com.bastion.cyber.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomStrException extends RuntimeException {
    String msg;

    public CustomStrException(String msg) {
        super(" 错误信息：" + msg);
        this.msg = msg;
    }
}
