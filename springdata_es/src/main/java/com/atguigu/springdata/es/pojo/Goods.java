package com.atguigu.springdata.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
@NoArgsConstructor//无参数构造器
@AllArgsConstructor//有参数构造器
@Data//setter和getter方法
@Document(indexName = "goods")//索引的名字
public class Goods implements Serializable {

    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text)
    private String goodsName;
    @Field(type = FieldType.Integer)
    private Integer store;
    @Field(type = FieldType.Double)
    private double price;

}
