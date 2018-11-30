package com.xczg.blockchain.common.model;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据页面对象，代表一个有固定大小的页面对象
 * 
 */
@SuppressWarnings("serial")
public class PageResult<T> implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(PageResult.class);
	// 升序排列标记
	public static final String ASC = "asc";

	// 降序排列标记
	public static final String DESC = "desc";

	// 当前页序号
	protected long pageNo = 1;
	
	// 当前页序号
	protected long totalPages = -1;

	// 页面大小
	protected int pageSize = 10;
	
	// 当前页偏移量
	private long offset = 0;

	// 是否自动查询结果集记录数
	protected boolean autoCount = false;

	// 结果集记录数
	protected long total = -1;

	// 排序设置（允许多个，用','分割）
	// 默认以ID倒序排列
	protected String orderBy = "ID";
	
	protected String orderDirection = "DESC";

	// 返回结果集 
	protected List<T> rows = null;
	 

	// -- 构造函数 --//
	public PageResult() { }

	public PageResult(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public PageResult(int pageSize,int pageNo,boolean autoCount) {
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.autoCount=autoCount;
	}

	// -- 分页参数访问函数 --//
	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public long getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNo(final long pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 返回Page对象自身的setPageNo函数,可用于连续设置。
	 */
	public PageResult<T> pageNo(final long thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	/**
	 * 获得每页的记录数量, 默认为-1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回Page对象自身的setPageSize函数,可用于连续设置。
	 */
	public PageResult<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 */
	public long getOffset() {
		offset = (pageNo - 1) * pageSize;
		logger.debug("PageSize:" + pageSize + ", PageNo:" + pageNo + ", Offset:" + offset);
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	/**
	 * 获得排序字段,无默认值. 多个排序字段时用','分隔.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * 返回Page对象自身的setOrderBy函数,可用于连续设置。
	 */
	public PageResult<T> orderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}

	/**
	 * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为false.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 设置查询对象时是否自动先执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	/**
	 * 返回Page对象自身的setAutoCount函数,可用于连续设置。
	 */
	public PageResult<T> autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	// -- 访问查询结果函数 --//

	/**
	 * 获得总记录数, 默认值为-1.
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotal(final long total) {
		this.total= total;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 */
	public long getTotalPages() {
		if (total < 0) {
			return -1;
		}

		totalPages = total / pageSize;
		if (total % pageSize > 0) {
			totalPages++;
		}
		return totalPages;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
	 */
	public long getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	public long getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	
}
