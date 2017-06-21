package com.example.classes;

public class LogisticsDetial {
	private int id;//物流收费标准id（1：到县自提 【先付】2：到县自提【后付】 3：工作站配送到村）
	private String title;//物流配送title
	private String content;//物流配送详情
	private String lgistics;//物流费
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLgistics() {
		return lgistics;
	}
	public void setLgistics(String lgistics) {
		this.lgistics = lgistics;
	}
	

}
