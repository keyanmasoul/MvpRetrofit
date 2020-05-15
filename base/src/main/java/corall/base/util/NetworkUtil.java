package corall.base.util;

import android.util.Log;

import java.net.InetAddress;
import java.net.URI;

/**
 * 与网络状态、类型相关的常量定义
 * Created by linlin_91 on 2015/6/24.
 */
public class NetworkUtil {

    // API中隐藏参数，数值客户端自定义。
    // 网络类型未知
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    // 网络类型为未识别的移动网络
    public static final int NETWORK_CLASS_MOBLE = 0;
    // 网络类型为WIFI
    public static final int NETWORK_CLASS_WIFI = 10;
    // 移动网络类型为2G
    public static final int NETWORK_CLASS_2_G = 50;
    // 移动网络类型为3G
    public static final int NETWORK_CLASS_3_G = 60;
    // 移动网络类型为4G
    public static final int NETWORK_CLASS_4_G = 70;
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;

    // 具体的网络类型
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    private static final String TAG = NetworkUtil.class.getSimpleName();

    /**
     * 域名解析
     * 注意：此方法不能在主线程（UI线程）中执行，会抛异常！！！！！
     * 需要在事件响应代码或其他线程中执行
     *
     * @param dnsName 需要解析的域名，如 market.hiapk.com
     * @return
     */
    public static String dnsNameToip(String dnsName) {
        String ip = null;
        InetAddress[] addrs = null;
        try {
            addrs = InetAddress.getAllByName(dnsName);

        } catch (Exception e) {
            Log.e(TAG, "cannot DNS translate :" + dnsName);
        }

        if (addrs != null && addrs.length >= 1) {
            ip = addrs[0].getHostAddress();
        }

        return ip;
    }

    /**
     * 输入url的地址，解析出对应的域名
     *
     * @param urlString
     * @return
     */
    public static String getUrlDomain(String urlString) {
        String uriDomain = null;
        try {
            URI uri = new URI(urlString);
            uriDomain = uri.getHost();

        } catch (Exception e) {
            Log.e(TAG, "cannot get Host :" + urlString);
        }

        return uriDomain;
    }

}
