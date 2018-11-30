package com.xczg.blockchain.yibaodapp.bean;



public class PageList {
	
		private  int iDisplayStart;//起始数据
		private int iDisplayLength;//一页所显示的数据条数
		private String sSortDir_0;
		private  String sEcho;//
		private int iTotalRecords;//数据总数
		private String orderAZ;//升、降排序
		private String orderName;//排序列表
		
		
		public String getOrderAZ() {
			return orderAZ;
		}
		public void setOrderAZ(String orderAZ) {
			this.orderAZ = orderAZ;
		}
		public String getOrderName() {
			return orderName;
		}
		public void setOrderName(String orderName) {
			this.orderName = orderName;
		}
		public int getiDisplayStart() {
			return iDisplayStart;
		}
		public void setiDisplayStart(int iDisplayStart) {
			this.iDisplayStart = iDisplayStart;
		}
		public int getiDisplayLength() {
			return iDisplayLength;
		}
		public void setiDisplayLength(int iDisplayLength) {
			this.iDisplayLength = iDisplayLength;
		}
		public String getsSortDir_0() {
			return sSortDir_0;
		}
		public void setsSortDir_0(String sSortDir_0) {
			this.sSortDir_0 = sSortDir_0;
		}
		public String getsEcho() {
			return sEcho;
		}
		public void setsEcho(String sEcho) {
			this.sEcho = sEcho;
		}
		public int getiTotalRecords() {
			return iTotalRecords;
		}
		public void setiTotalRecords(int iTotalRecords) {
			this.iTotalRecords = iTotalRecords;
		}
	
		
}
