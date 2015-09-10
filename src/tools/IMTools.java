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
		restAPI.init("sandboxapp.cloopen.com", "8883");// ��ʼ����������ַ�Ͷ˿ڣ���ʽ���£���������ַ����Ҫдhttps://
		restAPI.setAccount(MAIN_ACCOUN, MAIN_TOKEN);// ��ʼ�����ʺ����ƺ����ʺ�����
		restAPI.setAppId(APP_ID);// ��ʼ��Ӧ��ID
		result = restAPI.createSubAccount(account);

		System.out.println("SDKTestCreateSubAccount result=" + result);

		if ("000000".equals(result.get("statusCode"))) {
			// �����������data������Ϣ��map��
			@SuppressWarnings("unchecked")
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			@SuppressWarnings("unchecked")
			HashMap<String, Object> subAccount = (HashMap<String, Object>) data
					.get("SubAccount");

			return subAccount.get("voipAccount").toString();
		} else {
			// �쳣�������������ʹ�����Ϣ
			// System.out.println("������=" + result.get("statusCode") + " ������Ϣ= "
			// + result.get("statusMsg"));
			return null;
		}
	}

}
