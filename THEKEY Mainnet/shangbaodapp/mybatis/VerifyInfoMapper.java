package com.xczg.blockchain.shangbaodapp.mybatis;

import com.xczg.blockchain.shangbaodapp.bean.ProductApply;
import com.xczg.blockchain.yibaodapp.bean.ProductClaim;

import java.util.List;
import java.util.Map;

public interface VerifyInfoMapper {

	void insertProductClaim(ProductClaim o);
	/**
	 * 更新产品理赔
	 * @param o 产品理赔对象
	 */
	void updateProductClaim(ProductClaim o);
	/**
	 * 删除产品理赔
	 * @param map
	 */
	void deleteProductClaim(Map<String, Object> map);


	/**
	 * 查询投保产品购买申请个数
	 * @param map
	 * @return
	 */
	int getProductApplyListCount(Map<String, Object> map);
	/**
	 * 分页查询投保产品购买申请列表
	 * @param map
	 * @return
	 */
	List<ProductApply> getProductApplyPageList(Map<String, Object> map);
	/**
	 * 根据主键查询投保产品购买申请明细信息
	 * @param map
	 * @return
	 */
	List<ProductApply> getProductApplyById(Map<String, Object> map);
	/**
	 * 创建投保产品购买申请
	 * @param o 投保产品购买申请对象
	 */
	void insertProductApply(ProductApply o);
	/**
	 * 更新投保产品购买申请
	 * @param o 投保产品购买申请对象
	 */
	void updateProductApply(ProductApply o);

	/**
	 * 修改投保支付状态
	 * @param o
	 */
	void updateProductApplyPayStatus(ProductApply o);

	/**
	 * 删除投保产品购买申请
	 * @param map
	 */
	void deleteProductApply(Map<String, Object> map);
}	
