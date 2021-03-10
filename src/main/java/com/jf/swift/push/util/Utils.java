package com.jf.swift.push.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.heytap.msp.push.HeytapPushManager;
import com.jf.swift.push.inter.impl.PlatStatus;
import com.vivo.push.PushClient;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by jf on 2017/8/31.
 */

public class Utils {
    final static String PUSH_TOKEN = "pushtoken";
    public final static String HUAWEIPUSHID = "com.huawei.hms.client.appid";
    public final static String GETUIPUSHID = "PUSH_APPID";
    public final static String GETUIPUSHKEY = "PUSH_APPKEY";
    public final static String GETUIPUSHSERCET = "PUSH_APPSECRET";
    public final static String VIVOPUSHAPPID = "com.vivo.push.app_id";
    public final static String VIVOPUSHAPPKEY = "com.vivo.push.api_key";

    public static void setPushToken(String token) {
        new SpUtils().putString(PUSH_TOKEN, token);
    }

    public static String getPushToken() {
        return new SpUtils().getString(PUSH_TOKEN, "");
    }

    public static boolean isString(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";


    //EMUI标识
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    //Flyme标识
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    private static final String KEY_FLYME_ID_FALG_VALUE_KEYWORD = "Flyme";
    private static final String KEY_FLYME_ICON_FALG = "persist.sys.use.flyme.icon";
    private static final String KEY_FLYME_SETUP_FALG = "ro.meizu.setupwizard.flyme";
    private static final String KEY_FLYME_PUBLISH_FALG = "ro.flyme.published";

    public static ROM_TYPE getRomType(Context context) {
        ROM_TYPE rom_type = ROM_TYPE.OTHER;
        try {
            BuildProperties buildProperties = BuildProperties.newInstance();
            if (buildProperties.containsKey(KEY_EMUI_VERSION_CODE) || buildProperties.containsKey(KEY_EMUI_API_LEVEL) || buildProperties.containsKey(KEY_EMUI_CONFIG_HW_SYS_VERSION)) {
                if (EnableHms(context)) {
                    return ROM_TYPE.EMUI;
                } else {
                    return rom_type;
                }
            }
            if (buildProperties.containsKey(KEY_MIUI_VERSION_CODE) || buildProperties.containsKey(KEY_MIUI_VERSION_NAME) || buildProperties.containsKey(KEY_MIUI_INTERNAL_STORAGE)) {
                return ROM_TYPE.MIUI;
            }
            if (buildProperties.containsKey(KEY_FLYME_ICON_FALG) || buildProperties.containsKey(KEY_FLYME_SETUP_FALG) || buildProperties.containsKey(KEY_FLYME_PUBLISH_FALG)) {
                return ROM_TYPE.FLYME;
            }
            if (buildProperties.containsKey(KEY_FLYME_ID_FALG_KEY)) {
                String romName = buildProperties.getProperty(KEY_FLYME_ID_FALG_KEY);
                if (!TextUtils.isEmpty(romName) && romName.contains(KEY_FLYME_ID_FALG_VALUE_KEYWORD)) {
                    return ROM_TYPE.FLYME;
                }
            }
        } catch (Exception e) {
            if (Rom.isEmui()) {
                return ROM_TYPE.EMUI;
            } else if (Rom.isMiui()) {
                return ROM_TYPE.MIUI;
            } else if (Rom.isFlyme()) {
                return ROM_TYPE.FLYME;
            }
        }

        return rom_type;
    }

    private static boolean EnableHms(Context context) {
        if (context == null) return false;
        PackageInfo pi = null;
        PackageManager pm = context.getPackageManager();
        int hwid = 0;
        try {
            pi = pm.getPackageInfo("com.huawei.hwid", 0);
            if (pi != null) {
                hwid = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (hwid >= 20401300) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean EiuiEnableHms() {
        int emuiApiLevel = 0;
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (emuiApiLevel >= 9) {
            return true;
        } else {
            return false;
        }
    }

    public enum ROM_TYPE {
        MIUI,
        FLYME,
        EMUI,
        OPPO,
        VIVO,
        OTHER
    }

    public static PlatStatus getTuiPlat(Context context) {
        Utils.ROM_TYPE rom_type = Utils.getRomType(context);
        if (rom_type == Utils.ROM_TYPE.EMUI) {
            return PlatStatus.HUAWEI;
        } else if (rom_type == ROM_TYPE.MIUI) {
            return PlatStatus.MI;
        } else {
            if (HeytapPushManager.isSupportPush()) {
                return PlatStatus.OPPO;
            } else if (PushClient.getInstance(context).isSupport()) {
                return PlatStatus.VIVO;
            } else if (rom_type == ROM_TYPE.FLYME) {
                return PlatStatus.FLYME;
            } else {
                return PlatStatus.GETTUI;
            }
        }
    }

    public static void putMetaDataValue(Context context, String key, String value) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                applicationInfo.metaData.putString(key, value);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

    }

    public static boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    public static String getOSVersion(Context context) {
        PlatStatus status = getTuiPlat(context);
        if (status == PlatStatus.HUAWEI) {
            return "emui " + Rom.getProp(Rom.KEY_VERSION_EMUI);
        } else if (status == PlatStatus.MI) {
            return "miui " + Rom.getProp(Rom.KEY_VERSION_MIUI);
        } else if (status == PlatStatus.FLYME) {
            return Build.DISPLAY;
        } else if (status == PlatStatus.OPPO) {
            return "ColorOS " + Rom.getProp(Rom.ROM_OPPO);
        } else if (status == PlatStatus.VIVO) {
            return "Funtouch OS " + Rom.getProp(Rom.ROM_VIVO);
        } else {
            return Build.MANUFACTURER + " " + Build.VERSION.CODENAME;
        }
    }
}
