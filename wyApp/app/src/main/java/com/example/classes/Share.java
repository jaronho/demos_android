package com.example.classes;

public class Share {


	/**
	 * IsLogin : true
	 * Data : {"SharedTitle":"[使百克·铜]50%咪鲜胺铜盐悬浮剂","SharedContent":"农一网代购发展代购，在家轻松赚钱，发展越多，收益越多","SharedUrl":"http://m.16899.com/Product/Detail/1546.html","SharedImage":"http://img.16899.com/Product/y/2016/05/a8c4743e39e6445683699688b6a06ce5.jpg","Message":"点击下方分享按钮分享到微信朋友、微信朋友圈","QRImage":""}
	 * Result : true
	 * Message :
	 */

	private boolean IsLogin;
	/**
	 * SharedTitle : [使百克·铜]50%咪鲜胺铜盐悬浮剂
	 * SharedContent : 农一网代购发展代购，在家轻松赚钱，发展越多，收益越多
	 * SharedUrl : http://m.16899.com/Product/Detail/1546.html
	 * SharedImage : http://img.16899.com/Product/y/2016/05/a8c4743e39e6445683699688b6a06ce5.jpg
	 * Message : 点击下方分享按钮分享到微信朋友、微信朋友圈
	 * QRImage :
	 */

	private DataBean Data;
	private boolean Result;
	private String Message;

	public boolean isIsLogin() {
		return IsLogin;
	}

	public void setIsLogin(boolean IsLogin) {
		this.IsLogin = IsLogin;
	}

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
		private String SharedTitle;
		private String SharedContent;
		private String SharedUrl;
		private String SharedImage;
		private String Message;//二维码信息
		private String QRImage;//二维码

		public String getSharedTitle() {
			return SharedTitle;
		}

		public void setSharedTitle(String SharedTitle) {
			this.SharedTitle = SharedTitle;
		}

		public String getSharedContent() {
			return SharedContent;
		}

		public void setSharedContent(String SharedContent) {
			this.SharedContent = SharedContent;
		}

		public String getSharedUrl() {
			return SharedUrl;
		}

		public void setSharedUrl(String SharedUrl) {
			this.SharedUrl = SharedUrl;
		}

		public String getSharedImage() {
			return SharedImage;
		}

		public void setSharedImage(String SharedImage) {
			this.SharedImage = SharedImage;
		}

		public String getMessage() {
			return Message;
		}

		public void setMessage(String Message) {
			this.Message = Message;
		}

		public String getQRImage() {
			return QRImage;
		}

		public void setQRImage(String QRImage) {
			this.QRImage = QRImage;
		}
	}
}
