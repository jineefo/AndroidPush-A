package com.jf.swift.push.inter.impl;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.igexin.sdk.PushManager;
import com.jf.swift.push.bean.AppModel;
import com.jf.swift.push.bean.GetuiAppModel;
import com.jf.swift.push.bean.HuaWeiAppModel;
import com.jf.swift.push.bean.MZAppModel;
import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.bean.MiAppModel;
import com.jf.swift.push.bean.OppoAppModel;
import com.jf.swift.push.bean.VivoAppModel;
import com.jf.swift.push.exception.PushPlatformRegisterException;
import com.jf.swift.push.inter.PushInterface;
import com.jf.swift.push.listener.IPushListener;
import com.jf.swift.push.listener.PushType;
import com.jf.swift.push.util.Constants;
import com.jf.swift.push.util.Rom;
import com.jf.swift.push.util.Utils;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.jf.swift.push.util.Constants.EXCEPTION;
import static com.jf.swift.push.util.Constants.EXCEPTION1;
import static com.jf.swift.push.util.Constants.EXCEPTION3;
import static com.jf.swift.push.util.Constants.EXCEPTION4;
import static com.jf.swift.push.util.Constants.GETTUI;
import static com.jf.swift.push.util.Constants.HUAWEI;
import static com.jf.swift.push.util.Constants.MEIZU;
import static com.jf.swift.push.util.Constants.OPPO;
import static com.jf.swift.push.util.Constants.VIVO;
import static com.jf.swift.push.util.Constants.XIAOMI;


public class PushInterfaceImpl extends PushInterface {
    private static IPushListener iPushListener;
    private final static String default_prefer = "pushcontent";
    private HuaweiApiClient client;
    private AppModel[] appModel = null;
    private GetuiAppModel gettuiModel = null;
    private int currentRepat = 0;

    private static PushInterfaceImpl pushInterface = new PushInterfaceImpl();
    private static Integer currentPlat = null;
    private static boolean unregisterForVivo = false;
    private static Context mContext;
    private String currentToken;

    public static synchronized boolean isUnregisterForVivo() {
        return unregisterForVivo;
    }

    public static synchronized void setUnregisterForVivo(boolean unregisterForVivo) {
        PushInterfaceImpl.unregisterForVivo = unregisterForVivo;
    }

    public static synchronized Integer getCurrentPlat() {
        return currentPlat;
    }

    public static synchronized void setCurrentPlat(Integer currentPlat) {
        PushInterfaceImpl.currentPlat = currentPlat;
    }

    private PushInterfaceImpl() {
    }

    public synchronized static PushInterfaceImpl getInstance() {
        if (pushInterface == null) {
            pushInterface = new PushInterfaceImpl();
        }
        return pushInterface;
    }

    private AppModel getAppModel(Context context, GetuiAppModel getuiAppModel) {
        PlatStatus status = Utils.getTuiPlat(context);
        if (getuiAppModel != null && getuiAppModel.isCurrentPlatform(status)) {
            return getuiAppModel;
        }
        if (appModel != null) {
            AppModel defaultGetTuiModel = getuiAppModel;
            for (AppModel model : appModel) {
                if (model.isCurrentPlatform(status)) {
                    return model;
                } else {
                    if (model.isCurrentPlatform(PlatStatus.GETTUI)) {
                        defaultGetTuiModel = model;
                    }
                }
            }
            return defaultGetTuiModel;
        } else {
            return getuiAppModel;
        }
    }

    private boolean invaildPlatform(GetuiAppModel getuiAppModel) {
        if (getuiAppModel != null) return false;
        else return true;
    }

    @Override
    public void setPushListener(IPushListener iPushListener) {
        this.iPushListener = iPushListener;
    }

    @Override
    public void loginPushPlatform(final Context context, final GetuiAppModel getuiModel, AppModel... appModel) throws PushPlatformRegisterException {
        if (context == null) {
            throw new PushPlatformRegisterException(EXCEPTION3);
        }
        if (!init(context)) {
            throw new PushPlatformRegisterException(EXCEPTION4);
        }
        this.appModel = appModel;
        this.gettuiModel = getuiModel;
        boolean invaild = invaildPlatform(getuiModel);
        if (invaild) {
            throw new PushPlatformRegisterException(EXCEPTION);
        }
        mContext = context.getApplicationContext();
        //oppo推送的sdk需要初始化之后才可以调用对应的方法
        HeytapPushManager.init(context, false);
        final AppModel tempModel = getAppModel(context, getuiModel);
        int type = 2;
        if (tempModel instanceof HuaWeiAppModel) {
            if (Rom.getHuaWeiApiLevel() >= 10) {
                    type = 3;
            }
        } else if (tempModel instanceof MiAppModel) {
                type = 4;
        } else if (tempModel instanceof OppoAppModel) {
                type = 5;
        } else if (tempModel instanceof MZAppModel) {
                type = 6;
        } else if (tempModel instanceof VivoAppModel) {
                type = 7;
        }
        handlerRequest(tempModel, context, type);
    }

    public AppModel getAppModel(String type) {
        for (AppModel appModel : this.appModel) {
            if (appModel instanceof OppoAppModel && OPPO.equals(type)) {
                return appModel;
            } else if (appModel instanceof VivoAppModel && VIVO.equals(type)) {
                return appModel;
            } else if (appModel instanceof HuaWeiAppModel && HUAWEI.equals(type)) {
                return appModel;
            } else if (appModel instanceof MZAppModel && MEIZU.equals(type)) {
                return appModel;
            } else if (appModel instanceof MiAppModel && XIAOMI.equals(type)) {
                return appModel;
            }
        }
        return null;
    }

    private void handlerRequest(final AppModel tempModel, final Context context, final boolean unregister) throws PushPlatformRegisterException {
        if (tempModel == null) {
            throw new PushPlatformRegisterException(EXCEPTION);
        }
        if (tempModel.isCurrentPlatform(PlatStatus.HUAWEI)) {
            HuaWeiAppModel huaWeiAppModel = (HuaWeiAppModel) tempModel;
            Utils.putMetaDataValue(context, Utils.HUAWEIPUSHID, huaWeiAppModel.getAppId());
            client = new HuaweiApiClient.Builder(context).addApi(HuaweiPush.PUSH_API).addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    if (currentRepat < Constants.REPEAT_COUNT) {
                        try {
                            currentRepat++;
                            handlerRequest(tempModel, context, 3);
                        } catch (PushPlatformRegisterException e) {

                        }
                    } else {
                        currentRepat = 0;
                        handOnline(false);
                    }
                }
            }).build();
            client.setConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected() {
                    currentRepat = 0;
                    handOnline(true);
                    getTokenAsyn();
                }

                @Override
                public void onConnectionSuspended(int i) {
                    if (currentRepat < Constants.REPEAT_COUNT) {
                        return;
                    } else {
                        currentRepat = 0;
                        handOnline(false);
                    }
                }
            });
            //建议在oncreate的时候连接华为移动服务
            //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
            client.connect();
        } else if (tempModel.isCurrentPlatform(PlatStatus.MI)) {
            MiAppModel miAppModel = (MiAppModel) tempModel;
            if ((miAppModel.getAppId()!=null && miAppModel.getAppKey()!=null)) {
                if (shouldInit(context)) {
                    MiPushClient.registerPush(context.getApplicationContext(), miAppModel.getAppId(), miAppModel.getAppKey());
                }
            } else {
                throw new PushPlatformRegisterException(EXCEPTION1);
            }
        } else if (tempModel.isCurrentPlatform(PlatStatus.OPPO)) {
            final OppoAppModel oppoAppModel = (OppoAppModel) tempModel;
            try {
                HeytapPushManager.register(context, oppoAppModel.getAppKey(), oppoAppModel.getAppSercet(), new ICallBackResultService() {
                    @Override
                    public void onRegister(int code, String s) {
                        PushInterfaceImpl.setCurrentPlat(null);
                        if (code == 0) {
                            handPushToken(s, PlatStatus.OPPO);
                        } else {
                            try {
                                handlerRequest(gettuiModel, context, 2);
                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void onUnRegister(int i) {

                    }

                    @Override
                    public void onSetPushTime(int i, String s) {

                    }

                    @Override
                    public void onGetPushStatus(int code, int status) {
                        if (code == 0 && status == 0) {
                            handOnline(true);
                        }
                    }

                    @Override
                    public void onGetNotificationStatus(int code, int status) {
                        if (code == 0 && status == 0) {
                            handOnline(true);
                        }
                    }
                });
            } catch (Exception e) {
                throw new PushPlatformRegisterException(EXCEPTION);
            }
        } else if (tempModel.isCurrentPlatform(PlatStatus.FLYME)) {
            MZAppModel mzAppModel = (MZAppModel) tempModel;
            try {
                com.meizu.cloud.pushsdk.PushManager.register(context, mzAppModel.getAppId(), mzAppModel.getAppKey());
            } catch (Exception e) {
                throw new PushPlatformRegisterException(EXCEPTION);
            }
        } else if (tempModel.isCurrentPlatform(PlatStatus.VIVO)) {
            VivoAppModel vivoAppModel = (VivoAppModel) tempModel;
            Utils.putMetaDataValue(context, Utils.VIVOPUSHAPPID, vivoAppModel.getAppId());
            Utils.putMetaDataValue(context, Utils.VIVOPUSHAPPKEY, vivoAppModel.getAppKey());
            String resid = PushClient.getInstance(context).getRegId();
            setUnregisterForVivo(unregister);
            if (resid!=null) {
                if (!unregister) {
                    registerForVivo(context, resid, PlatStatus.VIVO);
                } else {
                    unreigsterForVivo(context);
                }
            } else {

                PushClient.getInstance(context).initialize();
                PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
                    @Override
                    public void onStateChanged(int i) {

                    }
                });
            }
        } else {
            if (tempModel.isCurrentPlatform(PlatStatus.GETTUI)) {
                GetuiAppModel getuiAppModel = (GetuiAppModel) tempModel;
                Utils.putMetaDataValue(context, Utils.GETUIPUSHID, getuiAppModel.getAppId());
                Utils.putMetaDataValue(context, Utils.GETUIPUSHKEY, getuiAppModel.getAppKey());
                Utils.putMetaDataValue(context, Utils.GETUIPUSHSERCET, getuiAppModel.getAppSercet());
                PushManager.getInstance().initialize(context.getApplicationContext(), GeTuiService.class);
                PushManager.getInstance().registerPushIntentService(context.getApplicationContext(), GetuiIntentService.class);
            } else {
                throw new PushPlatformRegisterException(EXCEPTION);
            }
        }

    }

    private void registerForVivo(Context context, String resid, PlatStatus platStatus) {
        handPushToken(resid, platStatus);
        PushInterfaceImpl.setCurrentPlat(null);
        PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int i) {
            }
        });
    }

    public static void unreigsterForVivo(Context context) {
        PushInterfaceImpl.setUnregisterForVivo(false);
    }

    private void handlerRequest(final AppModel tempModel, final Context context, Integer type) throws PushPlatformRegisterException {
        setCurrentPlat(type);
        handlerRequest(tempModel, context, false);
    }

    private void getTokenAsyn() {
        if (client == null) return;
        if (!client.isConnected()) {
            client.connect();
            return;
        }
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                if (result != null && result.getTokenRes() != null && Utils.isString(result.getTokenRes().getToken())) {
                    String token = result.getTokenRes().getToken();
                    try {
                        token = URLEncoder.encode(token, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    handPushToken(token, PlatStatus.HUAWEI);
                }
            }
        });
    }

    private boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public String getBaseUrl() {
        return Constants.getBaseUrl();
    }

    @Override
    public void logoutPushPlatForm(Context context, AppModel appModel) {
        if (appModel == null) return;
        if (appModel.isCurrentPlatform(PlatStatus.HUAWEI)) {
            if (client != null) {
                try {
                    client.disconnect();
                } catch (Exception e) {

                }
            }
        } else if (appModel.isCurrentPlatform(PlatStatus.MI)) {
            try {
                MiPushClient.unregisterPush(context.getApplicationContext());
            } catch (Exception e) {

            }
        } else if (appModel.isCurrentPlatform(PlatStatus.OPPO)) {
            try {
                HeytapPushManager.unRegister();
            } catch (Exception e) {

            }
        } else if (appModel.isCurrentPlatform(PlatStatus.FLYME)) {
            try {
                MZAppModel mzAppModel=null;
                if(appModel instanceof MZAppModel){
                    mzAppModel=(MZAppModel)appModel;
                }
                if(mzAppModel!=null&&mzAppModel.getAppId()!=null&&mzAppModel.getAppKey()!=null){
                    com.meizu.cloud.pushsdk.PushManager.unRegister(context,mzAppModel.getAppId(),mzAppModel.getAppKey());
                }else {
                    com.meizu.cloud.pushsdk.PushManager.unRegister(context);
                }
            } catch (Exception e) {

            }
        } else if (appModel.isCurrentPlatform(PlatStatus.VIVO)) {
            try {
                PushClient.getInstance(context).turnOffPush(new IPushActionListener() {
                    @Override
                    public void onStateChanged(int i) {

                    }
                });
            } catch (Exception e) {

            }
        } else {
            try {
                PushManager.getInstance().stopService(context.getApplicationContext());
            } catch (Exception e) {

            }
        }
    }


    public void handMessage(MessageBean data, PushType pushType) {
        if (iPushListener != null) {
            iPushListener.onPushMessageReceive(data, pushType);
        }
    }

    public void handPushToken(String token, PlatStatus platStatus) {

        currentToken = token;
        if (iPushListener != null) {
            iPushListener.onPushTokenReceive(token, platStatus);
        }
    }

    public void handOnline(boolean online) {
        if (iPushListener != null) {
            iPushListener.isOnline(online);
        }
    }

    private void handPushToggle(boolean toggle) {
        if (iPushListener != null) {
            iPushListener.onPushToggle(toggle);
        }
    }

    @Override
    public String getPushToken() {
        return currentToken;
    }

}