package com.zd.datapanel.support;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * @author wang xiao
 * @date Created in 10:06 2021/1/22
 */
public class JApiDocsMain {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("D:\\IdeaWorkSpace\\zd\\data-panel");
        config.setProjectName("数据看板接口文档");
        config.setApiVersion("V1.0");
        config.setDocsPath("D:\\IdeaWorkSpace\\zd\\data-panel\\docs");
        config.setAutoGenerate(Boolean.TRUE);
        Docs.buildHtmlDocs(config);
    }
}
