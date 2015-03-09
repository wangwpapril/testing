package com.cuc.miti.phone.xmc.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ڵ�
 * @author SongQing
 *
 */
public class TreeNode {
    private TreeNode parent;																		//���ڵ�
    private List<TreeNode> children = new ArrayList<TreeNode>();			//�ӽڵ�
    private String text;																				//�ڵ���ʾ������
    private String value;																				//�ڵ��ֵ
    private int icon = -1;																			//�Ƿ���ʾСͼ��,-1��ʾ����ͼ��
    private boolean isChecked = false;														//�Ƿ���ѡ��״̬
    private boolean isExpanded = true;														//�Ƿ���չ��״̬
    private boolean hasCheckBox = true;													//�Ƿ�ӵ�и�ѡ��
    
    /**
     * TreeNode���캯��
     * @param text �ڵ���ʾ������
     * @param value �ڵ��ֵ
     */
    public TreeNode(String text,String value){
    	this.text = text;
    	this.value = value;
    }
    
    /**
     * ���ø��ڵ�
     * @param node
     */
    public void setParent(TreeNode node){
    		this.parent = node;
    }
    /**
     * ��ø��ڵ�
     * @return
     */
    public TreeNode getParent(){
    	return this.parent;
    }
    /**
     * ���ýڵ��ı�
     * @param text
     */
    public void setText(String text){
    	this.text = text;
    }
    /**
     * ��ýڵ��ı�
     * @return
     */
    public String getText(){
    	return this.text;
    }
    /**
     * ���ýڵ�ֵ
     * @param value
     */
    public void setValue(String value){
    	this.value = value;
    }
    /**
     * ��ýڵ�ֵ
     * @return
     */
    public String getValue(){
    	return this.value;
    }
    /**
     * ���ýڵ�ͼ���ļ�
     * @param icon
     */
    public void setIcon(int icon){
    	this.icon = icon;
    }
    /**
     * ���ͼ���ļ�
     * @return
     */
    public int getIcon(){
    	return icon;
    }
    /**
     * �Ƿ��ڵ�
     * @return
     */
    public boolean isRoot(){
    	return parent==null?true:false;
    }
    /**
     * ����ӽڵ�
     * @return
     */
    public List<TreeNode> getChildren(){
    	return this.children;
    }
    /**
     * ����ӽڵ�
     * @param node
     */
    public void add(TreeNode node){
    	if(!children.contains(node)){
    		children.add(node);
    	}
    }
    /**
     * ����ӽڵ�(ָ��λ��)
     * @param node 
     * @param addLocation
     */
    public void add(TreeNode node,int addLocation){
    	if(children.contains(node)){
    		children.add(addLocation,node);
    	}
    }
    
    /**
     * ��������ӽڵ�
     * @param subNodes
     */
    public void addAll(List<TreeNode> subNodes){
    	this.children.addAll(subNodes);
    }
    /**
     * ��������ӽڵ�
     */
    public void clear(){
    	children.clear();
    }
    /**
     * ɾ��һ���ӽڵ�
     * @param node
     */
    public void remove(TreeNode node){
    	if(!children.contains(node)){
    		children.remove(node);
    	}
    }  
    /**
     * ����һ���ڵ�
     * @param oldNode �Ͻڵ�
     * @param newNode �½ڵ�
     */
    public void update(TreeNode oldNode,TreeNode newNode){
    	int index = children.indexOf(oldNode);
    	if( index >=0){
    		children.remove(oldNode);
    		children.add(index, newNode);
    	}
    }
    /**
     * ɾ��ָ��λ�õ��ӽڵ�
     * @param location
     */
    public void remove(int location){
    	children.remove(location);
    }
    /**
     * ��ýڵ�ļ���,��ڵ�Ϊ0
     * @return
     */
    public int getLevel(){
    	return parent==null?0:parent.getLevel()+1;
    }
    /**
     * ���ýڵ�ѡ��״̬
     * @param isChecked
     */
    public void setChecked(boolean isChecked){
    	this.isChecked = isChecked;
    }
    /**
     * ��ýڵ�ѡ��״̬
     * @return
     */
    public boolean isChecked(){
        return isChecked;
    }
    /**
     * �����Ƿ�ӵ�и�ѡ��
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
    	this.hasCheckBox = hasCheckBox;
    }
    /**
     * �Ƿ�ӵ�и�ѡ��
     * @return
     */
    public boolean hasCheckBox(){
        return hasCheckBox;
    }
    
    /**
     * �Ƿ�Ҷ�ڵ�,��û���ӽڵ�Ľڵ�
     * @return
     */
    public boolean isLeaf(){
    	return children.size()<1?true:false;
    }
    /**
    * ��ǰ�ڵ��Ƿ���չ��״̬ 
    * @return
    */
    public boolean isExpanded(){
        return isExpanded;
    }
    /**
     * ���ýڵ�չ��״̬
     * @return
     */
    public void setExpanded(boolean isExpanded){
    	 this.isExpanded =  isExpanded;
    }
    /**
     * �ݹ��жϸ��ڵ��Ƿ����۵�״̬,��һ�����ڵ��۵�����Ϊ���۵�״̬
     * @return
     */
    public boolean isParentCollapsed(){
    	if(parent==null)return !isExpanded;
    	if(!parent.isExpanded())return true;
    	return parent.isParentCollapsed();
    }
    /**
     * �ݹ��ж����Ľڵ��Ƿ�ǰ�ڵ�ĸ��ڵ�
     * @param node ���ڵ�
     * @return
     */
    public boolean isParent(TreeNode node){
    	if(parent==null)return false;
    	if(node.equals(parent))return true;
    	return parent.isParent(node);
    }
}
