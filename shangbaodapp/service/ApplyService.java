package com.xczg.blockchain.shangbaodapp.service;

import com.xczg.blockchain.yibaodapp.bean.*;

import java.util.List;
import java.util.Map;

public interface ApplyService {

	

	/**
	 * 查询投保产品购买申请个数
	 * @param o 投保产品购买申请对象
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @return
	 */
	public int getProductApplyListCount(ProductApply o,long time1,long time2);

	/**
	 * 分页查询投保产品购买申请列表
	 * @param o 投保产品购买申请对象
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @param iDisplayStart 分页记录开始
	 * @param iDisplayLength 每页返回记录数
	 * @param orderName 排序字段
	 * @param orderAZ 排序方式
	 * @return
	 */
	public List<ProductApply> getProductApplyPageList(ProductApply o,long time1,long time2,int iDisplayStart,int iDisplayLength,String orderName,String orderAZ);

	/**
	 * 根据主键查询投保产品购买申请明细信息
	 * @param id
	 * @return
	 */
	public ProductApply getProductApplyById(long id);

	/**
	 * 创建投保产品购买申请
	 * @param info 投保产品购买申请对象
	 */
	public void insertProductApply(ProductApply info) ;

	/**
	 * 更新投保产品购买申请
	 * @param info 投保产品购买申请对象
	 */
	public void updateProductApply(ProductApply info);
	
	public void updateProductApplyPayStatus(ProductApply info);

	/**
	 * 删除投保产品购买申请
	 * @param id
	 */
	public void deleteProductApply(long id) ;

}
