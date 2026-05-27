package com.nicodemos.stockportfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicodemos.stockportfolio.model.StockPurchase;
import com.nicodemos.stockportfolio.service.StockPortfolioService;

/**
 * REST facade for stock purchase management.
 */
@RestController
@RequestMapping("purchases")
public class StockPurchaseRestController {

	@Autowired
	private StockPortfolioService stockPortfolioService;

	@GetMapping
	public ResponseEntity<Iterable<StockPurchase>> findAll() {
		return ResponseEntity.ok(stockPortfolioService.findAllPurchases());
	}

	@GetMapping("/{id}")
	public ResponseEntity<StockPurchase> findById(@PathVariable Long id) {
		return ResponseEntity.ok(stockPortfolioService.findPurchaseById(id));
	}

	@GetMapping("/ticker/{ticker}")
	public ResponseEntity<Iterable<StockPurchase>> findByTicker(@PathVariable String ticker) {
		return ResponseEntity.ok(stockPortfolioService.findPurchasesByTicker(ticker));
	}

	@PostMapping
	public ResponseEntity<StockPurchase> create(@RequestBody StockPurchase purchase) {
		stockPortfolioService.createPurchase(purchase);
		return ResponseEntity.ok(purchase);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StockPurchase> update(@PathVariable Long id, @RequestBody StockPurchase purchase) {
		stockPortfolioService.updatePurchase(id, purchase);
		return ResponseEntity.ok(purchase);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		stockPortfolioService.deletePurchase(id);
		return ResponseEntity.ok().build();
	}

}
