package com.example.classes;

import java.util.List;

public class HomePageList {

	private DataBean Data;
	private boolean Result;
	private String Message;

	public DataBean getData() {
		return Data;
	}

	public void setData(DataBean Data) {
		this.Data = Data;
	}

	public boolean isResult() {
		return Result;
	}

	public void setResult(boolean Result) {
		this.Result = Result;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public static class DataBean {

		private List<HomePageBean> lunboList;//轮播
		private List<HomePageBean> chucaoList;//除草
		private List<HomePageBean> shachongList;//杀虫
		private List<HomePageBean> shajunList;//杀菌
		private List<HomePageBean> qitaList;//调节剂及其他
		private List<HomePageBean> coupons; // 优惠券专区
		private List<HomePageBean> zhongbuList; // 新加的团购
		private List<HomePageBean> zhongbudaohang;//中部导航

		public List<HomePageBean> getZhongbudaohang() {
			return zhongbudaohang;
		}

		public void setZhongbudaohang(List<HomePageBean> zhongbudaohang) {
			this.zhongbudaohang = zhongbudaohang;
		}

		public List<HomePageBean> getLunboList() {
			return lunboList;
		}

		public void setLunboList(List<HomePageBean> lunboList) {
			this.lunboList = lunboList;
		}

		public List<HomePageBean> getCoupons() {
			return coupons;
		}

		public void setCoupons(List<HomePageBean> coupons) {
			this.coupons = coupons;
		}

		public List<HomePageBean> getZhongbuList() {
			return zhongbuList;
		}

		public void setZhongbuList(List<HomePageBean> zhongbuList) {
			this.zhongbuList = zhongbuList;
		}

		public List<HomePageBean> getChucaoList() {
			return chucaoList;
		}

		public void setChucaoList(List<HomePageBean> chucaoList) {
			this.chucaoList = chucaoList;
		}

		public List<HomePageBean> getShachongList() {
			return shachongList;
		}

		public void setShachongList(List<HomePageBean> shachongList) {
			this.shachongList = shachongList;
		}

		public List<HomePageBean> getShajunList() {
			return shajunList;
		}

		public void setShajunList(List<HomePageBean> shajunList) {
			this.shajunList = shajunList;
		}

		public List<HomePageBean> getQitaList() {
			return qitaList;
		}

		public void setQitaList(List<HomePageBean> qitaList) {
			this.qitaList = qitaList;
		}
	}
}
