package com.cuc.miti.phone.xmc.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class Util {

	public static Location getLocation(Context context) {
		LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

}
