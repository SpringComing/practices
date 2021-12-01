package com.douzone.mysite.vo;

public class PageVo {
	private Long pIndex;
	private Long pages;
	private Long lines;
	private Long startPage;
	private Long endPage;
	private Long nextPage;
	private Long prevPage;
	private Long count;
	private Long ablepIndex;
	
	public PageVo(Long pIndex){
		pages = 5L;
		lines = 10L;
		setpIndex(pIndex);
	}

	public Long getpIndex() {
		return pIndex;
	}

	public void setpIndex(Long pIndex) {
		this.pIndex = pIndex;
		setStartPage(pages*((pIndex-1)/pages)+1);
		setEndPage(((pIndex-1)/pages+1)*pages);
		setPrevPage(getStartPage()-1);
		setNextPage(getEndPage()+1);
	}

	public Long getPages() {
		return pages;
	}

	public void setPages(Long pages) {
		this.pages = pages;
	}

	public Long getLines() {
		return lines;
	}

	public void setLines(Long lines) {
		this.lines = lines;
	}

	public Long getStartPage() {
		return startPage;
	}

	public void setStartPage(Long startPage) {
		this.startPage = startPage;
	}

	public Long getEndPage() {
		return endPage;
	}

	public void setEndPage(Long endPage) {
		this.endPage = endPage;
	}

	public Long getNextPage() {
		return nextPage;
	}

	public void setNextPage(Long nextPage) {
		this.nextPage = nextPage;
	}

	public Long getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(Long prevPage) {
		this.prevPage = prevPage;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
		setAblepIndex((long) Math.ceil((double)getCount()/(double)getLines()));
	}

	public Long getAblepIndex() {
		return ablepIndex;
	}

	public void setAblepIndex(Long ablepIndex) {
		this.ablepIndex = ablepIndex;
	}
	
	
}
