package com.cuc.miti.phone.xmc.demo;

import java.io.InputStream;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.NewsCategory;

public interface NewsCategoryParser {
	
	/**
	 * �������������õ�NewsCategory���󼯺�
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public List<NewsCategory> parser(InputStream is) throws Exception;

	/**
	 * ���л�NewsCategory���󼯺ϣ��õ�xml��ʽ���ַ�
	 * @param newCategoryList
	 * @return
	 * @throws Exception
	 */
	public String Serialize(List<NewsCategory> newCategoryList ) throws Exception;
	
}
