package com.bastion.cyber.api.error;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "重写SpringBoot错误重定向页面",tags = "重写SpringBoot错误重定向页面")
public interface ErrorControllerApi {
    @ApiOperation(value = "404返回")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object error_404();

    @ApiOperation(value = "500返回")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object error_500();
}
