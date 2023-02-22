package com.atguigu.test;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.pojo.Student;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.IOException;

public class EsTest {
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    //创建索引
    @Test
    public void createIndex(){
        //创建CreateIndexRequest对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("student");
        //设置索引的映射
        try {
            createIndexRequest.mapping("{\n" +
                    "    \"properties\": {\n" +
                    "      \"name\": {\n" +
                    "        \"type\": \"keyword\",\n" +
                    "        \"index\": true,\n" +
                    "        \"store\": true\n" +
                    "      },\n" +
                    "      \"age\": {\n" +
                    "        \"type\": \"integer\",\n" +
                    "        \"index\": true,\n" +
                    "        \"store\": true\n" +
                    "      },\n" +
                    "      \"remark\": {\n" +
                    "        \"type\": \"text\",\n" +
                    "        \"index\": true,\n" +
                    "        \"store\": true,\n" +
                    "        \"analyzer\": \"ik_max_word\",\n" +
                    "        \"search_analyzer\": \"ik_max_word\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }", XContentType.JSON);
            //创建索引
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            //获取ack
             boolean acknowledged = createIndexResponse.isAcknowledged();
            System.out.println("acknowledged = " + acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //查询索引
    @Test
    public void testGetIndex(){
        //创建GetIndexRequest对象
        GetIndexRequest getIndexRequest = new GetIndexRequest("student");
        try {
            //查询索引库
            GetIndexResponse getIndexResponse = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            //获取结果
            System.out.println("getIndexResponse.getSettings() = " + getIndexResponse.getSettings());
            System.out.println("getIndexResponse.getMappings() = " + getIndexResponse.getMappings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //测试删除索引
    @Test
    public void testDeleteIndex(){
        //创建DeleteIndexRequest对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test");
        //调用客户端的方法删除数据库
        AcknowledgedResponse acknowledgedResponse = null;
        try {
            acknowledgedResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            System.out.println("acknowledgedResponse.isAcknowledged() = " + acknowledgedResponse.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //测试添加文档（索引文档）
    @Test
    public void testIndexDoc() throws IOException {
        //创建IndexRequest对象
        IndexRequest indexRequest = new IndexRequest("student");
        //设置文档的id
        indexRequest.id("1");
        //创建Student对象
        Student student = new Student();
        //设置属性值
        student.setAge(23);
        student.setName("张三");
        student.setRemark("三三三");
        //将Student对象转换为JSON格式的字符串
        String jsonString = JSONObject.toJSONString(student);
        //添加文档数据
        indexRequest.source(jsonString,XContentType.JSON);
        //调用客户端添加
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println("result = " + result);
    }

    //测试查询文档
    @Test
    public void testGetDoc() throws IOException {
        //创建
        GetRequest getRequest = new GetRequest("student","1");
        //调用高级客户端中的方法进行查询
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        //获取结果
        String sourceAsString = getResponse.getSourceAsString();
        System.out.println("sourceAsString = " + sourceAsString);
    }

    //测试更新文档
    @Test
    public void testUpdateDoc() throws IOException {
        //创建UpdateRequest对象
        UpdateRequest updateRequest = new UpdateRequest("student", "1");
        //创建Student对象
        Student student = new Student();
        //设置要更新的属性
        student.setName("张小三");
        updateRequest.doc(JSONObject.toJSONString(student),XContentType.JSON);
        //调用高级客户端中更新文档属性的方法
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = updateResponse.getResult();
        System.out.println("result = " + result);
    }

    //测试批量添加文档
    @Test
    public void testBulkDoc(){
        //创建BulkRequest对象
        BulkRequest bulkRequest = new BulkRequest();
        Student student = new Student();
        for(int i=0;i<10;i++){
            student.setAge(18 + i);
            student.setName("robin" + i);
            student.setRemark("good man " + i);
            bulkRequest.add(new IndexRequest("student").id(String.valueOf(10 + i)).source(JSONObject.toJSONString(student), XContentType.JSON));
        }
        try {
            //调用高级客户端的bulk方法
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            for(BulkItemResponse itemResponse : response.getItems()){
                System.out.println(itemResponse.isFailed());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除文档
    @Test
    public void deleteDocument(){
        DeleteRequest request = new DeleteRequest("student","11");
        try {
            //调用高级客户端删除的方法
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            System.out.println(response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
