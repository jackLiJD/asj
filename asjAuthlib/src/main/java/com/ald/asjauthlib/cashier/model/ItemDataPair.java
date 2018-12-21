package com.ald.asjauthlib.cashier.model;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/21 16:45
 * 描述：
 * 修订历史：
 */

public class ItemDataPair {

	/** 数据 */
	private Object data;

	/** 数据类型，由各Adapter定义维护 */
	private int itemType;

	public ItemDataPair(Object data, int itemType) {
		this.data = data;
		this.itemType = itemType;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
