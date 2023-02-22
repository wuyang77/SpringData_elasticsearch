package com.atguigu.springdata.es;

import com.atguigu.springdata.es.dao.GoodsDao;
import com.atguigu.springdata.es.pojo.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoodsTest {
    @Autowired
    private GoodsDao goodsDao;
    /*
        测试添加文档
     */
    @Test
    public void testIndexDoc(){
//        Goods goods = new Goods("1001", "Iphone 13 Pro Max", 1, 9799);
        Goods goods = new Goods("1002", "三星s23", 2, 9000);
        goodsDao.save(goods);
    }
    /*
        测试查询文档
     */
    @Test
    public void testGetDoc(){
        Iterable<Goods> all = goodsDao.findAll();
        for (Goods good : all) {
            System.out.println("good = " + good);
        }
        Goods goods = goodsDao.findById("1001").get();
        System.out.println("goods = " + goods);
    }
    /*
        测试更新和删除文档
     */
    @Test
    public void testUpdate(){
        Goods goods = new Goods("1002", "SAMSUNG22", 100, 100000);
        goodsDao.save(goods);//更新文档
        goodsDao.deleteById("1002");//删除文档
    }
}
