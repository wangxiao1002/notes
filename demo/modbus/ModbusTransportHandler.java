package com.xiao.demo.modbus;

import com.xiao.demo.modbus.model.ModbusMsg;
import com.xiao.demo.modbus.util.ByteMsgUtil;
import com.xiao.demo.modbus.util.SchedulerComponent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 *  modbus rtu handler
 *  询问帧：
 *   | 地址码| 功能码 | 寄存器地址 | 寄存器数量 | 校验码|
 *   | 01 (1字节)  | 03   | 00 48 (2字节)   | 00 80    | CF 2A|
 *  响应帧:
 *   | 地址码| 功能码 | 寄存器个数 | 数据字段 。。。 | 校验码|
 *    | 01 (1字节)  | 03   | (1字节)   | 00 80 (2字节) 。。。   | CF 2A|
 *  @author wangxiao@aibaixun.com
 *  @date 2021/5/9
 */
@Slf4j
public class ModbusTransportHandler extends SimpleChannelInboundHandler<ByteBuf> implements GenericFutureListener<Future<? super Void>> {

    private final ModbusTransportContext context;

    private final char defaultSymbol = '0';

    private UUID sessionId;

    private final  int connectLength;

    private final SchedulerComponent scheduler;

    public ModbusTransportHandler(ModbusTransportContext context) {
        this.context = context;
        this.connectLength = 8;
        this.scheduler = context.getScheduler();
        this.sessionId = UUID.randomUUID();
    }


    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        log.info("msg is reviced");
        String msg = handByteMsg(byteBuf);
        try {
            processModbusMsg(channelHandlerContext,msg);
        }finally {

        }
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {
       log.info("complete");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("active");
    }

    /**
     *  处理modbus 消息
     *  @author wangxiao@aibaixun.com
     *  @date 2021/5/10
     */
    private void processModbusMsg (final ChannelHandlerContext ctx, final String msg) {
        if (Objects.isNull(msg)){
            ctx.close();
        }
        final int msgLength = msg.length();
        if ( msgLength == connectLength){
            processConnect(ctx, msg);
        }else {
            processAckMsg(ctx, msg);
        }
    }

    /**
     *  modbus 连接
     *  @author wangxiao@aibaixun.com
     *  @date 2021/5/10
     */
    private void processConnect(ChannelHandlerContext ctx, String msg) {
        log.info("[{}] Processing connect msg for client msg: {}!", sessionId, msg);
        Long connectId = Long.parseLong(msg,16);
        // 设备查询 密钥是否满足
        /**
         *
         *  1. 设备连接 后单独发送ask
         */
        // 资产查询 资产的服务端属性是否满足
        /**
         *
         *  资产连接 查询当下资产下的 设备 异步发送设备ask
         */
        scheduler.scheduleAtFixedRate(() -> sendAskMsg(ctx,1,3,0,2,50187), 60,60, TimeUnit.SECONDS);
        ByteBuf byteBuf = null;
        for (int i = 0; i < byteBuf.readableBytes(); i++) {
            var a = byteBuf.readByte();
        }
    }

    private void  processAuth () {}

    /**
     * 关闭连接
     */
    private void processDisconnect(ChannelHandlerContext ctx) {
        ctx.close();
        log.info("[{}] Client disconnected!", sessionId);
        doDisconnect();
    }


    /**
     *  modbus 上行数据
     *  @author wangxiao@aibaixun.com
     *  @date 2021/5/10
     */
    private void processAckMsg(ChannelHandlerContext ctx, String msg) {
        log.info("[{}] Processing ack msg for client msg: {}!", sessionId, msg);
        ModbusMsg modbusMsg = new ModbusMsg(msg).calc();
        if (!modbusMsg.crc()) {
            log.info("[{}] crc calc is error",sessionId);
        }
    }



    private void processDevicePublish(ChannelHandlerContext ctx,ModbusMsg msg){}


    private void processAssertPublish(ChannelHandlerContext ctx,ModbusMsg msg){}

    private void doDisconnect() {}

    /**
     *  发送问询帧
     *  @author wangxiao@aibaixun.com
     *  @date 2021/5/10
     */
    private void sendAskMsg (final  ChannelHandlerContext context,int deviceAddr,int functionCode, int startAddr,int dataLength,int checkCode){
        final String deviceAddrStr =ByteMsgUtil.fill(Integer.toHexString(deviceAddr),2,defaultSymbol);
        final String functionCodeStr =ByteMsgUtil.fill(Integer.toHexString(functionCode),2,defaultSymbol);
        final String startAddrStr =ByteMsgUtil.fill(Integer.toHexString(startAddr),4,defaultSymbol);
        final String dataLengthStr =ByteMsgUtil.fill(Integer.toHexString(dataLength),4,defaultSymbol);
        final String checkCodeStr =ByteMsgUtil.fill(Integer.toHexString(checkCode),4,defaultSymbol);
        ack(context,deviceAddrStr+functionCodeStr+startAddrStr+dataLengthStr+checkCodeStr);
    }


    private void ack(final  ChannelHandlerContext context,final String sendMsg) {
        try {
            log.info("[{}] send msg to device msg is [{}]",sessionId,sendMsg);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(ByteMsgUtil.hexString2Bytes(sendMsg));
            context.writeAndFlush(buffer).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("send success");
                } else {
                    log.info("send fail");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[{}] sendToDevice exception msg is :[{}]",sessionId,e.getMessage());
        }
    }


    private String  handByteMsg (ByteBuf msg) {
        byte [] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        return ByteMsgUtil.receiveHexToString(bytes);
    }






}
