package tools.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

public class Info {
    private Context context;

    public Info(Context context) {
        this.context = context;
    }

    /**
     * 获取设备唯一识别号
     * @return
     */
    public String getDeviceId() {
        if (context != null) {
            MobileInfoUtil MInfoU = new MobileInfoUtil();
            String imei = MInfoU.getDeviceId(this.context);
            return imei;
        }else{
            return "";
        }
    }

}
