package com.nicodemos.stockportfolio.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nicodemos.stockportfolio.dto.MarketAssetResponse;
import com.nicodemos.stockportfolio.dto.BrapiQuoteListResponse;
import com.nicodemos.stockportfolio.dto.PortfolioPosition;
import com.nicodemos.stockportfolio.model.StockPurchase;
import com.nicodemos.stockportfolio.model.StockPurchaseRepository;
import com.nicodemos.stockportfolio.service.BrapiService;

@ExtendWith(MockitoExtension.class)
class StockPortfolioServiceImplTest {

	@Mock
	private StockPurchaseRepository stockPurchaseRepository;
	@Mock
	private BrapiService brapiService;
	@InjectMocks
	private StockPortfolioServiceImpl stockPortfolioService;

	@Test
	void shouldCreatePurchaseWithNormalizedTicker() {
		StockPurchase purchase = createPurchase(" petr4 ", 100, "28.50");

		stockPortfolioService.createPurchase(purchase);

		assertEquals("PETR4", purchase.getTicker());
		verify(stockPurchaseRepository).save(purchase);
	}

	@Test
	void shouldFindPurchasesByTickerIgnoringCase() {
		when(stockPurchaseRepository.findByTickerIgnoreCase("petr4")).thenReturn(Collections.emptyList());

		stockPortfolioService.findPurchasesByTicker("petr4");

		verify(stockPurchaseRepository).findByTickerIgnoreCase("petr4");
	}

	@Test
	void shouldCalculateAveragePriceForCurrentTickerPosition() {
		when(stockPurchaseRepository.findByTickerIgnoreCase("PETR4")).thenReturn(Arrays.asList(
				createPurchase("PETR4", 100, "10.00"),
				createPurchase("PETR4", 50, "20.00")));

		PortfolioPosition position = stockPortfolioService.consolidateByTicker("petr4");

		assertEquals("PETR4", position.getTicker());
		assertEquals(150, position.getTotalQuantity());
		assertEquals(new BigDecimal("2000.00"), position.getTotalAmount());
		assertEquals(new BigDecimal("13.33"), position.getAveragePrice());
	}

	@Test
	void shouldConsolidateAllPortfolioAssets() {
		when(stockPurchaseRepository.findAll()).thenReturn(Arrays.asList(
				createPurchase("petr4", 100, "10.00"),
				createPurchase("VALE3", 10, "60.00"),
				createPurchase("PETR4", 50, "20.00")));

		List<PortfolioPosition> positions = stockPortfolioService.consolidatePortfolio();

		assertEquals(2, positions.size());
		assertEquals("PETR4", positions.get(0).getTicker());
		assertEquals(150, positions.get(0).getTotalQuantity());
		assertEquals(new BigDecimal("2000.00"), positions.get(0).getTotalAmount());
		assertEquals(new BigDecimal("13.33"), positions.get(0).getAveragePrice());
		assertEquals("VALE3", positions.get(1).getTicker());
		assertEquals(10, positions.get(1).getTotalQuantity());
		assertEquals(new BigDecimal("600.00"), positions.get(1).getTotalAmount());
		assertEquals(new BigDecimal("60.00"), positions.get(1).getAveragePrice());
	}

	@Test
	void shouldUpdateExistingPurchase() {
		StockPurchase purchase = createPurchase("vale3", 10, "60.00");
		when(stockPurchaseRepository.findById(1L)).thenReturn(Optional.of(createPurchase("VALE3", 5, "55.00")));

		stockPortfolioService.updatePurchase(1L, purchase);

		assertEquals(1L, purchase.getId());
		assertEquals("VALE3", purchase.getTicker());
		verify(stockPurchaseRepository).save(purchase);
	}

	@Test
	void shouldDeletePurchase() {
		stockPortfolioService.deletePurchase(1L);

		verify(stockPurchaseRepository).deleteById(1L);
	}

	@Test
	void shouldFindTopGainersWithDescendingOrder() {
		BrapiQuoteListResponse response = new BrapiQuoteListResponse();
		response.setStocks(Collections.singletonList(new MarketAssetResponse()));
		when(brapiService.listStocks(any(), any(), any(), any())).thenReturn(response);

		List<MarketAssetResponse> assets = stockPortfolioService.findTopGainers(5);

		assertEquals(1, assets.size());
		verify(brapiService).listStocks("change", "desc", 5, "stock");
	}

	@Test
	void shouldFindTopLosersWithAscendingOrder() {
		BrapiQuoteListResponse response = new BrapiQuoteListResponse();
		response.setStocks(Collections.singletonList(new MarketAssetResponse()));
		when(brapiService.listStocks(any(), any(), any(), any())).thenReturn(response);

		List<MarketAssetResponse> assets = stockPortfolioService.findTopLosers(5);

		assertEquals(1, assets.size());
		verify(brapiService).listStocks("change", "asc", 5, "stock");
	}

	private StockPurchase createPurchase(String ticker, Integer quantity, String price) {
		StockPurchase purchase = new StockPurchase();
		purchase.setTicker(ticker);
		purchase.setQuantity(quantity);
		purchase.setPrice(new BigDecimal(price));
		purchase.setPurchaseDate(LocalDate.of(2026, 5, 26));
		return purchase;
	}

}
