package com.xiao.demo.modbus;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author wangxiao@aibaoxun.com
 * @date 2021/5/9
 */
public class ModbusTransportServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ModbusTransportContext context;

    public ModbusTransportServerInitializer(ModbusTransportContext context) {
        this.context = context;
    }

    /**
     * override method  设置encoder,decoder,ssl,handler
     */
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        ModbusTransportHandler handler = new ModbusTransportHandler(context);
        pipeline.addLast(handler);
        ch.closeFuture().addListener(handler);
    }
}