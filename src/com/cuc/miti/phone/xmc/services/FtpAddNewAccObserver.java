package com.cuc.miti.phone.xmc.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.cuc.miti.phone.xmc.R;
import com.cuc.miti.phone.xmc.IngleApplication;
import com.cuc.miti.phone.xmc.dao.SendToAddressDao;
import com.cuc.miti.phone.xmc.domain.Accessories;
import com.cuc.miti.phone.xmc.domain.Enums.AccessoryType;
import com.cuc.miti.phone.xmc.domain.Enums.PreferenceKeys;
import com.cuc.miti.phone.xmc.domain.Enums.TemplateType;
import com.cuc.miti.phone.xmc.domain.KeyValueData;
import com.cuc.miti.phone.xmc.domain.ManuscriptTemplate;
import com.cuc.miti.phone.xmc.domain.Manuscripts;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.domain.User;
import com.cuc.miti.phone.xmc.domain.XmcException;
import com.cuc.miti.phone.xmc.http.RemoteCaller;
import com.cuc.miti.phone.xmc.logic.AccessoriesService;
import com.cuc.miti.phone.xmc.logic.ManuscriptTemplateService;
import com.cuc.miti.phone.xmc.logic.ManuscriptsService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.ui.MainActivity;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.MediaHelper;
import com.cuc.miti.phone.xmc.utils.SendManuscriptsHelper;
import com.cuc.miti.phone.xmc.utils.SharedPreferencesHelper;
import com.cuc.miti.phone.xmc.utils.StandardizationDataHelper;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.Utils;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;

public class FtpAddNewAccObserver extends FileObserver {

	private static final int MSG_Add_One = 1;
	private List<String> lstFile = new ArrayList<String>();  //��� List 
	private String PathG;
	private Context mContext;
	private MessageHandler mHandler=null;
	private ManuscriptsService mservice=null; 
	public Manuscripts manuscripts = null;										// ��ǰ�༭�еĸ������
	private List<Accessories> accessories = null;								// ��ǰ��������б�
	private ManuscriptTemplateService mtService = null;
	private ManuscriptTemplate mManuTemplate = null;
	AccessoriesService accService=null;
	SharedPreferencesHelper sharedPreferencesHelper;
	String fileName = "ftpusername";
	InputStream is = null;
	List<KeyValueData> objs = new ArrayList<KeyValueData>();
	String instantAccAuthor="";
	private String sessionId = "";
	private String loginname = "";
	private SendToAddressService sendToAddressService;
	
	public FtpAddNewAccObserver(String path,Context context) {
		super(path);
		this.PathG=path;
		this.mContext = context;
		this.mHandler=new MessageHandler();
		mservice = new ManuscriptsService(mContext);
		mtService=new ManuscriptTemplateService(mContext);
		accService = new AccessoriesService(mContext);
		sendToAddressService = new SendToAddressService(mContext);
		sharedPreferencesHelper = new SharedPreferencesHelper(mContext);
		sessionId=IngleApplication.getSessionId();
		loginname=IngleApplication.getInstance().getCurrentUser();
	}

	/**
	 * ��ʼ���ڴ��еĸ������
	 */
	private void iniManuscript() {
		manuscripts = new Manuscripts();									//�������
		accessories = new ArrayList<Accessories>();					//������������б�

		//������ĸ��guid
		manuscripts.setM_id(UUID.randomUUID().toString());
				
		//��ȡ��ǰ�û���Ϣ
		User user = IngleApplication.getInstance().currentUserInfo;
		manuscripts.setGroupcode(user.getUserattribute().getGroupCode());
		manuscripts.setGroupnameC(user.getUserattribute().getGroupNameC());
		manuscripts.setGroupnameE(user.getUserattribute().getGroupNameE());
		manuscripts.setUsernameC(user.getUserattribute().getUserNameC());
		manuscripts.setUsernameE(user.getUserattribute().getUserNameE());
		manuscripts.setLoginname(user.getUsername());

		// ���ø�ǩģ��
		mManuTemplate = mtService.getManuscriptTemplateSystem(IngleApplication.getInstance().getCurrentUser(),TemplateType.INSTANT.toString());
		
		//TODO ����ط�ΪʲôҪ��content��send���Ը�ֵ��������ڣ�
		//this.setContent_SendControlValue();	
		
		manuscripts.setManuscriptTemplate(mManuTemplate);
		
		//manuscripts.setAuthor(mManuTemplate.getAuthor());	
		
		manuscripts.getManuscriptTemplate().setComefromDept(user.getUserattribute().getGroupNameC());
		manuscripts.getManuscriptTemplate().setComefromDeptID(user.getUserattribute().getGroupCode());
		
		//�ڷ���ǰ��Ҫ��У�����û��Լ����õĵ�ַ�Ƿ񻹴�����ϵͳ�������Ȩ�޷�Χ�ڣ��統�û����ü��ļ�����ַʱ�С�address1�����������ʹ�ü��ļ�������ʱ���˵�ַ�Ѳ����ڣ�
		String sendToAddress=manuscripts.getManuscriptTemplate().getAddress();
		String[] address = null;
		List<String> addressList=null;
		List<String> addressAll=RemoteCaller.addressAll;
		StringBuilder strAddress = new StringBuilder();
		
		if (StringUtils.isNotBlank(sendToAddress)) {
			
			if("".equals(sendToAddress)==false){
				addressList=new ArrayList<String>();
				address = sendToAddress.split(",");
				for(int i=0;i<address.length;i++)
				{
					addressList.add(address[i]);
				}
			}
		}
		
		if(addressList.size()>0&&addressList!=null)
		{
			manuscripts.getManuscriptTemplate().setAddress("");//����������Ϊ�գ��ٽ������ݵ����
			
			SendToAddress sta=null;
			
			for(int j=0;j<addressList.size();j++)
			{
				for(int k=0;k<addressAll.size();k++){
					sta=sendToAddressService.getSendToAddressByCode("zh-CHS",addressAll.get(k));
					if(addressList.get(j).equals(sta.getName())){
						strAddress.append(addressList.get(j)).append(",");
					}
				}
			}
			
			manuscripts.getManuscriptTemplate().setAddress(strAddress.substring(0,strAddress.length()-1).toString());
			
		}else{
			manuscripts.getManuscriptTemplate().setAddress("");
			}
		
		
//************************************************end
		if("".equals(instantAccAuthor))
		{
			//instantAccAuthor=user.getUsername();//ԭ�����ô˷����ģ���������Ҫʵ���ܹ��޸����ߵĹ���
			instantAccAuthor=manuscripts.getManuscriptTemplate().getAuthor();
		}
		//��ǩ�͸����author���ĳɼ��ļ�����author
		manuscripts.getManuscriptTemplate().setAuthor(instantAccAuthor);
		manuscripts.setAuthor(instantAccAuthor);
		manuscripts.setTitle("���ļ���");
		manuscripts.setContents("���ļ���");
	}
	
	@Override
	public void onEvent(int event, String path) {	
		switch (event) {
			case FileObserver.CLOSE_WRITE:
				/*//22088219871204282X.jpg
				this.notifyMediaScanToFindThePic(path);			//֪ͨ��ᷢ����Ƭ
				//ȡ�á����ļ�����ȡֵ����Ϊ�������̽����ļ����Ͳ���  FTP ����ģʽ
				//String selectedValue = sharedPreferencesHelper.GetUserPreferenceValue(PreferenceKeys.instantUpload.toString());	
				
				//���Ƚ��е�ǰ��¼�û���У�飬�����Ƿ����Ȩ�޽��м��ļ�������
				try {
				if(RemoteCaller.validateInstantUpload(sessionId,loginname)){
				//if(!selectedValue.equals("")){
					//if("true".equals(selectedValue)){
								if(Integer.parseInt(MediaHelper.getFileSize(MediaHelper.copy2TempStoreWithOriginalName(PathG+path)))>0)//���Ƚ����ж��Ƿ��и������û��Ƿ�ȡ���ļ���������
								{
									this.createManuscript(path);								//��������
								}
						//	}
					//	}
					}else{
					ToastHelper.showToast("��ǰ��¼�û��޼��ļ���Ȩ��",Toast.LENGTH_SHORT);
				}
				} catch (Exception e) {
					Logger.e(e);
					ToastHelper.showToast(e.getMessage(),Toast.LENGTH_SHORT);
					e.printStackTrace();
				}*/
				break;
			default:
				break;
		}	
	}
	
	private void notifyMediaScanToFindThePic(String path){
		
		Uri uri = Uri.parse(PathG+path);
		Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
		mContext.sendBroadcast(localIntent);
	}
	
	/**
	 * ��⵽FTP�ļ��������µ��ļ�������ɸ��
	 * @param path
	 */
	private void createManuscript(String path){
		
		 String authorOfAcc="";
		 char firstChar=path.charAt(0);
		 if('_'==firstChar)
		 {
			 authorOfAcc=path.substring(1, 4).toLowerCase();     //�ϴ�ͼƬ����Ƶ������
		 }else{
			 authorOfAcc=path.substring(0, 3).toLowerCase();     //�ϴ�ͼƬ����Ƶ������
		 }
		
		getAuthorOfAccList();                                                     //��ȡ�����Ѵ��ڵ������б�

		if(objs!=null&&objs.size()>0){
			for(int i=0;i<objs.size();i++)
			{
				//����б��д��ڴ��ϴ������û�����Ѹ�ǩ�͸���е��û����Ϊ��author����
				if(authorOfAcc.equals(objs.get(i).getKey()))                     
				{
					instantAccAuthor=objs.get(i).getValue();
				}
			}
		}
			
		iniManuscript();			
		mservice.addManuscripts(this.manuscripts);
		
		String filePath="";     		//ͼƬδ��ѹ��ǰ������·��
		String fileURLTD="";    		//ͼƬ��temp�ļ����µ�����·��
		String fileURLTDC="";     	//ͼƬ��temp�ļ����µı�ѹ���������·��
		filePath=PathG+path;
		
		String end = path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase();	
	        
	    if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")|| end.equals("bmp") || end.equals("tiff")|| end.equals("ico")|| end.equals("gif")|| end.equals("wav") || end.equals("mp3") || end.equals("wma"))  
	    { 
	    	try {
	    		fileURLTD = MediaHelper.copy2TempStoreWithOriginalName(filePath); //��ͼƬ�ȿ�����Temp�ļ����£�Ȼ�����ѹ������
	    		fileURLTDC=fileURLTD.substring(0,fileURLTD.lastIndexOf("."))+"_resized."+end;
	    		AccessoryType accType = null;
	    		Accessories acc = null;
	    		if(!end.equals("wav")&&!end.equals("mp3")&&!end.equals("wma")){//ֻ����ͼƬʱ�Ž���ѹ������Ƶ����Ҫ����ѹ������
	    			fileURLTDC=Utils.CompressBitmap(fileURLTDC,fileURLTD); 
	    			if (fileURLTDC.equals(""))
	    				return;
	    			// ���·����ý������������ɸ�������
					accType = MediaHelper.checkFileType(fileURLTDC);
					acc = populateAccessorie(fileURLTDC, accType);
				}else{
					if (fileURLTD.equals(""))
						return;
					// ���·����ý������������ɸ�������
					accType = MediaHelper.checkFileType(fileURLTD);
					acc = populateAccessorie(fileURLTD, accType);
					
				}
				// ���渽��
				saveAcc(acc);
				accessories.add(acc);
	    	} catch (Exception e) {
					e.printStackTrace();
			}
	        	
	        KeyValueData resultMessage = new KeyValueData("", "");
			boolean result = SendManuscriptsHelper.validateForReleManuscript(manuscripts, resultMessage, mContext);
			
			//ȡ�á����ļ�����ȡֵ����Ϊ�������̽����ļ����Ͳ���  FTP ����ģʽ
			String selectedValue = sharedPreferencesHelper.GetUserPreferenceValue(PreferenceKeys.instantUpload.toString());
			if (result == true) {
				
				if(!selectedValue.equals("")){
					if("true".equals(selectedValue)){
				
				        if (sent()) {
				        	showMessage("�����֤��ϣ��ѽ��뷢�Ͷ��У�");
				        } else {
							showMessage("����ʧ�ܣ�δ֪����");
						}
					}else{
						//�Ѹ�����浽�ڱ����б���
						save();
						showMessage("δ�������ļ������ܣ�����ѱ��浽�ڱ��б���");
						}
					}
			}
		} 
	}
	
	//ͼƬѹ������
//	private void CompressBitmap(String fileURLTDC,String fileURLTD) {
//		OutputStream os;
//		try {
//			
//			BitmapFactory.Options options = new BitmapFactory.Options();
////			options.inJustDecodeBounds = true;
//			// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull
////			Bitmap bitmap = BitmapFactory.decodeFile(fileURLTD, options);
//						
//			String compressRatio=sharedPreferencesHelper.GetUserPreferenceValue(PreferenceKeys.User_UploadPicCompressRatio.toString());
//			
//			int ratio=100/Integer.parseInt(compressRatio);
//			
//			options.inSampleSize = ratio;
//			os = new FileOutputStream(fileURLTDC);
//			
////			int outputwidth = options.outWidth/ratio;
////			int outputheight = options.outHeight/ratio;
//			
//			options.inJustDecodeBounds = false;
//			Bitmap resizedBitmap = BitmapFactory.decodeFile(fileURLTD, options);
//			//resizedBitmap = ThumbnailUtils.extractThumbnail(resizedBitmap, outputwidth,outputheight,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//			resizedBitmap.compress(CompressFormat.JPEG, 100, os);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
//	}

	private void sendMessage(int what, Object obj) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessageDelayed(msg, 500);
	}
	private class MessageHandler extends Handler {
		// This method is used to handle received messages
		public void handleMessage(Message msg) {

			switch (msg.what) {				
				case MSG_Add_One:
					//Toast.makeText(FragmentView.getContext(),"��Ϣ������ϣ�",Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(mContext, MainActivity.class);			
					mContext.startActivity(intent);
					
					break;	
				default:
					break;
			}
			}
		}
	  
	/**
	 * ���͸��
	 */
	protected boolean sent() {
		try {
			return mservice.sendNormalManuscripts(this.manuscripts, this.accessories);
		} catch (Exception e) {
			Logger.e(e);
			return false;
		}

	}
	
	/**
	 * ������������ҪУ��
	 */
	protected void save() {
		mservice.updateManuscripts(this.manuscripts);
	}
	
	/**
	 * ��ʾ�����Ϣ
	 * 
	 * @param message
	 */
	protected void showMessage(String message) {
		ToastHelper.showToast(message, Toast.LENGTH_SHORT);
	}
	
	/**
	 * ���Accessories����
	 * 
	 * @param accPath
	 *            ����·��
	 * @param accType
	 *            ��������
	 * @return ��������
	 */
	private Accessories populateAccessorie(String accPath, AccessoryType accType) {

		Accessories accessorie = new Accessories();
		accessorie.setA_id(UUID.randomUUID().toString());
		accessorie.setM_id(this.manuscripts.getM_id());
		accessorie.setType(accType.toString());

		try {
			accessorie.setOriginalName(MediaHelper.getFileName(accPath));
			accessorie.setSize(MediaHelper.getFileSize(accPath));
		} catch (IOException ex) {
			accessorie.setOriginalName("");
			accessorie.setSize("");
			Logger.e(ex);

			showMessage(ToastHelper.getStringFromResources(R.string.filePathError));
		}

		// ͼƬ·��
		accessorie.setUrl(accPath);

		return accessorie;
	}
	
	/**
	 * ���渽������
	 */
	private void saveAcc(Accessories acc) {
		// ���ø�������ı���
		acc.setTitle(ToastHelper.getStringFromResources(R.string.title_UploadPicFM));
		// ���ø������������
		acc.setDesc(ToastHelper.getStringFromResources(R.string.title_UploadPicFM));
		// ���浽��ݿ�		
		accService.addAccessories(acc);			
	}
	
	public List<String> GetFiles(String Path)  //����Ŀ¼�ļ��� �µ��ļ�
	{ 
	    File[] files = new File(Path).listFiles(); 
	    if(files !=null&&files.length>0){
	    
	   
	    for (int i = 0; i < files.length; i++) 
	    { 
	        File f = files[i]; 
	        
	        String end = f.getPath().substring(f.getPath().lastIndexOf(".") + 1, f.getPath().length()).toLowerCase();	
	        
	        f.lastModified();
	        
	        if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
					|| end.equals("bmp") || end.equals("tiff")|| end.equals("ico")|| end.equals("gif"))  
	        { 
	        	lstFile.add(f.getPath());
	        } 
	    }
	    }
	    return lstFile;
	}
	
	
	public void getAuthorOfAccList()
	{
		String path = StandardizationDataHelper.getAccessoryFileUploadStorePath(AccessoryType.Cache);
    	File dataFile = null;
		dataFile = new File(path + "/" + fileName + ".xml");
		if(!dataFile.exists()){
			return ;
		}
		try {
			is = new FileInputStream(dataFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		List<KeyValueData> kvList = null;
		try {
			objs = XMLDataHandle.parserFtpUsername(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
