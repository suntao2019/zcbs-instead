package com.zcbspay.platform.instead.realtime.bean;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.zcbspay.platform.instead.common.bean.BaseBean;

/**
 * 实时代付请求bean
 * @author: zhangshd
 * @date:   2017年3月13日 下午1:17:46   
 * @version :v1.0
 */
public class RealTimePayReqBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6480809760868993217L;
	@Length(max = 14,message="[订单发送时间]长度不符合规范")
	@NotEmpty(message="[订单发送时间]不能为空")
	private String 	txnTime	;//	订单发送时间
	@Length(max = 14,message="[支付超时时间]长度不符合规范")
	@NotEmpty(message="[支付超时时间]不能为空")
	private String payTimeout;//支付超时时间
	@Length(max = 15,message="[商户代码]长度不符合规范")
	@NotEmpty(message="[商户代码]不能为空")
	private String 	merId	;//	商户代码
	@Length(max = 40,message="[商户全称]长度不符合规范")
	private String 	merName	;//	商户全称
	@Length(max = 40,message="[商户简称]长度不符合规范")
	private String 	merAbbr	;//	商户简称
	@Length(max = 32,message="[商户订单号]长度不符合规范")
	@NotEmpty(message="[商户订单号]不能为空")
	private String 	orderId	;//	商户订单号
	@Length(max = 3,message="[交易币种]长度不符合规范")
	@NotEmpty(message="[交易币种]不能为空")
	private String 	currencyCode	;//	交易币种
	@Length(max = 12,message="[金额]长度不符合规范")
	@NotEmpty(message="[金额]不能为空")
	private String 	txnAmt	;//	金额
	@Length(max = 1024,message="[加密信息域]长度不符合规范")
	private String 	encryptData	;//	加密信息域
	private String 	reserved	;//	保留域
	
	public String getPayTimeout() {
		return payTimeout;
	}
	public void setPayTimeout(String payTimeout) {
		this.payTimeout = payTimeout;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public String getMerAbbr() {
		return merAbbr;
	}
	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getEncryptData() {
		return encryptData;
	}
	public void setEncryptData(String encryptData) {
		this.encryptData = encryptData;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}


}
