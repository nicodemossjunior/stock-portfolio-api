package com.nicodemos.stockportfolio.service;

import java.util.List;

import com.nicodemos.stockportfolio.dto.MarketAssetResponse;
import com.nicodemos.stockportfolio.dto.PortfolioPosition;
import com.nicodemos.stockportfolio.model.StockPurchase;

/**
 * Strategy interface for stock portfolio operations.
 */
public interface StockPortfolioService {

	Iterable<StockPurchase> findAllPurchases();

	StockPurchase findPurchaseById(Long id);

	Iterable<StockPurchase> findPurchasesByTicker(String ticker);

	void createPurchase(StockPurchase purchase);

	void updatePurchase(Long id, StockPurchase purchase);

	void deletePurchase(Long id);

	List<PortfolioPosition> consolidatePortfolio();

	PortfolioPosition consolidateByTicker(String ticker);

	List<MarketAssetResponse> findTopGainers(Integer limit);

	List<MarketAssetResponse> findTopLosers(Integer limit);

}
