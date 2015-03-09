package com.cuc.miti.phone.xmc.dao.test;

import java.util.List;

import com.cuc.miti.phone.xmc.dao.NewsCategoryDao;
import com.cuc.miti.phone.xmc.dao.NewsPriorityDao;
import com.cuc.miti.phone.xmc.dao.NewsTypeDao;
import com.cuc.miti.phone.xmc.dao.PlaceDao;
import com.cuc.miti.phone.xmc.dao.ProvideTypeDao;
import com.cuc.miti.phone.xmc.dao.RegionDao;
import com.cuc.miti.phone.xmc.dao.ServerAddressDao;
import com.cuc.miti.phone.xmc.domain.NewsCategory;
import com.cuc.miti.phone.xmc.domain.NewsPriority;
import com.cuc.miti.phone.xmc.domain.NewsType;
import com.cuc.miti.phone.xmc.domain.Pager;
import com.cuc.miti.phone.xmc.domain.Place;
import com.cuc.miti.phone.xmc.domain.ProvideType;
import com.cuc.miti.phone.xmc.domain.Region;
import com.cuc.miti.phone.xmc.domain.ServerAddress;

import android.test.AndroidTestCase;
import android.util.Log;

public class PlaceDaoTest extends AndroidTestCase{
	
	private static final String TAG="PlaceServices";
	private static final String TAG1 = "ProvideType";
	private static final String TAG2 = "Region";
	private static final String TAG3 = "NewsType";
	private static final String TAG4 = "ServerAddress";
	private static final String TAG5 = "NewsCategory";
	private static final String TAG6 = "NewsPriority";
	
	public void testAddPlaceDao(){
		PlaceDao placeDao = new PlaceDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			Place place = new Place();
			place.setCode("code" + i);
			place.setName("name" + i);
			place.setLanguage("language" + i);
			placeDao.addPlace(place);
		}
		List<Place> places = placeDao.getPlaceList();
		
		for(Place e:places){
			Log.i(TAG, e.toString());
		}
	}
	public void testDeletePlaceDao(){
		PlaceDao placeDao = new PlaceDao(this.getContext());
		
		placeDao.deletePlace("name1");
		
		List<Place> places = placeDao.getPlaceList();
		
		for(Place e:places){
			Log.i(TAG, e.toString());
		}
		
	}
	
	public void testUpdatePlaceDao(){
		PlaceDao placeDao = new PlaceDao(this.getContext());
		
		Place place = placeDao.getPlace("name0");
		
		place.setCode("updateCode0");
		place.setLanguage("updateLanguage0");
		placeDao.updatePlace(place);
		Log.i(TAG, place.toString());
	}
	
	public void testGetPlaceByPage(){
		PlaceDao placeDao = new PlaceDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(placeDao.getPlaceCount()+""));
		
		List<Place> places = placeDao.getPlaceByPage(pager);
		
		for(Place e:places){
			Log.i(TAG, e.toString());
		}
	}
	
	public void testAddProvideTypeDao(){
		ProvideTypeDao provideTypeDao = new ProvideTypeDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			ProvideType provideType = new ProvideType();
			provideType.setCode("code" + i);
			provideType.setName("name" + i);
			provideType.setLanguage("language" + i);
			provideTypeDao.addProvideType(provideType);
		}
		
		List<ProvideType> provideTypes = provideTypeDao.getProvideTypeList();
		
		for(ProvideType e:provideTypes){
			Log.i(TAG1, e.toString());
		}
	}
	
	public void testAddRegionDao(){
		RegionDao regionDao = new RegionDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			Region region = new Region();
			region.setCode("code" + i);
			region.setName("name" + i);
			region.setLanguage("language" + i);
			regionDao.addRegion(region);
		}
		
		List<Region> regions = regionDao.getRegionList();
		
		for(Region e:regions){
			Log.i(TAG2, e.toString());
		}
	}
	
	public void testAddNewsTypeDao(){
		NewsTypeDao newsTypeDao = new NewsTypeDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			NewsType newsType = new NewsType();
			newsType.setCode("code" + i);
			newsType.setName("name" + i);
			newsType.setLanguage("language" + i);
			newsTypeDao.addNewsType(newsType);
		}
		
		List<NewsType> newsTypes = newsTypeDao.getNewsTypeList();
		
		for(NewsType e:newsTypes){
			Log.i(TAG3, e.toString());
		}
	}
	
	public void testAddServerAddressDao(){
		ServerAddressDao serverAddressDao = new ServerAddressDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			ServerAddress serverAddress = new ServerAddress();
			serverAddress.setCode("code" + i);
			serverAddress.setName("name" + i);
			serverAddress.setLanguage("language" + i);
			serverAddressDao.addServerAddress(serverAddress);

		}
		
		List<ServerAddress> serverAddresses = serverAddressDao.getServerAddressList();
		
		for(ServerAddress e:serverAddresses){
			Log.i(TAG4, e.toString());
		}
	}
	
	
	public void testDeleteProvideTypeDao(){
		ProvideTypeDao provideTypeDao = new ProvideTypeDao(this.getContext());
		
		provideTypeDao.deleteProvideType("name1");
		
		List<ProvideType> provideTypes = provideTypeDao.getProvideTypeList();
		
		for(ProvideType e:provideTypes){
			Log.i(TAG1, e.toString());
		}
		
	}
	public void testDeleteRegionDao(){
		RegionDao regionDao = new RegionDao(this.getContext());
		
		regionDao.deleteRegion("name2");
		
		List<Region> regions = regionDao.getRegionList();
		
		for(Region e:regions){
			Log.i(TAG2, e.toString());
		}
		
	}
	
	
	public void testDeleteNewsTypeDao(){
		NewsTypeDao newsTypeDao = new NewsTypeDao(this.getContext());
		
		newsTypeDao.deleteNewsType("name1");
		
		List<NewsType> newsTypes = newsTypeDao.getNewsTypeList();
		
		for(NewsType e:newsTypes){
			Log.i(TAG3, e.toString());
		}
		
	}
	
	public void testDeleteServerAddressDao(){
		ServerAddressDao serverAddressDao = new ServerAddressDao(this.getContext());
		
		serverAddressDao.deleteServerAddress("name1");
		
		List<ServerAddress> serverAddresses = serverAddressDao.getServerAddressList();
		
		for(ServerAddress e:serverAddresses){
			Log.i(TAG4, e.toString());
		}
		
	}
	
	
	public void testUpdateProvideTypeDao(){
		ProvideTypeDao provideTypeDao = new ProvideTypeDao(this.getContext());
		
		ProvideType provideType = provideTypeDao.getProvideType("name0");
		
		provideType.setCode("updateCode0");
		provideType.setLanguage("updateLanguage0");
		provideTypeDao.updateProvideType(provideType);
		Log.i(TAG1, provideType.toString());
	}
	
	public void testUpdateRegionDao(){
		RegionDao regionDao = new RegionDao(this.getContext());
		
		Region region = regionDao.getRegion("name0");
		
		region.setCode("updateCode0");
		region.setLanguage("updateLanguage0");
		regionDao.updateRegion(region);
		Log.i(TAG2, region.toString());
	}
	
	public void testUpdateNewsTypeDao(){
		NewsTypeDao newsTypeDao = new NewsTypeDao(this.getContext());
		
		NewsType newsType = newsTypeDao.getNewsType("name0");
		
		newsType.setCode("updateCode0");
		newsType.setLanguage("updateLanguage0");
		newsTypeDao.updateNewsType(newsType);
		Log.i(TAG3, newsType.toString());
	}
	
	public void testUpdateServerAddressDao(){
		ServerAddressDao serverAddressDao = new ServerAddressDao(this.getContext());
		
		ServerAddress serverAddress = serverAddressDao.getServerAddress("name0");
		
		serverAddress.setCode("updateCode0");
		serverAddress.setLanguage("updateLanguage0");
		serverAddressDao.updateServerAddress(serverAddress);
		Log.i(TAG4, serverAddress.toString());
	}
	
	public void testGetPlace(){
		PlaceDao placeDao = new PlaceDao(this.getContext());
		
		Place place = placeDao.getPlace("name1");
		Log.i(TAG, place.toString());
	}
	
	public void testGetProvideType(){
		ProvideTypeDao provideTypeDao = new ProvideTypeDao(this.getContext());
		
		ProvideType provideType = provideTypeDao.getProvideType("name0");
		Log.i(TAG1, provideType.toString());
	}
	
	public void testGetRegion(){
		RegionDao regionDao = new RegionDao(this.getContext());
		
		Region region = regionDao.getRegion("name0");
		Log.i(TAG2, region.toString());
	}
	
	public void testGetPlaceList(){
		PlaceDao placeDao = new PlaceDao(this.getContext());
		
		List<Place> places = placeDao.getPlaceList();
		
		for(Place e:places){
			Log.i(TAG, e.toString());
		}
	}
	public void testGetProvideTypeList(){
		ProvideTypeDao provideTypeDao = new ProvideTypeDao(this.getContext());
		List<ProvideType> provideTypes = provideTypeDao.getProvideTypeList();
		
		for(ProvideType e:provideTypes){
			Log.i(TAG1, e.toString());
		}
		
	}
	public void testGetRegionList(){
		RegionDao regionDao = new RegionDao(this.getContext());
		
		List<Region> regions = regionDao.getRegionList();
		
		for(Region e:regions){
			Log.i(TAG2, e.toString());
		}
		
	}
	
	
	public void testGetProvideTypeByPage(){
		ProvideTypeDao provideTypeDao = new ProvideTypeDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(provideTypeDao.getProvideTypeCount()+""));
		
		List<ProvideType> provideTypes = provideTypeDao.getProvideTypeByPage(pager);
		
		for(ProvideType e:provideTypes){
			Log.i(TAG1, e.toString());
		}
	}
	public void testGetRegionByPage(){
		RegionDao regionDao = new RegionDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(regionDao.getRegionCount()+""));
		
		List<Region> regions = regionDao.getRegionByPage(pager);
		
		for(Region e:regions){
			Log.i(TAG2, e.toString());
		}
	}
	public void testGetNewsTypeByPage(){
		NewsTypeDao newsTypeDao = new NewsTypeDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(newsTypeDao.getNewsTypeCount()+""));
		
		List<NewsType> newsTypes = newsTypeDao.getNewsTypeByPage(pager);
		
		for(NewsType e:newsTypes){
			Log.i(TAG3, e.toString());
		}
	}
	public void testGetServerAddressByPage(){
		ServerAddressDao serverAddressDao = new ServerAddressDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(serverAddressDao.getServerAddressCount()+""));
		
		List<ServerAddress> serverAddresses = serverAddressDao.getServerAddressByPage(pager);
		
		for(ServerAddress e:serverAddresses){
			Log.i(TAG4, e.toString());
		}
	}
	
	public void testAddNewsCategoryDao(){
		NewsCategoryDao newsCategoryDao = new NewsCategoryDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			NewsCategory newsCategory = new NewsCategory();
			newsCategory.setCode("code" + i);
			newsCategory.setName("name" + i);
			newsCategory.setLanguage("language" + i);
			newsCategoryDao.addNewsCategory(newsCategory);
		}
		List<NewsCategory> newsCategorys = newsCategoryDao.getNewsCategoryList();
		
		for(NewsCategory e:newsCategorys){
			Log.i(TAG5, e.toString());
		}
	}
	public void testDeleteNewsCategoryDao(){
		NewsCategoryDao newsCategoryDao = new NewsCategoryDao(this.getContext());
		
		newsCategoryDao.deleteNewsCategory("name1");
		
		List<NewsCategory> newsCategorys = newsCategoryDao.getNewsCategoryList();
		
		for(NewsCategory e:newsCategorys){
			Log.i(TAG5, e.toString());
		}
		
	}
	
	public void testUpdateNewsCategoryDao(){
		NewsCategoryDao newsCategoryDao = new NewsCategoryDao(this.getContext());
		
		NewsCategory newsCategory = newsCategoryDao.getNewsCategory("name0");
		
		newsCategory.setCode("updateCode0");
		newsCategory.setLanguage("updateLanguage0");
		newsCategoryDao.updateNewsCategory(newsCategory);
		Log.i(TAG5, newsCategory.toString());
	}
	
	public void testGetNewsCategoryByPage(){
		NewsCategoryDao newsCategoryDao = new NewsCategoryDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(newsCategoryDao.getNewsCategoryCount()+""));
		
		List<NewsCategory> newsCategorys = newsCategoryDao.getNewsCategoryByPage(pager);
		
		for(NewsCategory e:newsCategorys){
			Log.i(TAG5, e.toString());
		}
	}
	
	
	public void testAddNewsPriorityDao(){
		NewsPriorityDao newsPriorityDao = new NewsPriorityDao(this.getContext());
		
		for(int i=0;i<=5;i++){
			NewsPriority newsPriority = new NewsPriority();
			newsPriority.setCode("code" + i);
			newsPriority.setName("name" + i);
			newsPriority.setLanguage("language" + i);
			newsPriorityDao.addNewsPriority(newsPriority);
		}
		List<NewsPriority> newsPrioritys = newsPriorityDao.getNewsPriorityList();
		
		for(NewsPriority e:newsPrioritys){
			Log.i(TAG6, e.toString());
		}
	}
	public void testDeleteNewsPriorityDao(){
		NewsPriorityDao newsPriorityDao = new NewsPriorityDao(this.getContext());
		
		newsPriorityDao.deleteNewsPriority("name1");
		
		List<NewsPriority> newsPrioritys = newsPriorityDao.getNewsPriorityList();
		
		for(NewsPriority e:newsPrioritys){
			Log.i(TAG6, e.toString());
		}
		
	}
	
	public void testUpdateNewsPriorityDao(){
		NewsPriorityDao newsPriorityDao = new NewsPriorityDao(this.getContext());
		
		NewsPriority newsPriority = newsPriorityDao.getNewsPriority("name0");
		
		newsPriority.setCode("updateCode0");
		newsPriority.setLanguage("updateLanguage0");
		newsPriorityDao.updateNewsPriority(newsPriority);
		Log.i(TAG6, newsPriority.toString());
	}
	
	public void testGetNewsPriorityByPage(){
		NewsPriorityDao newsPriorityDao = new NewsPriorityDao(this.getContext());
		
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(3);
		pager.setTotalNum(Integer.parseInt(newsPriorityDao.getNewsPriorityCount()+""));
		
		List<NewsPriority> newsPrioritys = newsPriorityDao.getNewsPriorityByPage(pager);
		
		for(NewsPriority e:newsPrioritys){
			Log.i(TAG6, e.toString());
		}
	}
}
