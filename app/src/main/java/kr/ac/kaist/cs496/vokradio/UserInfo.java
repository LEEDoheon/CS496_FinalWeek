package kr.ac.kaist.cs496.vokradio;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by q on 2017-07-30.
 */

public class UserInfo {
    private static boolean login_status = false;
    private static String useremail;
    private static String username;
    private static String userjob;
    private static String usernumber;

    public static void Login(String email) throws JSONException {
        login_status = true;
        HttpCall.setMethodtext("GET");
        HttpCall.setUrltext("/api/admin/"+email);
        JSONObject user = new JSONObject(HttpCall.getResponse());
        useremail = user.getString("email");
        username = user.getString("name");
        userjob = user.getString("job");
        usernumber = user.getString("yearnumber");
    }

    public static void Logout() {
        login_status = false;
    }

    public static boolean isLogined() {
        return login_status;
    }

    public static String getName() {
        return username;
    }

    public static String getJob() {
        return userjob;
    }

    public static String getNumber() {
        return usernumber;
    }
}
