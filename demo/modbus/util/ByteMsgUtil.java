package com.xiao.demo.modbus.util;


import lombok.extern.slf4j.Slf4j;


/**
 * 字节消息 处理类
 * @author wangxiao@aibaoxun.com
 * @date 2021/5/9
 */
@Slf4j
public final class ByteMsgUtil {

    private ByteMsgUtil()  {
    }
    /**
     * 字节数组转16进制字符串
     * @param b 字节数组
     * @return 16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder result = new StringBuilder();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }
    /**
     *  16进制字符串转字节数组
     * @param src  16进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }
    /**
     * 字符串转16进制字符串
     * @param strPart  字符串
     * @return 16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }
    /**
     * 16进制字符串转字符串
     * @param src 16进制字符串
     * @return 字符串
     */
    public static String hexString2String(String src) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < src.length() / 2; i++) {
            temp.append((char) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue());
        }
        return temp.toString();
    }









    /**
     *  Convert byte[] to hex string.这里我们可以将byte转换成int
     * @param src byte[] data
     * @return hex string
     */
    public static String bytes2Str(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * @param by byte array
     * @return 接收字节数据并转为16进制字符串
     */
    public static String receiveHexToString(byte[] by) {
        try {
            return bytes2Str(by);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * "7dd",4,'0'==>"07dd"
     * @param input 需要补位的字符串
     * @param size 补位后的最终长度
     * @param symbol 按symbol补充 如'0'
     */
    public static String fill(String input, int size, char symbol) {
        StringBuilder inputBuilder = new StringBuilder(input);
        while (inputBuilder.length() < size) {
            inputBuilder.insert(0, symbol);
        }
        input = inputBuilder.toString();
        return input;
    }






}
