package com.android.test.uiautomator;

public class UIAutomatorData {

	private String packageName;
	private double totalTime;
	private double changeTime;
	private int pageNumber = 0;
	
	public UIAutomatorData(String packString){
		packageName=packString;
	}

	public void changeTimePlus(Double plus){
		changeTime+=plus;
	}

	public void totalTimePlus(Double plus){
		totalTime+=plus;
	}

	public void pageNumberPlus(){
		pageNumber+=1;
	}

//	@Override
//	public String toString() {
//		return "<被测包名：" + packageName
//				+ "\n  覆盖页面数："+pageNumber
//				+ "\n  总测试时长："+BasicUtils.formatSecond(totalTime)
//				+ "\n  页面变化时长："+ BasicUtils.formatSecond(changeTime) +">\r\n";
//	}


	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public double getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(long changeTime) {
		this.changeTime = changeTime;
	}

	public double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
