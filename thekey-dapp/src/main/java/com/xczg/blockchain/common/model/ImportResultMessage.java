package com.xczg.blockchain.common.model;

import java.util.List;

public class ImportResultMessage<T> {
	private int toltalRows = 0;
	private int insertRows = 0;
	private String errorRows = "";
	private int updateRows = 0;
	private List<T> insertList;
	private List<T> updateList;
	
	public List<T> getInsertList() {
		return insertList;
	}
	public void setInsertList(List<T> insertList) {
		this.insertList = insertList;
	}
	public List<T> getUpdateList() {
		return updateList;
	}
	public void setUpdateList(List<T> updateList) {
		this.updateList = updateList;
	}
	private String errorMessage;
	
	public int getToltalRows() {
		return toltalRows;
	}
	public void setToltalRows(int toltalRows) {
		this.toltalRows = toltalRows;
	}
	public int getInsertRows() {
		return insertRows;
	}
	public void setInsertRows(int insertRows) {
		this.insertRows = insertRows;
	}
	public String getErrorRows() {
		return errorRows;
	}
	public void setErrorRows(String errorRows) {
		this.errorRows = errorRows;
	}
	public int getUpdateRows() {
		return updateRows;
	}
	public void setUpdateRows(int updateRows) {
		this.updateRows = updateRows;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
