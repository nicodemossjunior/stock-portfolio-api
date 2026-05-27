package com.nicodemos.stockportfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodemos.stockportfolio.dto.PortfolioPosition;
import com.nicodemos.stockportfolio.service.StockPortfolioService;

/**
 * REST facade for portfolio position queries.
 */
@RestController
@RequestMapping("portfolio")
public class PortfolioRestController {

	@Autowired
	private StockPortfolioService stockPortfolioService;

	@GetMapping("/positions")
	public ResponseEntity<List<PortfolioPosition>> consolidatePortfolio() {
		return ResponseEntity.ok(stockPortfolioService.consolidatePortfolio());
	}

	@GetMapping("/positions/{ticker}")
	public ResponseEntity<PortfolioPosition> consolidateByTicker(@PathVariable String ticker) {
		return ResponseEntity.ok(stockPortfolioService.consolidateByTicker(ticker));
	}

}
