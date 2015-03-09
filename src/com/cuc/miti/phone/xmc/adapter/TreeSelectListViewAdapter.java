package com.cuc.miti.phone.xmc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �����Դ������
 * @author SongQing
 *
 */
public class TreeSelectListViewAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater = null;														//��������XML����
	private List<TreeNode> allsCache = new ArrayList<TreeNode>();
	private List<TreeNode> alls = new ArrayList<TreeNode>();
	private TreeSelectListViewAdapter tsAdapter = this;
	private boolean hasCheckBox = true;														//�Ƿ�ӵ�и�ѡ��
	private int expandedIcon = -1;																	//չ����ť
	private int collapsedIcon = -1;																	//����ť
	
	/**
	 * TreeAdapter���캯��
	 * @param context 
	 * @param rootNode ��ڵ�
	 */
	public TreeSelectListViewAdapter(Context context,TreeNode rootNode){
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addNode(rootNode);
	}
	
	/**
	 * �������ڵ�
	 * @param node
	 */
	private void addNode(TreeNode node){
		alls.add(node);
		allsCache.add(node);
		if(node.isLeaf()){			//�����Ҷ�ӽڵ�
			return;
		}
		//�����Ҷ�ӽڵ㣬�����Ӹýڵ�����������ӽڵ�
		for(int i=0;i<node.getChildren().size();i++){
			addNode(node.getChildren().get(i));
		}
	}
	
	/**
	 * �������ڵ�
	 * @param node
	 */
	public void addNode(int position,TreeNode node){
		alls.add(position,node);
		allsCache.add(position,node);
		
		if(node.isLeaf()){			//�����Ҷ�ӽڵ�
			return;
		}
		//�����Ҷ�ӽڵ㣬�����Ӹýڵ�����������ӽڵ�
		for(int i=0;i<node.getChildren().size();i++){
			addNode(position+i+1,node.getChildren().get(i));
		}
	}
	
	/**
	 * ɾ�����ڵ�
	 * @param position
	 */
	public void removeNode(int position){
		alls.remove(position);
		allsCache.remove(position);
	}
	
	public void copyCacheToHost(){
		if(allsCache !=null &&alls !=null){
			alls.clear();
			for(TreeNode tn:allsCache){
				alls.add(tn);
			}
		}		
	} 
	/**
	 * ���ڵ���ӽڵ�ĸ�ѡ������
	 * @param node
	 * @param isChecked
	 */
	private void checkNode(TreeNode node,boolean isChecked){
		node.setChecked(isChecked);
		for(int i=0;i<node.getChildren().size();i++){
			checkNode(node.getChildren().get(i),isChecked);
		}
	}
	
	/**
	 * ���ѡ�нڵ�
	 * @return
	 */
	public List<TreeNode> getSeletedNodes(){
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for(int i=0;i<allsCache.size();i++){
			TreeNode n = allsCache.get(i);
			if(n.isChecked()){
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	/**
	 * ���ƽڵ��չ�����۵�
	 */
	private void filterNode(){
		List<TreeNode> temp = new ArrayList<TreeNode>();
		//alls.clear();
		for(int i=0;i<alls.size();i++){
			TreeNode n = alls.get(i);
			if(!n.isParentCollapsed() || n.isRoot()){
				temp.add(n);
			}
		}
		alls.clear();
		alls = temp;
	}
	
	/**
     * �����Ƿ�ӵ�и�ѡ��
     * @param hasCheckBox
     */
    public void setCheckBox(boolean hasCheckBox){
    	this.hasCheckBox = hasCheckBox;
    }
	
    /**
     * ����չ�����۵�״̬ͼ��
     * @param expandedIcon չ��ʱͼ��
     * @param collapsedIcon �۵�ʱͼ��
     */
    public void setExpandedCollapsedIcon(int expandedIcon,int collapsedIcon){
    	this.expandedIcon = expandedIcon;
    	this.collapsedIcon = collapsedIcon;
    }
    
	/**
	 * ����չ������
	 * @param level
	 */
	public void setExpandLevel(int level){
		alls.clear();
		for(int i=0;i<allsCache.size();i++){
			TreeNode n = allsCache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level){// �ϲ㶼����չ��״̬
					n.setExpanded(true);
				}else{// ���һ�㶼�����۵�״̬
					n.setExpanded(false);
				}
				alls.add(n);
			}
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * ���ƽڵ��չ��������
	 * @param position
	 */
	public void ExpandOrCollapse(int position){
		TreeNode n = alls.get(position);
		if(n != null){
			if(!n.isLeaf()){
				n.setExpanded(!n.isExpanded());
				filterNode();
				this.notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * ��ȡ�ڵ���ListTreeNode�е���ʵλ�ã�����ListView��Position
	 * @param c
	 * @return
	 */
	public int getItemPosition(TreeNode node){
		this.copyCacheToHost();
		return alls.indexOf(node);
	}
	
	public int getCount() {
		return alls.size();
	}

	public Object getItem(int position) {
		return alls.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		//if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.listview_item_for_treeselect, null);
			holder = new ViewHolder();
			holder.checkBoxSelect = (CheckBox)convertView.findViewById(R.id.checkBoxSelectItem_lvITFTS);
			
			// ��ѡ�򵥻��¼�
			holder.checkBoxSelect.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					TreeNode n = (TreeNode)v.getTag();
					checkNode(n,((CheckBox)v).isChecked());
					tsAdapter.notifyDataSetChanged();
				}
				
			});
			holder.imageViewIcon = (ImageView)convertView.findViewById(R.id.imageViewIcon_lvITFTS);
			holder.textViewText = (TextView)convertView.findViewById(R.id.textViewText_lvITFTS);
			holder.imageViewExEc = (ImageView)convertView.findViewById(R.id.imageViewExEc_lvITFTS);
			convertView.setTag(holder);
			
//		}else{
//			holder = (ViewHolder)convertView.getTag();
//		}
		
		// �õ���ǰ�ڵ�
		TreeNode currentNode = alls.get(position);
		
		if(currentNode != null){
			holder.checkBoxSelect.setTag(currentNode);
			holder.checkBoxSelect.setChecked(currentNode.isChecked());
			
			// �Ƿ���ʾ��ѡ��
			if(currentNode.hasCheckBox() && hasCheckBox){
				holder.checkBoxSelect.setVisibility(View.VISIBLE);
			}else{
				holder.checkBoxSelect.setVisibility(View.GONE);
			}
			
			// �Ƿ���ʾͼ��
			if(currentNode.getIcon() == -1){
			    holder.imageViewIcon.setVisibility(View.GONE);
			}else{
				holder.imageViewIcon.setVisibility(View.VISIBLE);
				holder.imageViewIcon.setImageResource(currentNode.getIcon());
			}
			
			// ��ʾ�ı�
			holder.textViewText.setText(currentNode.getText());
			
			if(currentNode.isLeaf()){
				// ��Ҷ�ڵ� ����ʾչ�����۵�״̬ͼ��
				holder.imageViewExEc.setVisibility(View.GONE);
			}else{ 
				// ����ʱ�����ӽڵ�չ�����۵�,״̬ͼ��ı�
				holder.imageViewExEc.setVisibility(View.VISIBLE);
				if(currentNode.isExpanded()){
					if(expandedIcon != -1)
					holder.imageViewExEc.setImageResource(expandedIcon);
				}
				else {
					if(collapsedIcon != -1)
					holder.imageViewExEc.setImageResource(collapsedIcon);
				}				
			}
			
			// ��������
			convertView.setPadding(35*currentNode.getLevel(), 3, 3, 3);			
		}		
		return convertView;
	}

	/**
	 * 
	 * �б���ؼ�����
	 *
	 */
	public class ViewHolder{
		CheckBox checkBoxSelect;			//ѡ�б��
		ImageView imageViewIcon;			//�ڵ�ͼ��
		TextView textViewText;					//����˵��
		ImageView imageViewExEc;			//չ�����۵����">"��"v"
	}
}
