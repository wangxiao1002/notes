package com.idiot.esdemo;

import com.idiot.esdemo.entity.TestUser;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@SpringBootTest
class EsDemoApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testESClient(){
        System.out.println(client);
    }

    // index 的操作
    @Test
    public void testDelIndex() throws IOException {
        // 操作索引的对象
        IndicesClient indices = client.indices();
        // 删除索引的请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test");
        // 删除索引
        AcknowledgedResponse response = indices.delete(deleteIndexRequest,RequestOptions.DEFAULT);
        // 得到响应
        boolean b = response.isAcknowledged();
        System.out.println(b);
    }

    @Test
    public void testAddIndex() throws IOException {
        // 操作索引的对象
        IndicesClient indices = client.indices();
        // 新建索引的请求
        CreateIndexRequest request = new CreateIndexRequest("source");
        request.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
        // 创建映射
        request.mapping("{\"properties\":{\"description\":{\"type\":\"text\"},\"name\":{\"type\":\"text\"},\"pic\":{\"type\":\"text\",\"index\":false},\"price\":{\"type\":\"scaled_float\",\"scaling_factor\":100},\"studymodel\":{\"type\":\"keyword\"},\"timestamp\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"}}}",XContentType.JSON);

        // 执行创建操作
        CreateIndexResponse response = indices.create(request,RequestOptions.DEFAULT);
        // 得到响应
        boolean b = response.isAcknowledged();
        System.out.println(b);
    }

    @Test
    public void add () throws IOException {
        IndexRequest indexRequest = new IndexRequest("source");
//        indexRequest.source("{\"name\":\"Bootstrap开发\",\"description\":\"Bootstrap是由Twitter推出的一个前台页面开发框架，是一个非常流行的开发框架，此框架集成了多种页面效果。此开发框架包含了大量的CSS、JS程序代码，可以帮助开发者（尤其是不擅长页面开发的程序人员）轻松的实现一个不受浏览器限制的精美界面效果。\",\"studymodel\":\"201002\",\"price\":38.6,\"timestamp\":\"2018-04-25 19:11:35\",\"pic\":\"group1/M00/00/00/wKhlQFs6RCeAY0pHAAJx5ZjNDEM428.jpg\"}",XContentType.JSON);
//        IndexResponse response = client.index(indexRequest,RequestOptions.DEFAULT);
//        System.out.println(response.toString());
        XContentBuilder content = XContentFactory.jsonBuilder()
                .startObject()
                .field("name", "java 开发")
                .field("description", "世界上最优美的语言")
                .field("studymodel", "2001")
                .field("price", 12)
                .field("timestamp", Instant.now().getEpochSecond())
                .field("pic", "/pic/url")
                .endObject();
        indexRequest.source(content);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @Test
    public void testSearchAll() throws IOException, ParseException {
        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("source");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索方式
        // matchAllQuery搜索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // fixme 精确匹配
     //   searchSourceBuilder.query(QueryBuilders.termQuery("studymodel", "201002"));
     //  searchSourceBuilder.query(QueryBuilders.termQuery("_id", "1"));;
        // fixme 全文检索
        // searchSourceBuilder.query(QueryBuilders.matchQuery("description", "Spring开发框架").minimumShouldMatch("70%"));
        // fixme 多字段 联合搜索
        // searchSourceBuilder.query(QueryBuilders.multiMatchQuery("Spring开发框架", "name", "description").minimumShouldMatch("70%"));
        // fixme 联合查询 设置权重
        // searchSourceBuilder.query(QueryBuilders.multiMatchQuery("Spring开发框架", "name", "description").field("name", 10)); // 设置 name 10倍权重
        // 设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        // 向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        // fixme 分页
       // searchSourceBuilder.from(1);
       // searchSourceBuilder.size(1);
        // 执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配到的总记录数
        long totalHits = hits.getTotalHits().value;
        System.out.println("total is "+ totalHits);
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();


        printData(searchHits);

    }

    // 排序查询
    @Test
    public void testSort() throws IOException, ParseException {
        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("source");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索方式
        // 添加条件到布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 通过布尔查询来构造过滤查询
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        // 将查询条件封装给查询对象
        searchSourceBuilder.query(boolQueryBuilder);
        // 向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);

        // 设置排序规则
        searchSourceBuilder.sort("studymodel", SortOrder.DESC); // 第一排序规则
        searchSourceBuilder.sort("price", SortOrder.ASC); // 第二排序规则

        // 执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配到的总记录数
        long totalHits = hits.getTotalHits().value;
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 输出
        printData(searchHits);
    }

    // 高亮查询
    @Test
    public void testHighLight() throws IOException, ParseException {
        // 搜索请求对象
        SearchRequest searchRequest = new SearchRequest("source");

        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索方式
        // 首先构造多关键字查询条件
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery("Spring框架", "name", "description").field("name", 10);
        // 添加条件到布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(matchQueryBuilder);
        // 通过布尔查询来构造过滤查询
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        // 将查询条件封装给查询对象
        searchSourceBuilder.query(boolQueryBuilder);


        // 高亮查询
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<em>"); // 高亮前缀
        highlightBuilder.postTags("</em>"); // 高亮后缀
        highlightBuilder.fields().add(new HighlightBuilder.Field("name")); // 高亮字段
        // 添加高亮查询条件到搜索源
        searchSourceBuilder.highlighter(highlightBuilder);

        // ***********************

        // 设置源字段过虑,第一个参数结果集包括哪些字段，第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        // 向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索,向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配到的总记录数
        long totalHits = hits.getTotalHits().value;
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 日期格式化对象
        printData(searchHits);
    }



    @Test
    void contextLoads() {
    }


    public void printData (SearchHit[] searchHits){

        for(SearchHit hit:searchHits){
            // 文档的主键
            String id = hit.getId();
            // 源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 由于前边设置了源文档字段过虑，这时description是取不到的
            String description = (String) sourceAsMap.get("description");
            // 学习模式
            String studymodel = (String) sourceAsMap.get("studymodel");
            // 价格
            Object price = sourceAsMap.get("price");
            // 日期
            Object timestamp = sourceAsMap.get("timestamp");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println("你看不见我，看不见我~" + description);
            System.out.println(price);
            System.out.println(timestamp);
        }
    }




}
