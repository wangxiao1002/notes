package com.xiao.demo.modbus.model;

import com.xiao.demo.modbus.util.ByteMsgUtil;
import lombok.Getter;

/**
 * @author wangxiao@aibaoxun.com
 * @date 2021/5/10
 */
public class ModbusMsg {


    private final String sourceStr;

    private  boolean calc;


    public final static int DEFAULT_BYTE_LENGTH = 2;

    private int calcIndex = 0;

    public ModbusMsg(String sourceStr) {
        this.sourceStr = sourceStr;
        this.calc = false;
        calcIndex = 0;
    }


    @Getter
    private String addrCode;

    @Getter
    private String funCode;

    @Getter
    private String dataLength;

    @Getter
    private String data;

    @Getter
    private String checkCode;


    public ModbusMsg calc (){
        if (!calc){
            addrCode = sourceStr.substring(calcIndex,calcIndex+=DEFAULT_BYTE_LENGTH);
            funCode = sourceStr.substring(calcIndex,calcIndex+=DEFAULT_BYTE_LENGTH);
            dataLength = sourceStr.substring(calcIndex,calcIndex+=DEFAULT_BYTE_LENGTH);
            int dataEndIndex = sourceStr.length()-4;
            data = sourceStr.substring(calcIndex,dataEndIndex);
            checkCode = sourceStr.substring(dataEndIndex);
            calc = true;
            calcIndex = dataEndIndex+4;
        }
        return this;
    }

    public boolean crc (){
       return crc(addrCode+funCode+dataLength+data).equals(checkCode);
    }

    /**
     * 计算CRC16校验码
     * @param data String16进制字符串
     * @return {@link String} 校验码
     */
    public final String crc(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        return crc(para);
    }


    private  String crc(byte[] bytes) {
        int crc = 0x0000ffff;
        int polynomial = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            crc ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((crc & 0x00000001) != 0) {
                    crc >>= 1;
                    crc ^= polynomial;
                } else {
                    crc >>= 1;
                }
            }
        }
        String result = Integer.toHexString(crc).toUpperCase();
        if (result.length() != DEFAULT_BYTE_LENGTH*DEFAULT_BYTE_LENGTH) {
            StringBuilder sb = new StringBuilder("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        return result.substring(2, 4)  + result.substring(0, 2);
    }
}
