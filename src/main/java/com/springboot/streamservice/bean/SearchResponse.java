package com.springboot.streamservice.bean;

import java.util.List;

import com.springboot.streamservice.bean.tmbdbean.Result;

public class SearchResponse {
	public int page;
	public List<Result> results;
	public int total_pages;
	public int total_results;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}

	public int getTotal_results() {
		return total_results;
	}

	public void setTotal_results(int total_results) {
		this.total_results = total_results;
	}
}
