package tools;

import java.util.HashMap;


import com.cloopen.rest.sdk.CCPRestSDK;

public class IMTools {

	private static final String MAIN_ACCOUN = "8a48b5514c9d9c05014cb0b8fd4c0c39";

	private static final String MAIN_TOKEN = "1279c35dbb3343388374fc1289e461a4";

	private static final String APP_ID = "aaf98f894c9d994b014cb0bb82730c25";

	public static String createSubAccount(String account) {
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(MAIN_ACCOUN, MAIN_TOKEN);// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId(APP_ID);// 初始化应用ID
		result = restAPI.createSubAccount(account);

		System.out.println("SDKTestCreateSubAccount result=" + result);

		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			@SuppressWarnings("unchecked")
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			@SuppressWarnings("unchecked")
			HashMap<String, Object> subAccount = (HashMap<String, Object>) data
					.get("SubAccount");

			return subAccount.get("voipAccount").toString();
		} else {
			// 异常返回输出错误码和错误信息
			// System.out.println("错误码=" + result.get("statusCode") + " 错误信息= "
			// + result.get("statusMsg"));
			return null;
		}
	}

}
