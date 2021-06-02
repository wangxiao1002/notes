package com.zd.datapanel.handlers;

import com.zd.datapanel.scheduleds.DataTask;
import com.zd.datapanel.vo.SaleDataVO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author wang xiao
 * @date Created in 11:16 2021/1/28
 */
@RestController
@RequestMapping("/msg")
public class MessageHandler {

    @GetMapping
    @CrossOrigin("*")
    public Flux<SaleDataVO> msg () {
       return Flux.interval(Duration.ofMinutes(1)).map(s->DataTask.getSaleDataVO());
    }
}
