package com.cuc.miti.phone.xmc.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.cuc.miti.phone.xmc.domain.Enums.Readertype;
/**
 * @Description OA��ϢMessage
 * @author caojuan
 * 
 */
public class MessageForOa  implements Parcelable {
	private int id;
	private String info_title;
	private String writer;
	private String department;
	private String publishtime;
	private String infocontent;
	private Readertype readertype;
	private String inforeader;
	private String infomobile;
	
	public MessageForOa() {
		super();
	}
	public MessageForOa(int id, String infoTitle, String writer,
			String department, String publishtime, String infocontent,
			Readertype readertype, String inforeader, String infomobile) {
		super();
		this.id = id;
		info_title = infoTitle;
		this.writer = writer;
		this.department = department;
		this.publishtime = publishtime;
		this.infocontent = infocontent;
		this.readertype = readertype;
		this.inforeader = inforeader;
		this.infomobile = infomobile;
	}
	@Override
	public String toString() {
		return "MessageForOa [department=" + department + ", id=" + id
				+ ", info_title=" + info_title + ", infocontent=" + infocontent
				+ ", infomobile=" + infomobile + ", inforeader=" + inforeader
				+ ", publishtime=" + publishtime + ", readertype=" + readertype
				+ ", writer=" + writer + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInfo_title() {
		return info_title;
	}
	public void setInfo_title(String infoTitle) {
		info_title = infoTitle;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}
	public String getInfocontent() {
		return infocontent;
	}
	public void setInfocontent(String infocontent) {
		this.infocontent = infocontent;
	}
	public Readertype getReadertype() {
		return readertype;
	}
	public void setReadertype(Readertype readertype) {
		this.readertype = readertype;
	}
	public String getInforeader() {
		return inforeader;
	}
	public void setInforeader(String inforeader) {
		this.inforeader = inforeader;
	}
	public String getInfomobile() {
		return infomobile;
	}
	public void setInfomobile(String infomobile) {
		this.infomobile = infomobile;
	}
	 // 1.����ʵ��Parcelable.Creator�ӿ�,�����ڻ�ȡPerson��ݵ�ʱ�򣬻ᱨ�?���£�
	 // android.os.BadParcelableException:
	// Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
	// 2.����ӿ�ʵ���˴�Percel������ȡPerson��ݣ�������Person������߼���ʹ��
	// 3.ʵ��Parcelable.Creator�ӿڶ��������ΪCREATOR������ͬ��ᱨ���������ᵽ�Ĵ?
	// 4.�ڶ�ȡParcel�����������£����밴��Ա����������˳���ȡ��ݣ���Ȼ����ֻ�ȡ��ݳ���
	// 5.�����л�����
	public static final Parcelable.Creator<MessageForOa> CREATOR = new Creator<MessageForOa>() {
		
		public MessageForOa[] newArray(int size) {
			return new MessageForOa[size];

		}
		
		public MessageForOa createFromParcel(Parcel source) {
			// ���밴��Ա����������˳���ȡ��ݣ���Ȼ����ֻ�ȡ��ݳ���
			MessageForOa mOa = new MessageForOa();
			mOa.setId(source.readInt());
			mOa.setInfo_title(source.readString());
			mOa.setWriter(source.readString());
			mOa.setDepartment(source.readString());
			mOa.setPublishtime(source.readString());
			mOa.setInfocontent(source.readString());
			mOa.setReadertype(source.readString().equals("")?null:Readertype.valueOf(source.readString()));
			mOa.setInforeader(source.readString());
			mOa.setInfomobile(source.readString());
			return mOa;
		}
	};
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) { 
		// 1.���밴��Ա����������˳���װ��ݣ���Ȼ����ֻ�ȡ��ݳ���
		// 2.���л�����
		dest.writeInt(id);
		dest.writeString(info_title==null?"":info_title);
		dest.writeString(writer==null?"":writer);
		dest.writeString(department==null?"":department);
		dest.writeString(publishtime==null?"":publishtime);
		dest.writeString(infocontent==null?"":infocontent);
		dest.writeString(readertype==null?"":readertype.toString());
		dest.writeString(inforeader==null?"":inforeader);
		dest.writeString(infomobile==null?"":infomobile);

	}
	

}
