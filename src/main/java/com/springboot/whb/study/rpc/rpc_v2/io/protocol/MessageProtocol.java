package com.springboot.whb.study.rpc.rpc_v2.io.protocol;

import com.springboot.whb.study.rpc.rpc_v2.core.RpcRequest;
import com.springboot.whb.study.rpc.rpc_v2.core.RpcResponse;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author: whb
 * @date: 2019/8/19 9:59
 * @description: 请求、应答 解析和反解析，包含了序列化以及反序列化操作
 */
public interface MessageProtocol {

    /**
     * 服务端解析从网络传输的数据，转变成request对象
     *
     * @param inputStream
     * @return
     */
    RpcRequest serviceToRequest(InputStream inputStream);

    /**
     * 服务端把计算的结果包装好，通过输出流返回给客户端
     *
     * @param response
     * @param outputStream
     * @param <T>
     */
    <T> void serviceGetResponse(RpcResponse<T> response, OutputStream outputStream);

    /**
     * 客户端把请求拼接好，通过输出流发送到服务端
     *
     * @param request
     * @param outputStream
     */
    void clientToRequest(RpcRequest request, OutputStream outputStream);

    /**
     * 客户端接收到服务端响应的结果，转变成response对象
     *
     * @param inputStream
     */
    <T> RpcResponse<T> clientGetResponse(InputStream inputStream);
}
