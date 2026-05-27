package com.nicodemos.stockportfolio.dto;

import java.math.BigDecimal;

public class PortfolioPosition {

	private String ticker;
	private Integer totalQuantity;
	private BigDecimal totalAmount;
	private BigDecimal averagePrice;

	public PortfolioPosition() {
	}

	public PortfolioPosition(String ticker, Integer totalQuantity, BigDecimal totalAmount, BigDecimal averagePrice) {
		this.ticker = ticker;
		this.totalQuantity = totalQuantity;
		this.totalAmount = totalAmount;
		this.averagePrice = averagePrice;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

}
