package com.bastion.cyber.api.user;

import com.bastion.cyber.model.bo.AuthBo;
import com.bastion.cyber.model.dto.LoginUserDto;
import com.bastion.cyber.model.dto.ReportDto;
import io.swagger.annotations.*;

/**
 * Created on 2022/4/7.
 *
 * @author zyg
 */
@Api(value = "用户接口", tags = "用户接口")
public interface UserControllerApi {
    @ApiOperation(value = "拉起登陆页面", notes = "拉起登陆页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publicAddress", value = "钱包地址", required = true, dataTypeClass = String.class, paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object info(String addr);


    @ApiOperation(value = "Oauth2授权认证-普通用户", notes = "Oauth2授权认证-普通用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUserDto", value = "登陆授权信息", required = true, dataTypeClass = LoginUserDto.class, paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object auth(LoginUserDto loginUserDto);

    @ApiOperation(value = "Oauth2授权认证-经销商", notes = "Oauth2授权认证-经销商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addr", value = "钱包地址", required = true, dataTypeClass = String.class, paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object inviter(String addr);

    @ApiOperation(value = "用户详情", notes = "用户详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataTypeClass = String.class, paramType = "header"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object detail(AuthBo userDto);

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "name", value = "用户昵称", required = true, dataTypeClass = String.class, paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object update(AuthBo userDto, String name);

    @ApiOperation(value = "下载App上报", notes = "下载App上报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportDto", value = "上报信息", required = true, dataTypeClass = ReportDto.class, paramType = "body"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 404, message = "资源不存在"),
            @ApiResponse(code = 500, message = "服务器内部错误")
    })
    Object report(ReportDto reportDto);
}
