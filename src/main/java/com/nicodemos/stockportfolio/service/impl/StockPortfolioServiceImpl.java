package com.nicodemos.stockportfolio.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nicodemos.stockportfolio.dto.MarketAssetResponse;
import com.nicodemos.stockportfolio.dto.BrapiQuoteListResponse;
import com.nicodemos.stockportfolio.dto.PortfolioPosition;
import com.nicodemos.stockportfolio.model.StockPurchase;
import com.nicodemos.stockportfolio.model.StockPurchaseRepository;
import com.nicodemos.stockportfolio.service.BrapiService;
import com.nicodemos.stockportfolio.service.StockPortfolioService;

/**
 * StockPortfolioService strategy implementation, managed as a Spring singleton.
 */
@Service
public class StockPortfolioServiceImpl implements StockPortfolioService {

	private static final int DEFAULT_LIMIT = 10;
	private static final String SORT_BY_CHANGE = "change";
	private static final String SORT_ORDER_ASC = "asc";
	private static final String SORT_ORDER_DESC = "desc";
	private static final String TYPE_STOCK = "stock";

	@Autowired
	private StockPurchaseRepository stockPurchaseRepository;
	@Autowired
	private BrapiService brapiService;

	@Override
	public Iterable<StockPurchase> findAllPurchases() {
		return stockPurchaseRepository.findAll();
	}

	@Override
	public StockPurchase findPurchaseById(Long id) {
		Optional<StockPurchase> purchase = stockPurchaseRepository.findById(id);
		return purchase.orElseThrow(() -> new NoSuchElementException("Stock purchase not found."));
	}

	@Override
	public Iterable<StockPurchase> findPurchasesByTicker(String ticker) {
		return stockPurchaseRepository.findByTickerIgnoreCase(ticker);
	}

	@Override
	public void createPurchase(StockPurchase purchase) {
		savePurchase(purchase);
	}

	@Override
	public void updatePurchase(Long id, StockPurchase purchase) {
		Optional<StockPurchase> storedPurchase = stockPurchaseRepository.findById(id);
		if (storedPurchase.isPresent()) {
			purchase.setId(id);
			savePurchase(purchase);
		}
	}

	@Override
	public void deletePurchase(Long id) {
		stockPurchaseRepository.deleteById(id);
	}

	@Override
	public List<PortfolioPosition> consolidatePortfolio() {
		Map<String, List<StockPurchase>> purchasesByTicker = new TreeMap<>();

		for (StockPurchase purchase : stockPurchaseRepository.findAll()) {
			String ticker = normalizeTicker(purchase.getTicker());
			if (StringUtils.hasText(ticker)) {
				purchasesByTicker.computeIfAbsent(ticker, key -> new ArrayList<>()).add(purchase);
			}
		}

		List<PortfolioPosition> positions = new ArrayList<>();
		for (Map.Entry<String, List<StockPurchase>> entry : purchasesByTicker.entrySet()) {
			positions.add(consolidatePurchases(entry.getKey(), entry.getValue()));
		}
		return positions;
	}

	@Override
	public PortfolioPosition consolidateByTicker(String ticker) {
		String normalizedTicker = normalizeTicker(ticker);
		Iterable<StockPurchase> purchases = stockPurchaseRepository.findByTickerIgnoreCase(normalizedTicker);

		return consolidatePurchases(normalizedTicker, purchases);
	}

	@Override
	public List<MarketAssetResponse> findTopGainers(Integer limit) {
		return findAssetsByChange(SORT_ORDER_DESC, limit);
	}

	@Override
	public List<MarketAssetResponse> findTopLosers(Integer limit) {
		return findAssetsByChange(SORT_ORDER_ASC, limit);
	}

	private PortfolioPosition consolidatePurchases(String ticker, Iterable<StockPurchase> purchases) {
		Integer totalQuantity = 0;
		BigDecimal totalAmount = BigDecimal.ZERO;

		for (StockPurchase purchase : purchases) {
			Integer quantity = purchase.getQuantity() == null ? 0 : purchase.getQuantity();
			BigDecimal price = purchase.getPrice() == null ? BigDecimal.ZERO : purchase.getPrice();

			totalQuantity += quantity;
			totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
		}

		BigDecimal averagePrice = BigDecimal.ZERO;
		if (totalQuantity > 0) {
			averagePrice = totalAmount.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP);
		}

		return new PortfolioPosition(ticker, totalQuantity, totalAmount, averagePrice);
	}

	private void savePurchase(StockPurchase purchase) {
		purchase.setTicker(normalizeTicker(purchase.getTicker()));
		stockPurchaseRepository.save(purchase);
	}

	private String normalizeTicker(String ticker) {
		if (!StringUtils.hasText(ticker)) {
			return ticker;
		}
		return ticker.trim().toUpperCase();
	}

	private List<MarketAssetResponse> findAssetsByChange(String sortOrder, Integer limit) {
		BrapiQuoteListResponse response = brapiService.listStocks(
				SORT_BY_CHANGE,
				sortOrder,
				normalizeLimit(limit),
				TYPE_STOCK);

		if (response == null || response.getStocks() == null) {
			return Collections.emptyList();
		}
		return new ArrayList<>(response.getStocks());
	}

	private Integer normalizeLimit(Integer limit) {
		if (limit == null || limit <= 0) {
			return DEFAULT_LIMIT;
		}
		return limit;
	}

}
