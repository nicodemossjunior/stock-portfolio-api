package com.nicodemos.stockportfolio.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPurchaseRepository extends CrudRepository<StockPurchase, Long> {

	Iterable<StockPurchase> findByTickerIgnoreCase(String ticker);

}
