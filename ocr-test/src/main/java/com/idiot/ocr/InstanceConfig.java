package com.idiot.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author wang xiao
 * @date Created in 18:01 2021/2/1
 */
@Configuration
public class InstanceConfig {

    @Bean
    public ITesseract iTesseract () {
        // 识别图片的路径（修改为自己的图片路径）
        String path = "C:\\Users\\admin\\Documents\\test.jpg";

        // 语言库位置（修改为跟自己语言库文件夹的路径）
        String lagnguagePath = "D:\\IdeaWorkSpace\\xiao\\ocr-test\\src\\main\\resources\\traineddata";

        File file = new File(path);
        ITesseract instance = new Tesseract();

        //设置训练库的位置
        instance.setDatapath(lagnguagePath);
        instance.setOcrEngineMode(1);


        //chi_sim ：简体中文， eng    根据需求选择语言库
        instance.setLanguage("chi_sim");
        return instance;
    }

}
