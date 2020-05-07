package corall.base.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 与字串相关的工具类
 * Created by linlin_91 on 2015/6/17.
 */
public class StringUtil {

    // 格式化有小数的字符串用
    public static final String SIZE_FORMAT = "0.##";
    // 下载进度条用到
    public static final String DOWNLOAD_FORMAT = "0.#";

    private static int STRING_PASSWORD = 0;

    private static Map<Integer, String> stringResMap = new HashMap<>();
    /**
     * 字符串名字比较器 特殊-数字-英语-汉语
     */
    public static final Comparator<String> CHAIN_NAME_COMPARE = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            char[] a1 = o1.toCharArray();
            char[] a2 = o2.toCharArray();

            for (int i = 0; i < a1.length && i < a2.length; i++) {
                int c1 = getGBCode(a1[i]);
                int c2 = getGBCode(a2[i]);

                if (c1 == c2) {
                    continue;
                }

                return c1 - c2;
            }

            if (a1.length == a2.length) {
                return 0;
            }

            return a1.length - a2.length;
        }
    };
    // 清除所有空白时用
    private static final Pattern pattern = Pattern.compile("(^\\s*)|(\\s*$)");
    // 字母Z使用了两个标签，这里有２７个值
    // i, u, v都不做声母, 跟随前面的字母
    private final static char[] CHAR_TABLE = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃', '妈', '拿',
            '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'};
    private final static char[] ALPHA_TABLE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    // 字母对应的区位码值
    private final static int CHAR_CODE[] = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594,
            2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};
    // 存放所有国标二级汉字读音,用于次常用汉字
    private final static String SECONDARY_TABLE = "CJWGNSPGCGNE[Y[BTYYZDXYKYGT[JNNJQMBSGZSCYJSYY"
            +
            "[PGKBZGY[YWJKGKLJYWKPJQHY[W" +
            "[DZLSGMRYPYWWCCKZNKYYGTTNJJNYKKZYTCJNMCYLQLYPYQFQRPZSLWBTGKJFYXJWZLTBNCXJJJJTXDTTSQZYCDXXHGCK"
            +
            "[PHFFSS[YBGXLPPBYLL[HLXS[ZM[JHSOJNGHDZQYKLGJHSGQZHXQGKEZZWYSCSCJXYEYXADZPMDSSMZJZQJYZC[J"
            +
            "[WQJBYZPXGZNZCPWHKXHQKMWFBPBYDTJZZKQHYLYGXFPTYJYYZPSZLFCHMQSHGMXXSXJ["
            +
            "[DCSBBQBEFSJYHXWGZKPYLQBGLDLCCTNMAYDDKSSNGYCSGXLYZAYBNPTSDKDYLHGYMYLCXPY"
            +
            "[JNDQJWXQXFYYFJLEJPZRXCCQWQQSBNKYMGPLBMJRQCFLNYMYQMSQYRBCJTHZTQFRXQHXMJJCJLXQGJMSHZKBSWYEMYLTXFSYDSWLYCJQXSJNQBSCTYHBFTDCYZDJWY"
            +
            "GHQFRXWCKQKXEBPTLPXJZSRMEBWHJLBJSLYYSMDXLCLQKXLHXJRZJMFQHXHWYWSBHTRXXGLHQHFNM[YKLDYXZPYLGG" +
            "[MTCFPAJJZYLJTYANJGBJPLQGDZYQY"
            +
            "AXBKYSECJSZNSLYZHSXLZCGHPXZHZNYTDSBCJKDLZAYFMYDLEBBGQYZKXGLDNDNYSKJSHDLYXBCGHXYPKDJMMZNGMMCLGWZSZXZJFZNMLZZTHCSYDBDLLSCDD"
            +
            "NLKJYKJSYCJLKWHQASDKNHCSGANHDAASHTCPLCPQYBSDMPJLPZJOQLCDHJJYSPRCHN" +
            "[NNLHLYYQYHWZPTCZGWWMZFFJQQQQYXACLBHKDJXDGMMYDJXZLLSYGX"
            +
            "GKJRYWZWYCLZMSSJZLDBYD[FCXYHLXCHYZJQ[[QAGMNYXPFRKSSBJLYXYSYGLNSCMHZWWMNZJJLXXHCHSY" +
            "[[TTXRYCYXBYHCSMXJSZNPWGPXXTAYBGAJCXLY"
            +
            "[DCCWZOCWKCCSBNHCPDYZNFCYYTYCKXKYBSQKKYTQQXFCWCHCYKELZQBSQYJQCCLMTHSYWHMKTLKJLYCXWHEQQHTQH[PQ"
            +
            "[QSCFYMNDMGBWHWLGSLLYSDLMLXPTHMJHWLJZYHZJXHTXJLHXRSWLWZJCBXMHZQXSDZPMGFCSGLSXYMJSHXPJXWMYQKSMYPLRTHBXFTPMHYXLCHLHLZY"
            +
            "LXGSSSSTCLSLDCLRPBHZHXYYFHB[GDMYCNQQWLQHJJ[YWJZYEJJDHPBLQXTQKWHLCHQXAGTLXLJXMSL[HTZKZJECXJCJNMFBY" +
            "[SFYWYBJZGNYSDZSQYRSLJ"
            +
            "PCLPWXSDWEJBJCBCNAYTWGMPAPCLYQPCLZXSBNMSGGFNZJJBZSFZYNDXHPLQKZCZWALSBCCJX" +
            "[YZGWKYPSGXFZFCDKHJGXDLQFSGDSLQWZKXTMHSBGZMJZRGLYJ"
            +
            "BPMLMSXLZJQQHZYJCZYDJWBMYKLDDPMJEGXYHYLXHLQYQHKYCWCJMYYXNATJHYCCXZPCQLBZWWYTWBQCMLPMYRJCCCXFPZNZZLJPLXXYZTZLGDLDCKLYRZZGQTG"
            +
            "JHHGJLJAXFGFJZSLCFDQZLCLGJDJCSNZLLJPJQDCCLCJXMYZFTSXGCGSBRZXJQQCTZHGYQTJQQLZXJYLYLBCYAMCSTYLPDJBYREGKLZYZHLYSZQLZNWCZCLLWJQ"
            +
            "JJJKDGJZOLBBZPPGLGHTGZXYGHZMYCNQSYCYHBHGXKAMTXYXNBSKYZZGJZLQJDFCJXDYGJQJJPMGWGJJJPKQSBGBMMCJSSCLPQPDXCDYYKY[CJDDYYGYWRHJRTGZ"
            +
            "NYQLDKLJSZZGZQZJGDYKSHPZMTLCPWNJAFYZDJCNMWESCYGLBTZCGMSSLLYXQSXSBSJSBBSGGHFJLYPMZJNLYYWDQSHZXTYYWHMZYHYWDBXBTLMSYYYFSXJC[DXX"
            +
            "LHJHF[SXZQHFZMZCZTQCXZXRTTDJHNNYZQQMNQDMMG" +
            "[YDXMJGDHCDYZBFFALLZTDLTFXMXQZDNGWQDBDCZJDXBZGSQQDDJCMBKZFFXMKDMDSYYSZCMLJDSYNSBRS"
            +
            "KMKMPCKLGDBQTFZSWTFGGLYPLLJZHGJ" +
            "[GYPZLTCSMCNBTJBQFKTHBYZGKPBBYMTDSSXTBNPDKLEYCJNYDDYKZDDHQHSDZSCTARLLTKZLGECLLKJLQJAQNBDKKGHP"
            +
            "JTZQKSECSHALQFMMGJNLYJBBTMLYZXDCJPLDLPCQDHZYCBZSCZBZMSLJFLKRZJSNFRGJHXPDHYJYBZGDLQCSEZGXLBLGYXTWMABCHECMWYJYZLLJJYHLG[DJLSLY"
            +
            "GKDZPZXJYYZLWCXSZFGWYYDLYHCLJSCMBJHBLYZLYCBLYDPDQYSXQZBYTDKYXJY" +
            "[CNRJMPDJGKLCLJBCTBJDDBBLBLCZQRPPXJCJLZCSHLTOLJNMDDDLNGKAQHQH"
            +
            "JGYKHEZNMSHRP[QQJCHGMFPRXHJGDYCHGHLYRZQLCYQJNZSQTKQJYMSZSWLCFQQQXYFGGYPTQWLMCRNFKKFSYYLQBMQAMMMYXCTPSHCPTXXZZSMPHPSHMCLMLDQF"
            +
            "YQXSZYYDYJZZHQPDSZGLSTJBCKBXYQZJSGPSXQZQZRQTBDKYXZKHHGFLBCSMDLDGDZDBLZYYCXNNCSYBZBFGLZZXSWMSCCMQNJQSBDQSJTXXMBLTXZCLZSHZCXRQ"
            +
            "JGJYLXZFJPHYMZQQYDFQJJLZZNZJCDGZYGCTXMZYSCTLKPHTXHTLBJXJLXSCDQXCBBTJFQZFSLTJBTKQBXXJJLJCHCZDBZJDCZJDCPRNPQCJPFCZLCLZXZDMXMPH"
            +
            "JSGZGSZZQLYLWTJPFSYASMCJBTZKYCWMYTCSJJLJCQLWZMALBXYFBPNLSFHTGJWEJJXXGLLJSTGSHJQLZFKCGNNNSZFDEQFHBSAQTGYLBXMMYGSZLDYDQMJJRGBJ"
            +
            "TKGDHGKBLQKBDMBYLXWCXYTTYBKMRTJZXQJBHLMHMJJZMQASLDCYXYQDLQCAFYWYXQHZ";
    /**
     * 初始化 拼音声母 对照表
     */
    private static int[] table = new int[27];

    static {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(CHAR_TABLE[i]);
        }
    }

    /**
     * 清除所有空白
     *
     * @param oldStr
     * @return
     */
    public static String clearBlankChar(String oldStr) {
        return pattern.matcher(oldStr).replaceAll("");

    }

    /**
     * 是否是空字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static void setStringResPassword(int p) {
        STRING_PASSWORD = p;
    }

    public static String decodeStringRes(Context ctx, int res, Object... formatArgs) {
        String rawData = decodeRawStringRes(ctx, res, STRING_PASSWORD);
        try {
            return String.format(rawData, formatArgs);
        } catch (Exception e) {
            return rawData;
        }
    }

    private static String decodeRawStringRes(Context ctx, int res, int pwd) {
        if (pwd == 0) {
            return ctx.getString(res);
        }
        if (stringResMap.containsKey(res)) {
            return stringResMap.get(res);
        }
        try {
            String data = ctx.getString(res);
            byte[] decode = Base64.decode(data.getBytes());

            for (int i = 0; i < decode.length; i++) {
                decode[i] += pwd;
            }

            String result = new String(decode);
            result = Html.fromHtml(result).toString();
            result = StringEscapeUtils.unescapeJava(result);
            stringResMap.put(res, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return str != null && (str.matches("\\d+") || str.matches("-\\d+"));

    }

    /**
     * 是否是有效的邮箱格式
     *
     * @param emailStr
     * @return
     */
    public static boolean isEmail(String emailStr) {
        String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\" +
                ".)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(emailStr);
        return m.matches();

    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param content
     * @return
     */
    // TODO 这里面的循环有待优化
    public static boolean isDigital(String content) {
        for (int i = 0; i < content.length(); i++) {
            if (!Character.isDigit(content.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 编码url
     *
     * @param content
     * @return
     */
    // TODO 这里面的return语句语意要优化
    public static String encodeContentForUrl(String content) {
        try {
            return (content == null ? "" : URLEncoder.encode(content, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();

        }
        return content;
    }

    /**
     * 解码url
     *
     * @param content
     * @return
     */
    // TODO 这里面的return语句语意要优化
    public static String decodeContentForUrl(String content) {
        try {
            return (content == null ? "" : URLDecoder.decode(content, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 主函数,输入字符,得到他的声母,英文字母返回对应的大写字母，其他非简体汉字返回 '0'
     *
     * @param ch
     * @return
     */
    public static char char2Alpha(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return (char) (ch - 'a' + 'A');
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        int gb = gbValue(ch);
        if (gb < table[0]) {
            return '0';
        }
        // 超出常用汉字范围，归为次常用汉字（即二级汉字）
        if (gb > table[table.length - 1]) {
            return charSecondaryAlpha(ch);
        }
        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb)) {
                break;
            }
        }
        if (i >= 26) {
            return '0';
        } else {
            return ALPHA_TABLE[i];
        }
    }


    private static boolean match(int i, int gb) {

        if (gb < table[i]) {
            return false;
        }
        int j = i + 1;

        // 字母Z使用了两个标签
        while (j < 26 && (table[j] == table[i]))
            ++j;
        if (j == 26) {
            return gb <= table[j];
        } else {
            return gb < table[j];
        }
    }

    /**
     * 取出汉字的编码
     *
     * @param ch
     * @return
     */
    private static int gbValue(char ch) {

        String str = "";
        str += ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取出汉字的编码,此方法提示次常用汉字的编码获取
     *
     * @param ch
     * @return
     */
    private static char charSecondaryAlpha(char ch) {
        // 二级字库偏移量
        int ioffset = 0;
        String str = "";
        str += ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2) {
                return '0';

            } else {
                int areaCode = 96 + bytes[0]; // 区码
                int posCode = 96 + bytes[1]; // 位码
                int charCode = areaCode * 100 + posCode;
                if (charCode > (CHAR_CODE[0] - 1) && (charCode < CHAR_CODE[CHAR_CODE.length - 1])) {
                    for (int index = 0; index < CHAR_CODE.length; index++) {
                        if (charCode < CHAR_CODE[index]) {
                            return ALPHA_TABLE[index - 1];

                        }
                    }

                } else {
                    ioffset = (areaCode - 56) * 94 + posCode - 1;
                    if (ioffset >= 0 && ioffset <= 3007) {
                        return SECONDARY_TABLE.substring(ioffset, ioffset + 1).charAt(0);

                    }
                }
            }

            return '0';

        } catch (Exception e) {
            return '0';
        }
    }

    /**
     * 算出这个字符的BGCode值
     *
     * @param c
     * @return int
     */
    private static int getGBCode(char c) {
        byte[] bytes = null;
        int a = 0, b = 0;
        try {
            bytes = String.valueOf(c).getBytes("gbk");
            if (bytes.length == 1) {
                return bytes[0];
            }
            a = bytes[0] - 0xA0 + 256;
            b = bytes[1] - 0xA0 + 256;
            return a * 100 + b;
        } catch (UnsupportedEncodingException e) {
        }

        return a * 100 + b;
    }

    /**
     * 获取大小（单位为MB）
     *
     * @param size
     */
    public static String getMBSizeDes(long size) {
        size = (size == 0 ? 1 : size);
        return sizeFormat(size / 1024.0, "MB");
    }

    /**
     * <pre>
     * 字节单位转化转换
     * 数值在1~1024，单位根据大小变化而变化,最大单位为GB
     * </pre>
     *
     * @param size 传进来的必须是kb
     */
    public static String formatKByteDes(long size) {
        size = (size == 0 ? 1 : size);
        if (size > 1024 * 1024) {
            return sizeFormat((double) size / (1024.0 * 1024), "GB");

        } else if (size > 1024) {
            return sizeFormat((double) size / 1024, "MB");

        } else {
            return sizeFormat((double) size, "KB");
        }

    }

    /**
     * 大小
     *
     * @param size
     * @param unit
     * @return
     */
    public static String sizeFormat(double size, String unit) {
        NumberFormat sizeFormat = new DecimalFormat(SIZE_FORMAT);

        return sizeFormat.format(size) + unit;
    }

    /**
     * 现在进度大小
     *
     * @param size
     * @param dSize
     * @return
     */
    public static String downloadFormat(double size, double dSize) {
        if (size <= dSize) {
            return "100%";

        } else {
            if (size == 0) {
                size = 1;
            }

            NumberFormat downlaodFormat = new DecimalFormat(DOWNLOAD_FORMAT);
            return downlaodFormat.format(dSize / size * 100) + "%";
        }
    }

    /**
     * 获得一级域名
     *
     * @param url
     * @return
     */
    public static String getDomainName(String url) {
        if (StringUtil.isEmptyString(url)) {
            return null;
        }
        String domainStr = null;
        try {
            Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv|name|jp)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(url);
            matcher.find();
            domainStr = matcher.group().toLowerCase();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        return domainStr;

    }

    /**
     * 比较两个字符串是否相等，任何一个为null则返回false
     *
     * @param text1
     * @param text2
     * @return
     */
    public static boolean nullSafeEquals(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return false;
        } else {
            return text1.equals(text2);
        }
    }

    /**
     * 从URI解析出参数
     *
     * @param query    url
     * @param outQuery 输出的数据（Map）
     */
    public static void parseUriParams(String query, Map<String, String> outQuery) {
        if (TextUtils.isEmpty(query)) {
            return;
        }
        String[] paramPair = query.split("&");
        for (String param : paramPair) {
            String[] paramArray = param.split("=");
            if (paramArray.length == 2) {
                outQuery.put(paramArray[0], paramArray[1]);
            }
        }
    }

    /**
     * 拼接Map的参数为字符串
     *
     * @param params 参数
     * @return 拼接字符串
     */
    public static String appendUriParams(Map<String, String> params) {
        StringBuffer uriBuffer = new StringBuffer();

        if (params != null && !params.isEmpty()) {
            int count = 0;
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            int entrySize = entrySet.size();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuffer.append(entry.getKey());
                uriBuffer.append("=");
                uriBuffer.append(entry.getValue());
                count++;

                if (count != entrySize) {
                    uriBuffer.append("&");
                }
            }
        }

        return uriBuffer.toString();
    }

    /**
     * 转换map为JsonObject
     *
     * @param params map
     * @return
     */
    public static JSONObject convertMapToJsonObject(Map<String, String> params) {
        JSONObject result = new JSONObject();
        try {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                result.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 使用SpannableString对文字进行处理。粗体
     *
     * @param completeInfStr
     * @param spanStr
     * @return
     */
    public static SpannableString addBoldStyleSpan(String completeInfStr, String spanStr) {
        SpannableString spannableString = new SpannableString(completeInfStr);
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(span, 0, spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
