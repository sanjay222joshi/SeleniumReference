package com.sanjay.browsers;

public interface Browser {
	public enum BrowserType {
		firefox("firefox"), reusefirefox("reusefirefox"), reuseiexplore("reuseiexplore"), iexplore("iexplore"), chrome(
				"chrome"), remote("remote"), htmlunit("htmlunit"), android("android"), firefox_parallel(
				"firefox_parallel"), iexplore_parallel("iexplore_parallel"), safari("safari"), iexplore_remote(
				"iexplore_remote");

		private String text;

		BrowserType(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

}
