package com.android.test.uiautomator;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyXMLReadHandler extends DefaultHandler{

	StringBuilder resultStringBuilder;
	public StringBuilder getResultStringBuilder() {
		if (resultStringBuilder==null) {
			resultStringBuilder = new StringBuilder();
		}
		return resultStringBuilder;
	}

	public String getHashOfXML(){
		String resultString = resultStringBuilder.toString();
		return ""+BKDRHash(resultString);
	}

	public String getStrustOfXML(){
		return resultStringBuilder.toString();
	}

	/**
	 * 解析开始
	 **/
	public void startDocument() throws SAXException {
		getResultStringBuilder();
	}

	/**
	 * 开始标签时调用
	 */
	public void startElement(String arg0, String arg1, String arg2,Attributes arg3) throws SAXException {
		if (arg2.equals("node")) {
			getResultStringBuilder().append("_class="+arg3.getValue("class")+"_id="+arg3.getValue("resource-id"));
		}
	}

	/**
	 * 解析元素中的数据
	 */
	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	/**
	 * 结束标签时调用
	 */
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
	}

	/**
	 * 解析结束
	 */
	public void endDocument() throws SAXException {
	}


	/**
	 * BKDR Hash 获取哈希值
	 */
	public static long BKDRHash(String str)
	{
		long seed = 131; // 31 131 1313 13131 131313 etc..
		long hash = 0;
		for(int i = 0; i < str.length(); i++)
		{
			hash = (hash * seed) + str.charAt(i);
		}
		return hash;
	}
}
