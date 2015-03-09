package com.cuc.miti.phone.xmc.xmpp;

/** 
 * Utility class for LogCat.
 *
 * @author SongQing
 */
public class LogUtil {
    
    public static String makeLogTag(Class<?> cls) {
        return "Androidpn_" + cls.getSimpleName();
    }

}
