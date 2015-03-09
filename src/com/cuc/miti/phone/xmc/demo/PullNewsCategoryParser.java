package com.cuc.miti.phone.xmc.demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.cuc.miti.phone.xmc.domain.NewsCategory;

public class PullNewsCategoryParser implements NewsCategoryParser{

	public List<NewsCategory> parser(InputStream is) throws Exception {
		return null;

//		List<NewsCategory> newsCategoryList = null;
//		NewsCategory newsCategory = null;
//		Hashtable<String,String> languageList =  null;
//		
//		//��android.util.Xml����һ��XmlPullParserʵ��
//		XmlPullParser parser = Xml.newPullParser();
//		parser.setInput(is, "UTF-8");
//		
//		int eventType = parser.getEventType();  
//		while (eventType != XmlPullParser.END_DOCUMENT) {  
//			switch (eventType) {  
//				case XmlPullParser.START_DOCUMENT:  
//						newsCategoryList = new ArrayList<NewsCategory>();  
//						break;  
//				case XmlPullParser.START_TAG:  
//					if (parser.getName().equals("Topic")) {  
//						newsCategory = new NewsCategory();  
//						languageList = new Hashtable<String, String>();
//						newsCategory.setTopicId(parser.getAttributeValue("", "topicId"));
//						newsCategory.setId(parser.getAttributeValue("","id"));
//						newsCategory.setDeleteFlag(parser.getAttributeValue("","deleteFlag"));
//					} else if (parser.getName().equals("Name")) {
//						
//						String temp = parser.getAttributeValue(0);	//ȡ��xml:lang��ֵ
//						eventType = parser.next();  
//						
//						languageList.put(temp, parser.getText().toString());
//					} 
//					break;  
//				case XmlPullParser.END_TAG:  
//					if (parser.getName().equals("Topic")) {  
//						newsCategory.setLanguageList(languageList);
//						newsCategoryList.add(newsCategory);  
//						newsCategory = null;    
//						languageList = null;
//					}  
//					break;  
//			}  
//			eventType = parser.next();  
//		}  
//		return newsCategoryList;  
	}

	public String Serialize(List<NewsCategory> newCategoryList)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
