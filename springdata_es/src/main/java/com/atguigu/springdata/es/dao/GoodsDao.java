package com.atguigu.springdata.es.dao;

import com.atguigu.springdata.es.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsDao extends ElasticsearchRepository<Goods,String> {

}
