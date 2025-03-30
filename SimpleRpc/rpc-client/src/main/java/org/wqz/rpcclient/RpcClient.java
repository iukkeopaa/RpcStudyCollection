package org.wqz.rpcclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wqz.rpcclient.codec.RpcDecoder;
import org.wqz.rpcclient.codec.RpcEncoder;
import org.wqz.rpccommon.bean.RpcRequest;
import org.wqz.rpccommon.bean.RpcResponse;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/3/29 下午9:33
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger Loggger = LoggerFactory.getLogger(RpcClient.class);

    private final String host;

    private final int port;

    private RpcResponse rpcResponse;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {

        this.rpcResponse = rpcResponse;

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Loggger.error("api caught exception", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest request){


        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap =new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSctpChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {


                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class));
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                    pipeline.addLast(RpcClient.this);

                }
            });

            bootstrap.option(ChannelOption.TCP_NODELAY,true);

            ChannelFuture future = bootstrap.connect(host, port).sync();


            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();

            channel.closeFuture().sync();

            return rpcResponse;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            group.shutdownGracefully();
        }


    }

}
