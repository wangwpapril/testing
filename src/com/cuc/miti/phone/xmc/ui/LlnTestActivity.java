package com.cuc.miti.phone.xmc.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import com.cuc.miti.phone.xmc.domain.ComeFromAddress;
import com.cuc.miti.phone.xmc.domain.Enums.BaseDataFileType;
import com.cuc.miti.phone.xmc.domain.Language;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.ProvideType;
import com.cuc.miti.phone.xmc.domain.Region;
import com.cuc.miti.phone.xmc.domain.SendToAddress;
import com.cuc.miti.phone.xmc.logic.BaseDataService;
import com.cuc.miti.phone.xmc.logic.ComeFromAddressService;
import com.cuc.miti.phone.xmc.logic.LanguageService;
import com.cuc.miti.phone.xmc.logic.NewsCategoryService;
import com.cuc.miti.phone.xmc.logic.NewsPriorityService;
import com.cuc.miti.phone.xmc.logic.NewsTypeService;
import com.cuc.miti.phone.xmc.logic.PlaceService;
import com.cuc.miti.phone.xmc.logic.ProvideTypeService;
import com.cuc.miti.phone.xmc.logic.RegionService;
import com.cuc.miti.phone.xmc.logic.SendToAddressService;
import com.cuc.miti.phone.xmc.utils.DBManager;
import com.cuc.miti.phone.xmc.utils.Logger;
import com.cuc.miti.phone.xmc.utils.ToastHelper;
import com.cuc.miti.phone.xmc.utils.XMLDataHandle;
import com.cuc.miti.phone.xmc.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LlnTestActivity extends BaseActivity implements OnClickListener{
	public static final String FILE_NAME ="topiclist.cnml-MediaType-1.xml";		//�ļ���
	public static final String PACKGE_NAME = "com.cuc.miti.phone.xmc";
	public static final String FILE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKGE_NAME + "/" + "files" ; 		//���ֻ������ļ���λ��
	
	
	private Button btnDelDB;
	private Button btnCreDB;
	private Button btnNewsType;
	private Button btnNewsTypeAdd;
	private Button btnNewsTypeDel;
	
	private Button btnPlace;
	private Button btnNewsCategory;
	private Button btnNewsPriority;
	private Button btnProvideType;
	private Button btnRegion;
	private Button btnComeFromAddress;
	private Button btnLanguage;
	private Button btnMTAmanager;
	
	
	NewsTypeService newsTypeServices=new NewsTypeService(LlnTestActivity.this);
	PlaceService placeServices=new PlaceService(LlnTestActivity.this);
	NewsCategoryService newsCategoryServices=new NewsCategoryService(LlnTestActivity.this);
	NewsPriorityService newsPriorityService=new NewsPriorityService(LlnTestActivity.this);
	ProvideTypeService provideTypeService=new ProvideTypeService(LlnTestActivity.this);
	RegionService regionService=new RegionService(LlnTestActivity.this);
	ComeFromAddressService comeFromAddressService=new ComeFromAddressService(LlnTestActivity.this);
	LanguageService languageService=new LanguageService(LlnTestActivity.this);
	BaseDataService bdService = new BaseDataService(LlnTestActivity.this);
	
	public DBManager db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.llnatest);
		
		db =  new DBManager(LlnTestActivity.this);
		
		
		
		
		btnDelDB=(Button)this.findViewById(R.id.btnDelDB);
		btnCreDB=(Button)this.findViewById(R.id.btnCreDB);
		
		btnNewsType=(Button)this.findViewById(R.id.btnNewsType);
		btnNewsTypeAdd=(Button)this.findViewById(R.id.btnNewsTypeAdd);
		btnNewsTypeDel=(Button)this.findViewById(R.id.btnNewsTypeDel);
		
		btnPlace=(Button)this.findViewById(R.id.btnPlace);
		btnNewsCategory=(Button)this.findViewById(R.id.btnNewsCategory);
		btnNewsPriority=(Button)this.findViewById(R.id.btnNewsPriority);
		btnProvideType=(Button)this.findViewById(R.id.btnProvideType);
		btnRegion=(Button)this.findViewById(R.id.btnRegion);
		btnComeFromAddress=(Button)this.findViewById(R.id.btnComeFromAddress);
		btnLanguage=(Button)this.findViewById(R.id.btnLanguage);
		btnMTAmanager=(Button)this.findViewById(R.id.btnMTAmanager);
		
		btnDelDB.setOnClickListener(this);
		btnCreDB.setOnClickListener(this);
		
		btnNewsType.setOnClickListener(this);
		btnNewsTypeAdd.setOnClickListener(this);
		btnNewsTypeDel.setOnClickListener(this);
		
		btnPlace.setOnClickListener(this);		
		btnNewsCategory.setOnClickListener(this);		
		btnNewsPriority.setOnClickListener(this);		
		btnProvideType.setOnClickListener(this);
		btnRegion.setOnClickListener(this);
		btnComeFromAddress.setOnClickListener(this);
		btnLanguage.setOnClickListener(this);
		btnMTAmanager.setOnClickListener(this);
		
	}
	
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.btnDelDB:

			db.deleteDatabase();

			break;
		case R.id.btnCreDB:
			
			try {
				db.createDatabase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.btnNewsTypeDel:
			
			db.deleteFile();
			
			break;
			
			
		case R.id.btnNewsType:
			try {
				SendToAddressService sendToAddressService = new SendToAddressService(LlnTestActivity.this);
				InputStream is = getAssets().open("topiclist.SendAddress-2.xml");
				List<SendToAddress> sendToAddresseList = sendToAddressService.GetSendToAddressDataFromXMLFile(is);
				
				sendToAddressService.deleteAllSendToAddress();
				sendToAddressService.multiInsert(sendToAddresseList);
				
				bdService.SetCurrentBDFileName(BaseDataFileType.SendAddress, "topiclist.SendAddress-2.xml");
			} catch (Exception e) {
				Log.e("eNews", e.getMessage());
			}
			
//			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
//				InputStream is = getAssets().open("topiclist.cnml-XH_InternalInternational-4.xml");
//				
//				List<NewsType> newsTypeList =newsTypeServices.GetNewsTypeFromXMLFile(is);
//				
//				
//				newsTypeServices.multiInsert(newsTypeList);
//				bdService.SetCurrentBDFileName(BaseDataFileType.XH_InternalInternational, "topiclist.cnml-XH_InternalInternational-4.xml");
//				
				/*for(NewsCategory nc:newsCategoryList)
				{
					newsCategoryServices.addNewsCategory(nc);
					
				}*/
				
				
//			} catch (IOException ex) {
//				Logger.e(ex);
//				ex.printStackTrace();
//			}
//			
			
			break;
		case R.id.btnNewsCategory:
			try {
			InputStream is = getAssets().open("topiclist.cnml-XH_NewsCategory-7.xml");
			
			List<NewsCategory> ncList = newsCategoryServices.GetNewsCategoryDataFromXMLFile(is);
			
			newsCategoryServices.multiInsert(ncList);
			bdService.SetCurrentBDFileName(BaseDataFileType.XH_NewsCategory, "topiclist.cnml-XH_NewsCategory-7.xml");
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			break;
		case R.id.btnPlace:
			
			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
				InputStream is = getAssets().open("topiclist.cnml-WorldLocationCategory-1.xml");
				
				List<Place> placeList = placeServices.GetPlaceFromXMLFile(is);
				
				placeServices.multiInsert(placeList);
				bdService.SetCurrentBDFileName(BaseDataFileType.WorldLocationCategory, "topiclist.cnml-WorldLocationCategory-1.xml");
				
			/*
				for(Place p:placeList)
				{
					placeServices.addPlace(p);
					
				}
				*/
				
				
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			
			
			break;
		case R.id.btnNewsPriority:
			
			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
				InputStream is = getAssets().open("topiclist.cnml-Priority-1.xml");
				
				List<NewsPriority> newsPriorityList = newsPriorityService.GetNewsPriorityFromXMLFile(is);
				
				newsPriorityService.multiInsert(newsPriorityList);
				bdService.SetCurrentBDFileName(BaseDataFileType.Priority, "topiclist.cnml-Priority-1.xml");
				
				/*for(NewsPriority np:newsPriorityList)
				{
					newsPriorityService.addNewsPriority(np);
					
				}*/
				
				
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			
			
			break;
			
		case R.id.btnProvideType:
			
			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
				InputStream is = getAssets().open("topiclist.cnml-XH_InternalInternational-4.xml");
				
				List<ProvideType> provideTypeList = provideTypeService.GetProvideTypeFromXMLFile(is);
				
				provideTypeService.multiInsert(provideTypeList);
				bdService.SetCurrentBDFileName(BaseDataFileType.XH_InternalInternational, "topiclist.cnml-XH_InternalInternational-4.xml");
				
				
				/*for(ProvideType pt:provideTypeList)
				{
					provideTypeService.addProvideType(pt);
					
				}*/
				
				
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			
			
			break;
			
		case R.id.btnRegion:
			
			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
				InputStream is = getAssets().open("topiclist.cnml-XH_GeographyCategory-5.xml");
				
				List<Region> regionList = regionService.GetRegionFromXMLFile(is);
				
				regionService.multiInsert(regionList);
				bdService.SetCurrentBDFileName(BaseDataFileType.XH_GeographyCategory, "topiclist.cnml-XH_GeographyCategory-5.xml");
				
				
				/*for(Region r:regionList)
				{
					regionService.addRegion(r);
					
				}*/
				
				
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			
			
			break;
				
		case R.id.btnComeFromAddress:
			
			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
				InputStream is = getAssets().open("topiclist.cnml-Department-85.xml");
				
				List<ComeFromAddress> comeFromAddressList = comeFromAddressService.GetComeFromAddressFromXMLFile(is);
				
				comeFromAddressService.multiInsert(comeFromAddressList);
				bdService.SetCurrentBDFileName(BaseDataFileType.Department, "topiclist.cnml-Department-85.xml");			
				
				/*for(ComeFromAddress ca:comeFromAddressList)
				{
					comeFromAddressService.addComeFromAddress(ca);
					
				}*/
				
				
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			
			
			break;
			
		case R.id.btnLanguage:
			
			try {
				//String filePath=FILE_PATH + "/" +FILE_NAME;
				//List<NewsType> newsTypeList = newsTypeServices.GetNewsTypeFromXMLFile(filePath);
				
				InputStream is = getAssets().open("topiclist.cnml-Language-7.xml");
				
				List<Language> languageList = languageService.GetLanguageFromXMLFile(is);
				
				languageService.multiInsert(languageList);
				bdService.SetCurrentBDFileName(BaseDataFileType.Language, "topiclist.cnml-Language-7.xml");			
				
			} catch (IOException ex) {
				Logger.e(ex);
				ex.printStackTrace();
			}
			
			
			break;
			
		case R.id.btnMTAmanager:
			
			Intent mIntent = new Intent(LlnTestActivity.this,ManagementTemplateActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putString("requestType", "Manage");
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			
			break;
			
		default:
			break;
		}
		
		/*Toast toast = Toast.makeText(LlnTestActivity.this,
				"The Operation has completed", Toast.LENGTH_LONG);
		toast.show();*/
		ToastHelper.showToast("The Operation has completed",Toast.LENGTH_SHORT);
}
}
