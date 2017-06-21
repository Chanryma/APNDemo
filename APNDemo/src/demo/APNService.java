package demo;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;

/**
 * iOS通知推送服务。
 * 
 * @author jonathanma
 *
 */
public class APNService {
    private String certificatePath;
    private String certificatePassword;
    private PushNotificationManager pushManager;

    private static APNService instance;

    static {
        instance = new APNService();
    }

    public static APNService getInstance() {
        return instance;
    }

    public void setup(String certificatePath, String certificatePassword) {
        this.certificatePath = certificatePath;
        this.certificatePassword = certificatePassword;
        pushManager = new PushNotificationManager();
    }

    /**
     * 发送通知给单个设备。
     *
     * @param isProdEnv
     *            true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
     * @param summary
     *            通知标题。App在后台运行时接到通知，通知栏显示这个内容。
     * @param contentJson
     *            消息内容, Json格式。
     * @param deviceToken
     *            设备标识
     * @param closeAfterSent
     *            true: 发完当前通知后，关闭连接。建议在循环调用此接口，发送给多个用户时，在最后一个用户发送完成后传false。
     * @return 通知是否成功推送到APNS。
     * @throws Exception
     */
    public boolean push2Device(boolean isProdEnv, String summary, String contentJson, String deviceToken,
            boolean closeAfterSent) throws Exception {
        boolean isSuccess = true;

        try {
            String rawJson = "{aps:{content-available:1}}";
            PushNotificationPayload payLoad = new PushNotificationPayload(rawJson);
            payLoad.addAlert(summary);
//            payLoad.addBadge(1);
            payLoad.addCustomDictionary("content", contentJson);
            payLoad.addSound("default"); // 使用默认铃音
            pushManager.initializeConnection(
                    new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, isProdEnv));

            Device device = new BasicDevice();
            device.setToken(deviceToken);
            isSuccess = pushManager.sendNotification(device, payLoad, closeAfterSent).isSuccessful();
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            if (pushManager != null) {
                pushManager.stopConnection();
            }
        }

        return isSuccess;
    }
}
