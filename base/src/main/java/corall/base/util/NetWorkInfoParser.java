package corall.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.util.Enumeration;

import corall.base.app.CorApplication;

/**
 * 跟据规则确定当前的网络参数和规则<br>
 * 外部网络使用均通过这个类，由于部分机子在网络变化时无法正确进行网络通知，<br>
 * 所以这里要在相关对外接口中重新检查网络状态。
 * Created by linlin_91 on 2015/6/24.
 */
public class NetWorkInfoParser {

    public static final String TAG = NetWorkInfoParser.class.getSimpleName();

    // wap 代理ip和端口
    public static final String WAP_PROXY_IP = "10.0.0.172";
    public static final String CT_WAP_PROXY_IP = "10.0.0.200";

    public static final int WAP_PORT = 80;

    // 是否已经和网络建立连接
    private static boolean netConnect;
    // 使用的网络类型
    private static int netType = -1;
    // 使用的网络类型
    private static String netTypeName;
    // 使用的网络子类型名称（这个作为wap下载的参考）
    private static String netSubTypeName;
    // 网络额外信息，一般用于判断wap的特殊网络
    private static String netExtraType;

    /**
     * 更具网络参数设置环境配置<br>
     * 不确定锁定的实际效果所以这里先注释wifi锁定代码
     *
     * @param networkInfo 网络信息
     * @param imContext   上下文
     */
    public static void parserNetwork(NetworkInfo networkInfo, Context imContext) {
        // 如果外部传入的网络信息是空的，这里使用系统当前信息
        if (networkInfo == null) {
            ConnectivityManager conManager = (ConnectivityManager) imContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conManager != null) {
                networkInfo = conManager.getActiveNetworkInfo();
            }
        }

        // 更新网络状态
        if (networkInfo == null || networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
            netConnect = false;
            netType = -1;
            netSubTypeName = null;
            netExtraType = null;
            netTypeName = null;

        } else if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            netConnect = true;
            netType = getNetworkClass(imContext, networkInfo.getType());
            netSubTypeName = networkInfo.getSubtypeName();
            netTypeName = networkInfo.getTypeName();

            // 部分机型这个值为null
            netExtraType = networkInfo.getExtraInfo();
            if (netExtraType == null) {
                netExtraType = networkInfo.getTypeName();
            }

        }
    }


    /**
     * 更新Proxy连接参数
     */
    public static Proxy getProxy(CorApplication imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        Proxy proxy = null;
        // 是否初始化正常
        if (imContext.isBaseDataOk()) {
            if (netConnect) {
                if (netExtraType != null) {
                    if (netExtraType.toLowerCase().contains("ctwap")) {

                        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(CT_WAP_PROXY_IP, WAP_PORT));


                    } else if (netExtraType.contains("wap")) {
                        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(WAP_PROXY_IP, WAP_PORT));


                    } else {
                    }
                }
            }
        }
        return proxy;
    }

//    /**
//     * 更新HttpUrlConnection连接参数
//     */
//    public static MobHttpClient getHttpConnector(AMApplication imContext) {
//        // 重新检查网络状态
//        parserNetwork(null, imContext);
//
//        MobHttpClient httpClient = new MobHttpClient();
//        // 是否初始化正常
//        if (imContext.isBaseDataOk()) {
//            if (netConnect) {
//                if (netExtraType != null) {
//                    if (netExtraType.toLowerCase().contains("ctwap")) {
//                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(CT_WAP_PROXY_IP, WAP_PORT));
//                        httpClient.setProxy(proxy);
//
//                        if (MLog.debug) {
//                            MLog.d(TAG, "getHttpConnector -- set ctwap proxy for connector");
//                        }
//
//                    } else if (netExtraType.contains("wap")) {
//                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(WAP_PROXY_IP, WAP_PORT));
//                        httpClient.setProxy(proxy);
//
//                        if (MLog.debug) {
//                            MLog.d(TAG, "getHttpConnector -- set wap proxy for connector");
//                        }
//
//                    } else {
//                        if (MLog.debug) {
//                            MLog.d(TAG, "getHttpConnector -- common connector");
//                        }
//                    }
//                }
//            }
//        }
//        return httpClient;
//    }

    /**
     * 获取当前ip地址
     *
     * @return
     */
    public static String getCurrentIpAddress() {
        try {
            Enumeration<NetworkInterface> enumNetInterfaces = NetworkInterface.getNetworkInterfaces();
            if (enumNetInterfaces != null) {
                while (enumNetInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = enumNetInterfaces
                            .nextElement();
                    Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses();
                    if (enumIpAddr != null) {
                        while (enumIpAddr.hasMoreElements()) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress()) {
                                return inetAddress.getHostAddress().toString();
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 当前是否是wap连接
     */
    public static boolean isWapConnector(CorApplication imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        // 是否初始化正常
        if (imContext.isBaseDataOk()) {
            if (netConnect && netExtraType != null) {
                if (netExtraType.toLowerCase().contains("wap")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是wifi连接
     */
    public static boolean isWifiConnector(CorApplication imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        // 是否初始化正常
        if (imContext.isBaseDataOk()) {
            if (netConnect && netType == NetworkUtil.NETWORK_CLASS_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是不是wifi连接(Raw方法)
     *
     * @return 当前连接是否是WIFI
     */
    public static boolean isRawWifiConnector() {
        // 是否初始化正常
        return netConnect && netType == NetworkUtil.NETWORK_CLASS_WIFI;

    }

    /**
     * 重新检查网络状态，并返回当前网络状态
     * 目前会在：
     * 1.应用初始化
     * 2.网络变化
     * 2.亮屏
     * 4.解锁
     * 5.BaseActivity onCreate 中进行检查网络状态
     *
     * @param imContext 上下文
     * @return
     */
    public static boolean isNetConnect(Context imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        return netConnect;
    }

    /**
     * 这个方法依赖于 parserNetwork() 方法的判断
     * 目前是可靠的，一般情况下可以直接使用本方法获取网络状态
     *
     * @return the netConnect
     */
    public static boolean isNetRawConnect() {
        return netConnect;
    }

    /**
     * @return the netType
     */
    public static int getNetType(CorApplication imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        return netType;
    }

    public static int getRawNetType(CorApplication imContext) {
        return netType;
    }

    public static String getRawNetTypeName(CorApplication imContext) {
        return netTypeName;
    }

    public static String getNetSubTypeName(CorApplication imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        return netSubTypeName;
    }

    public static String getRawNetSubTypeName(CorApplication imContext) {
        return netSubTypeName;
    }

    /**
     * @return the netExtraType
     */
    public static String getNetExtraType(CorApplication imContext) {
        // 重新检查网络状态
        parserNetwork(null, imContext);

        return netExtraType;
    }

    public static String getRawNetExtraType(CorApplication imContext) {
        return netExtraType;
    }

    /**
     * 获取具体的网络类型
     * Wifi、2G、3G、4G、无法识别的移动网络、未知
     *
     * @param imContext   上下文
     * @param networkType 基础的网络类型：Wifi、mobile
     * @return
     */
    private static int getNetworkClass(Context imContext, int networkType) {
        if (networkType == ConnectivityManager.TYPE_WIFI) {
            return NetworkUtil.NETWORK_CLASS_WIFI;

        } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
            TelephonyManager telephonyManager = (TelephonyManager) imContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager == null) {
                return NetworkUtil.NETWORK_CLASS_MOBLE;
            }

            int mobileNetworkType = telephonyManager.getNetworkType();
            switch (mobileNetworkType) {
                case NetworkUtil.NETWORK_TYPE_GPRS:
                case NetworkUtil.NETWORK_TYPE_EDGE:
                case NetworkUtil.NETWORK_TYPE_CDMA:
                case NetworkUtil.NETWORK_TYPE_1xRTT:
                case NetworkUtil.NETWORK_TYPE_IDEN:
                    return NetworkUtil.NETWORK_CLASS_2_G;

                case NetworkUtil.NETWORK_TYPE_UMTS:
                case NetworkUtil.NETWORK_TYPE_EVDO_0:
                case NetworkUtil.NETWORK_TYPE_EVDO_A:
                case NetworkUtil.NETWORK_TYPE_HSDPA:
                case NetworkUtil.NETWORK_TYPE_HSUPA:
                case NetworkUtil.NETWORK_TYPE_HSPA:
                case NetworkUtil.NETWORK_TYPE_EVDO_B:
                case NetworkUtil.NETWORK_TYPE_EHRPD:
                case NetworkUtil.NETWORK_TYPE_HSPAP:
                    return NetworkUtil.NETWORK_CLASS_3_G;

                case NetworkUtil.NETWORK_TYPE_LTE:
                    return NetworkUtil.NETWORK_CLASS_4_G;

                default:
                    return NetworkUtil.NETWORK_CLASS_MOBLE;
            }
        }

        return NetworkUtil.NETWORK_CLASS_UNKNOWN;
    }

}
