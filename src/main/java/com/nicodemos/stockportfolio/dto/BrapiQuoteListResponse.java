package com.nicodemos.stockportfolio.dto;

import java.util.ArrayList;
import java.util.List;

public class BrapiQuoteListResponse {

	private List<MarketAssetResponse> stocks = new ArrayList<>();

	public List<MarketAssetResponse> getStocks() {
		return stocks;
	}

	public void setStocks(List<MarketAssetResponse> stocks) {
		this.stocks = stocks;
	}

}
