package com.bastion.cyber.exception;

import com.bastion.cyber.constant.ReturnNo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BastException extends RuntimeException {
    private ReturnNo returnNo = ReturnNo.GLOBE_ERROR;
    String msg;

    public BastException(ReturnNo returnNo) {
        super("错误代码：" + returnNo.getCode() + " 错误信息：" + returnNo.getMessage());
        this.returnNo = returnNo;
    }

    public BastException(String msg) {
        super(" 错误信息：" + msg);
        this.msg = msg;
    }
}
