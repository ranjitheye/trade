package com.future.trade.vo;

public class ReportVO {

	
	private String clientInfo;
	private String productInfo;
    private double totalAmt;
    
    
    public ReportVO(){
    	
    }
    public ReportVO (String clientInfo, String productInfo, double totalAmt) {
    	this.clientInfo = clientInfo;
    	this.productInfo = productInfo;
    	this.totalAmt = totalAmt;
    }
	public String getClientInfo() {
		return clientInfo;
	}
	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}
	public String getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

    
    
}
