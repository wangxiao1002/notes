package com.xiao.demo.modbus; /**
 * @author wangxiao@aibaoxun.com
 * @date 2021/5/9
 */

import com.xiao.demo.modbus.util.SchedulerComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 *  modbus 上下文
 *  @author wangxiao@aibaixun.com
 *  @date 2021/5/9
 */
@Slf4j
@Component
@ConditionalOnExpression("'${service.type:null}'=='dl-transport' || ('${service.type:null}'=='monolith' && '${transport.api_enabled:true}'=='true' && '${transport.modbus.enabled}'=='true')")
public class ModbusTransportContext {

    @Getter
    @Autowired
    private SchedulerComponent scheduler;
}
