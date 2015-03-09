package com.cuc.miti.phone.xmc.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cuc.miti.phone.xmc.domain.Enums.OperationType;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

//�������
//@author GuanWei

public class Accessories implements Parcelable {
	// ��id ����
	private String a_id;
	// ���id
	private String m_id;
	// ����ʱ��
	private String createtime;
	// �ֱ���
	private String title;
	// ��˵��
	private String desc;
	// ������С
	private String size;
	// ��������
	private String type;
	// ԭʼ�ļ���
	private String originalName;
	// ��������
	private String info;

	// ·��
	private String url;
	
	private Bitmap image;
	
	private boolean choose;

	// ����
	//private OperationType operation;

	public Accessories() {
		this.a_id = "";
		m_id = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createtime = formatter.format(new Date());
		this.title = "";
		this.desc = "";
		this.size = "";
		this.type = "";
		this.originalName = "";
		this.info = "";
		this.url = "";
		this.choose=false;

	}

	public Accessories(String aId, String mId, String createtime, String title,
			String desc, String size, String type, String originalName,
			String info, String url) {
		super();
		a_id = aId;
		m_id = mId;
		this.createtime = createtime;
		this.title = title;
		this.desc = desc;
		this.size = size;
		this.type = type;
		this.originalName = originalName;
		this.info = info;
		this.url = url;
//		this.operation = OperationType.None;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getA_id() {
		return a_id;
	}

	public void setA_id(String aId) {
		a_id = aId;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setImage(Bitmap image){
		this.image = image;
	}
	
	public Bitmap getImage(){
		return this.image;
	}
	
//	public OperationType getOperation(){
//		return this.operation;
//	}
//	
//	public void setOperation(OperationType operation){
//		this.operation = operation;
//	}

	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "Accessories [a_id=" + a_id + ", createtime=" + createtime
				+ ", desc=" + desc + ", info=" + info + ", m_id=" + m_id
				+ ", originalName=" + originalName + ", size=" + size
				+ ", title=" + title + ", type=" + type + ", url=" + url + "]";
	}

	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(this.a_id);
		dest.writeString(this.createtime);
		dest.writeString(this.desc);
		dest.writeString(this.info);
		dest.writeString(this.m_id);
		dest.writeString(this.originalName);
		dest.writeString(this.size);
		dest.writeString(this.title);
		dest.writeString(this.type);
		dest.writeString(this.url);
//		dest.writeInt(this.operation.ordinal());
	}

	public boolean isChoose() {
		return choose;
	}

	public void setChoose(boolean choose) {
		this.choose = choose;
	}

	public static final Parcelable.Creator<Accessories> CREATOR = new Creator<Accessories>() {
		public Accessories[] newArray(int size) {

			return new Accessories[size];
		}

		public Accessories createFromParcel(Parcel source) {
			Accessories temp = new Accessories();

			temp.a_id = source.readString();
			temp.createtime = source.readString();
			temp.desc = source.readString();
			temp.info = source.readString();
			temp.m_id = source.readString();
			temp.originalName = source.readString();
			temp.size = source.readString();
			temp.title = source.readString();
			temp.type = source.readString();
			temp.url = source.readString();
//			temp.operation = OperationType.values()[source.readInt()];
			return temp;
		}
	};

}
