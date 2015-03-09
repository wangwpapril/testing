package com.cuc.miti.phone.xmc.adapter;

import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import com.cuc.miti.phone.xmc.domain.TreeNode;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class TreeSelectListViewStyle2Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<TreeNode> nodes;
	private Context mContext;
	private NodeClickListener nodeClickListener;

	public void setNodes(List<TreeNode> nodes) {
		this.nodes = nodes;
		this.notifyDataSetChanged();
	}

	/**
	 * ע�ᶩ����
	 * @param listener
	 */
	public void setNodeClickListener(NodeClickListener listener) {
		this.nodeClickListener = listener;
	}

	public TreeSelectListViewStyle2Adapter(List<TreeNode> nodes, Context context) {
		super();

		this.mContext = context;

		this.inflater = LayoutInflater.from(context);

		this.nodes = nodes;
	}

	public int getCount() {
		if (null != nodes) {
			return nodes.size();
		} else {
			return 0;
		}
	}

	public TreeNode getItem(int position) {
		return nodes.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.listview_item_for_treeselect_style2,
							parent, false);
		}

		TextView textViewText_lvITFTS2 = (TextView) convertView
				.findViewById(R.id.textViewText_lvITFTS2);
		final ImageButton ibtnExEc_lvITFTS2 = (ImageButton) convertView
				.findViewById(R.id.ibtnExEc_lvITFTS2);

		TreeNode currentNode = this.nodes.get(position);

		textViewText_lvITFTS2.setText(currentNode.getText());
		
		ibtnExEc_lvITFTS2.setFocusable(false);
		ibtnExEc_lvITFTS2.setClickable(false);
		ibtnExEc_lvITFTS2.setTag(position);

		if (currentNode.isLeaf()) {
			// ��Ҷ�ڵ� ����ʾչ�����۵�״̬ͼ��
			ibtnExEc_lvITFTS2.setVisibility(View.GONE);
		} else {
			// ����ʱ�����ӽڵ�չ�����۵�,״̬ͼ��ı�
			ibtnExEc_lvITFTS2.setVisibility(View.VISIBLE);

			ibtnExEc_lvITFTS2.setOnClickListener(new OnClickListener() {
				public void onClick(final View arg0) {
					int id = Integer.parseInt(ibtnExEc_lvITFTS2.getTag().toString());
					
					//��ɽڵ�ݽ�ť�����¼�����
					NodeClicktEvent event = new NodeClicktEvent(TreeSelectListViewStyle2Adapter.this.getItem(id));
					
					//�����¼���֪ͨ������
					try {
						nodeClickListener.onNodeClick(event);
					} catch (Exception e) {
						Logger.d(e.getMessage());
					}
					
				}
			});
		}

		return convertView;
	}

	/**
	 * ����һ�����������ݵ���ݽ�ť�¼�״̬��Ϣ
	 * 
	 * @author wenyujun
	 * 
	 */
	public class NodeClicktEvent extends EventObject {

		public TreeNode currentNode;

		public NodeClicktEvent(TreeNode source) {
			super(source);
			this.currentNode = source;
		}

	}

	/**
	 * ͨ��ӿ��������¼���Ӧ����ԭ��
	 * 
	 * @author wenyujun
	 * 
	 */
	public interface NodeClickListener extends EventListener {
		void onNodeClick(NodeClicktEvent e);
	}
}