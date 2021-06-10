package com.xiao.demo.modbus;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Modbus RTU
 * @author wangxiao@aibaoxun.com
 * @date 2021/5/9
 */
@Service("ModbusTransportService")
@ConditionalOnExpression("'${service.type:null}'=='dl-transport' || ('${service.type:null}'=='monolith' && '${transport.api_enabled:true}'=='true' && '${transport.modbus.enabled}'=='true')")
@Slf4j
public class ModbusTransportService {

    @Value("${transport.modbus.bind_address}")
    private String host;
    @Value("${transport.modbus.bind_port}")
    private Integer port;
    @Value("${transport.modbus.netty.leak_detector_level}")
    private String leakDetectorLevel;
    @Value("${transport.modbus.netty.boss_group_thread_count}")
    private Integer bossGroupThreadCount;
    @Value("${transport.modbus.netty.worker_group_thread_count}")
    private Integer workerGroupThreadCount;
    @Value("${transport.modbus.netty.so_keep_alive}")
    private boolean keepAlive;


    @Autowired
    private ModbusTransportContext context;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    /**
     * 初始化netty 服务
     * @author wangxiao@aibaixun.com
     */
    @PostConstruct
    public void init() throws Exception {
        log.info("Setting resource leak detector level to {}", leakDetectorLevel);
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(leakDetectorLevel.toUpperCase()));

        log.info("Starting Modbus RTU transport...");
        bossGroup = new NioEventLoopGroup(bossGroupThreadCount);
        workerGroup = new NioEventLoopGroup(workerGroupThreadCount);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ModbusTransportServerInitializer(context))
                .childOption(ChannelOption.SO_KEEPALIVE, keepAlive);
        serverChannel = b.bind(host, port).sync().channel();
        log.info("Modbus RTU transport started!");
    }

    /**
     *  shutdown
     *  @author wangxiao@aibaixun.com
     */
    @PreDestroy
    public void shutdown() throws InterruptedException {
        log.info("Stopping Modbus RTU transport!");
        try {
            serverChannel.close().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        log.info("Modbus RTU transport stopped!");
    }

}
