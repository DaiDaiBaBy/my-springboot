package com.zhoufu.springbootexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zhoufu.springbootexcel.mapper.GoodsMapper;
import com.zhoufu.springbootexcel.model.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhoufu
 * @Date: 2021/2/1 16:36
     * @description: EasyExcel 导入监听
 */

public class ExcelListener extends AnalysisEventListener<Goods> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    /**
     * 可以通过实例获取该值
     */
    private List<Goods> datas = new ArrayList<>();

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private GoodsMapper goodsMapper;

    /**
     *
     * 不要使用自动装配
     * 在测试类中将dao当参数传进来
     */
    public ExcelListener(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    @Override
    public void invoke(Goods goods, AnalysisContext analysisContext) {
        datas.add(goods); // 数据批量存储到list， 供批量处理，或后续自己业务逻辑处理
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (datas.size() >= BATCH_COUNT){
            saveData();
            // 清理完成储存 list
            datas.clear();
        }
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        //   在这个地方可以调用dao  我们就直接打印数据了
        System.out.println(datas);
        for (Goods goods : datas){
            goodsMapper.addGoods(goods);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
    }

    public List<Goods> getDatas(){
        return datas;
    }
    public void setDatas(List<Goods> data){
        this.datas = data;
    }
}
