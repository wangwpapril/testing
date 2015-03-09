package com.cuc.miti.phone.xmc.domain;

/**
 * "��ҳ"ʵ����,���ڴ��������ݲ�ѯʱ�洢��ҳ��Ϣ��
 * @author SongQing
 *
 */
public class Pager {	

	private int totalNum;	//������
	private int pageSize;	//ÿҳ����
	private int currentPage;	//��ǰҳ
	
	/**
	 * ���캯��
	 */
	public Pager(){

	}
	
	/**
	 * ���캯��
	 * @param pageSize   ÿҳ����
	 * @param currentPage   ��ǰҳ
	 */
	public Pager(int pageSize, int currentPage) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalNum
	 */
	public int getTotalNum() {
		return totalNum;
	}
	/**
	 * @param totalNum the totalNum to set
	 */
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getTotalPageCount(){
		int temp = this.totalNum % this.pageSize;
		
		if(temp == 0){
			int totalPage = this.totalNum / this.pageSize;
			return totalPage == 0 ? 1 : totalPage;
		}
		else {
			return this.totalNum / this.pageSize + 1;
		}
	}
	
	public static Pager getDefault(){
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(5);
		
		return pager;
	}
	
}
