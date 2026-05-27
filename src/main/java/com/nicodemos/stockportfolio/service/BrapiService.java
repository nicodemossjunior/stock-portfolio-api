package com.nicodemos.stockportfolio.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nicodemos.stockportfolio.dto.BrapiQuoteListResponse;

/**
 * OpenFeign HTTP client for the brapi.dev API.
 */
@FeignClient(name = "brapi", url = "${brapi.url:https://brapi.dev}")
public interface BrapiService {

	@GetMapping("/api/quote/list")
	BrapiQuoteListResponse listStocks(
			@RequestParam("sortBy") String sortBy,
			@RequestParam("sortOrder") String sortOrder,
			@RequestParam("limit") Integer limit,
			@RequestParam("type") String type);

}
