package demo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import javapns.Push;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

public class APNDemo {
    private static String DEVICE_TOKEN = "a6695db750761b2d5fb371775f513bffcc6fa3fa18eedd61390bad57e8d6f3c3";
    private static String CERT_PATH = "/Users/jonathanma/Desktop/iphone_dist.p12";
    private static String CERT_PASSWORD = "123456";

    static {
        LogUtil.debug("Logger init");
    }

    public static void main(String[] args) throws Exception {
        APNService.getInstance().setup(CERT_PATH, CERT_PASSWORD);
        JSONObject json = getTestJson();
        APNService.getInstance().push2Device(true, "新通知", json.toString(), DEVICE_TOKEN, true);
    }

    /**
     * @param isProdEnv
     *            true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
     * @param summary
     *            通知标题。App在后台运行时接到通知，通知栏显示这个内容。
     * @param contentJson
     *            消息内容, Json格式。
     * @throws Exception
     */
    public static void test1(boolean isProdEnv, String summary, String contentJson, List<String> deviceTokens)
            throws Exception {
        if ((deviceTokens == null) || (deviceTokens.size() == 0)) {
            return;
        }

        int badge = 1;// 图标小红圈的数值
        String certificatePath = CERT_PATH;
        String certificatePassword = CERT_PASSWORD;// 此处注意导出的证书密码不能为空因为空密码会报错

        PushNotificationManager pushManager = new PushNotificationManager();
        try {
            PushNotificationPayload payLoad = new PushNotificationPayload();
            payLoad.addAlert(summary);
            payLoad.addCustomDictionary("content", contentJson);
            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
            payLoad.addSound("default");// 使用默认铃音
            pushManager.initializeConnection(
                    new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, isProdEnv));

            List<PushedNotification> notifications = new ArrayList<PushedNotification>();
            if (deviceTokens.size() == 1) {
                Device device = new BasicDevice();
                device.setToken(deviceTokens.get(0));
                PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
                notifications.add(notification);
            } else {
                List<Device> device = new ArrayList<Device>();
                for (String token : deviceTokens) {
                    device.add(new BasicDevice(token));
                }
                notifications = pushManager.sendNotifications(payLoad, device);
            }
            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
            List<PushedNotification> successfulNotifications = PushedNotification
                    .findSuccessfulNotifications(notifications);
            int failed = failedNotifications.size();
            int success = successfulNotifications.size();
            LogUtil.debug("failedCount=" + failed + ", successCount=" + success);
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            if (pushManager != null) {
                pushManager.stopConnection();
            }
        }
    }

    public static void test2(boolean isPro) {
        try {
            Push.alert("Hello World!", CERT_PATH, CERT_PASSWORD, isPro, DEVICE_TOKEN);
        } catch (KeystoreException e) {
            e.printStackTrace();
        } catch (javapns.communication.exceptions.CommunicationException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject getTestJson() {
        JSONObject json = null;

        try {
            json = new JSONObject();
            json.put("proid", "253");
//            json.put("projectname", "demo_project");
            json.put("senttime", "2016-03-18 14:57:01");
            json.put("geterid", 34693);
            json.put("geter", "Jonathan");
            json.put("type", "gx_video");
            json.put("funid", "4");
//            json.put("sysMsg", "xxxxx");
            json.put("openMsg", "");
        } catch (JSONException e) {

        }
        
        return json;
    }
}