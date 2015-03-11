package com.cuc.miti.phone.xmc.auth;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.cuc.miti.phone.xmc.ServiceManager;
import com.cuc.miti.phone.xmc.config.ParamBuilder;
import com.cuc.miti.phone.xmc.models.User;
import com.cuc.miti.phone.xmc.net.NetworkService;
import com.cuc.miti.phone.xmc.store.beans.UserTable;
import com.cuc.miti.phone.xmc.utils.StringUtil;

public class Session {
	private static final boolean DEBUG_FLAG = true;

	private static LoginObserver mLoginObserver;

	static {
		mLoginObserver = new LoginSuccessObserver();
	}

	public static LoginObserver getLoginObserver() {
		return mLoginObserver;
	}

	/**
	 * 登录接口
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param domain
	 * @param callback
	 */
	public static void doLogin(String url, final String username,
			final String password, String type, final ILoginCallback callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ParamBuilder.USER_NAME, username);
		params.put(ParamBuilder.PASSWORD, password);
		params.put(ParamBuilder.TYPE, type);
		ServiceManager.getNetworkService().post(url, params,
				new NetworkService.ICallback() {
					@Override
					public void onResponse(int status, String result) {
						if (DEBUG_FLAG)
							System.out.println("login ----> status = " + status
									+ ",result = " + result);
						if (status == 200) {
							saveUserInfo(username, password, result);
							if (callback != null) {
								callback.onSuccess();
							}
							mLoginObserver.notifyExecutors();
						} else if (status == 401) {
							if (callback != null) {
								callback.onFail(parserResult(result));
							}
						} else {
							if (callback != null) {
								callback.connectTimeout();
							}
						}
					}
				});
	}

	// 自动登录
	public static void autoLogin(String url, String type) {
		if (null != getLoginUser() && getLoginUser().isAutoLogin) {
			doLogin(url, getLoginUser().name, getLoginUser().password, type, null);
		}
	}

	public static User getLoginUser() {
		return UserTable.getInstance().getUser();
	}

	// 退出登录
	public static void doLogout(String url, String domain,
			final ILogoutCallback callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("_method", "DELETE");
		params.put("domain", domain);
		ServiceManager.getNetworkService().post(url, params,
				new NetworkService.ICallback() {
					@Override
					public void onResponse(int status, String result) {
						if (DEBUG_FLAG)
							System.out.println("login out ----> status = "
									+ status + ",result : " + result);
						if (200 == status) {
							String msg = parserResult(result);
							UserTable.getInstance().update(true);
							callback.logoutSuccess(msg);
						} else {
							callback.logoutFail();
						}
					}
				});
	}

	/**
	 * 注册
	 * 
	 * @param url
	 *            请求url
	 * @param phone
	 *            用户名或手机号
	 * @param password
	 *            密码
	 * @param surePwd
	 *            确认密码
	 * @param verifyCode
	 *            验证码
	 * @param domain
	 *            tuan800或hui800等
	 * @param callback
	 *            回调函数
	 */
	public static void doRegister(String url, final String name,
			final String password, String email, String nick, final IRegisterCallback callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ParamBuilder.USER_NAME, name);
		params.put(ParamBuilder.PASSWORD, password);
		params.put(ParamBuilder.EMAIL, email);
		// params.put(ParamBuilder.CAPTCHA, verifyCode);
		params.put("agreement", "true");
		params.put("auto_login", "true");
		params.put("nike", nick);
		ServiceManager.getNetworkService().post(url, params,
				new NetworkService.ICallback() {
					@Override
					public void onResponse(int status, String result) {
						if (DEBUG_FLAG)
							System.out.println("register ----> " + "status = "
									+ status + ",result = " + result);

						if (status == 200) {// 注册成功
							try {
								JSONObject obj = new JSONObject(result);
								String userid = obj.optString("userid");
								if(userid != null && userid.length() > 0){
									saveUserInfo(name, password, result);
									callback.onSuccess();
								}else{
									callback.onFail(obj.optString("message"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						} else if (status == 400) {// 注册失败
							try {
								JSONObject obj = new JSONObject(result);
								callback.onFail(obj.optString("message"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {// 链接超时
							callback.connectTimeout();
						}
					}
				});
	}

	// 保存用户信息到数据库
	private static void saveUserInfo(String name, String password, String json) {
		User user = new User();
		user.name = name;
		user.password = password;
		if (!StringUtil.isEmpty(json)) {
			try {
				JSONObject obj = new JSONObject(json);
				user.id = obj.getString("userId");
				user.phoneNumber = obj.optString("username");
				user.accessToken = obj.optString("key");
				user.isLogin = true;
			} catch (JSONException e) {
				user.isLogin = false;
			}
		} else {
			user.isLogin = false;
		}
		UserTable.getInstance().saveUser(user);
	}

	// 获取验证码
	public static void getVerifyCode(String url, String phoneNum,
			String isRegistered, final IGetVerifyCode callBack) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone_number", phoneNum);
		params.put("registered", isRegistered);
		ServiceManager.getNetworkService().post(url, params,
				new NetworkService.ICallback() {
					@Override
					public void onResponse(int status, String result) {
						if (DEBUG_FLAG)
							System.out
									.println("get verify code  ----> status = "
											+ status + ",result : " + result);
						if (status == 201) {
							callBack.onSuccess();
						} else if (status == 400) {
							String msg = parserResult(result);
							callBack.onFail(msg);
						} else {
							callBack.connectTimeout();
						}
					}
				});
	}

	// 修改密码
	public static void modifyPassword(String url, String phone,
			String verifyCode, String pwd, String confirm_pwd,
			final IModifyPassword callBack) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone_number", phone);
		params.put("phone_confirmation", verifyCode);
		params.put("password", pwd);
		params.put("password_confirmation", confirm_pwd);
		ServiceManager.getNetworkService().post(url, params,
				new NetworkService.ICallback() {
					@Override
					public void onResponse(int status, String result) {
						if (DEBUG_FLAG)
							System.out.println("status = " + status
									+ ",result : " + result);
						String msg = parserResult(result);
						if (status == 201) {
							callBack.modifySuccess(msg);
						} else if (status == 400) {
							callBack.modifyFail(msg);
						} else {
							callBack.connectTimeout();
						}
					}
				});
	}

	// 解析服务器返回的数据
	private static String parserResult(String json) {
		String msg = "请求失败";
		if (StringUtil.isEmpty(json))
			return msg;
		try {
			JSONObject obj = new JSONObject(json);
			msg = obj.optString("detail");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * if true returned,it's logging(登录),otherwise it's not logging.
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		if (getLoginUser() != null && getLoginUser().isLogin) {
			return getLoginUser().isLogin;
		}
		
		return false;
	}

	// the callback of register
	public interface IRegisterCallback extends IBaseCallBack {

	}

	// the callback of login
	public interface ILoginCallback extends IBaseCallBack {

	}

	// the callback of logout
	public interface ILogoutCallback {
		public void logoutSuccess(String msg);

		public void logoutFail();
	}

	public interface IModifyPassword {
		public void modifySuccess(String msg);

		public void modifyFail(String msg);

		public void connectTimeout();
	}

	// the callback of getting verify code
	public interface IGetVerifyCode extends IBaseCallBack {

	}

	private interface IBaseCallBack {
		public void onSuccess();

		public void onFail(String msg);

		/**
		 * 网络连接不好，链接超时，请重试
		 */
		public void connectTimeout();
	}
}
