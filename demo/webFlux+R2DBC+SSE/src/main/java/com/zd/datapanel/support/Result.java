package com.zd.datapanel.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.*;


/**
 * 结果
 * @author wang xiao
 * @date Created in 17:25 2020/12/18
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Result<R> implements Serializable {

    /**
     * 状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private R data;


    public static <R> Mono<Result<R>> ofSuccessMono (Mono<R> monoData) {
       return buildMono(monoData,0,"");
    }

    public static <R> Mono<Result<R>> ofMono (Mono<R> monoData,int code) {
        return buildMono(monoData,code,"");
    }
    public static <R> Mono<Result<R>> ofMono (int code,String  msg) {
        return buildMono(null,code,msg);
    }

    public static <R> Mono<Result<R>> ofMono (Mono<R> monoData,int code,String  msg) {
        return buildMono(monoData,code,msg);
    }


    public static <R> Mono<Result<List<R>>> ofSuccessFlux (Flux<R> fluxData) {
        return buildFlux(fluxData,0,"");
    }

    public static <R> Mono<Result<List<R>>> ofFlux (Flux<R> fluxData,int code) {
        return buildFlux(fluxData,code,"");
    }
    public static <R> Mono<Result<List<R>>> ofFlux (int code,String  msg) {
        return buildFlux(null,code,msg);
    }

    public static <R> Mono<Result<List<R>>> ofFlux (Flux<R> fluxData,int code,String  msg) {
        return buildFlux(fluxData,code,msg);
    }


    private static <R> Mono<Result<R>> buildMono (Mono<R> monoData, int code, String msg) {
        if (Objects.isNull(monoData)){
            return Mono.<Result<R>>just(new Result<R>().setCode(code).setMsg(msg));
        }
        final Result<R> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return monoData.map(r->{
             result.setData(r);
             return result;
         });
    }

    private static <R> Mono<Result<List<R>>> buildFlux (Flux<R> fluxData, int code, String msg) {
        final Result<List<R>> result = new Result<List<R>>().setCode(code).setMsg(msg);
        if (Objects.isNull(fluxData)){
            return Mono.just(result);
        }
       return fluxData.buffer().map(e->{
            result.setData(e);
            return e;
        }).then(Mono.just(result));

    }

    private int getCode() {
        return code;
    }

    private Result<R> setCode(int code) {
        this.code = code;
        return this;
    }

    private String getMsg() {
        return msg;
    }

    private Result<R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    private R getData() {
        return data;
    }

    private Result<R> setData(R data) {
        this.data = data;
        return this;
    }


}
