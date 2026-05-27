package com.nicodemos.stockportfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nicodemos.stockportfolio.dto.MarketAssetResponse;
import com.nicodemos.stockportfolio.service.StockPortfolioService;

/**
 * REST facade for external market data queries.
 */
@RestController
@RequestMapping("market")
public class MarketRestController {

	@Autowired
	private StockPortfolioService stockPortfolioService;

	@GetMapping("/top-gainers")
	public ResponseEntity<List<MarketAssetResponse>> findTopGainers(
			@RequestParam(defaultValue = "10") Integer limit) {
		return ResponseEntity.ok(stockPortfolioService.findTopGainers(limit));
	}

	@GetMapping("/top-losers")
	public ResponseEntity<List<MarketAssetResponse>> findTopLosers(
			@RequestParam(defaultValue = "10") Integer limit) {
		return ResponseEntity.ok(stockPortfolioService.findTopLosers(limit));
	}

}
