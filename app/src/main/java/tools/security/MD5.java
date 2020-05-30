package tools.security;

import java.security.MessageDigest;

public class MD5 {
    /**
     * MD5 加密
     * @param dataStr
     * @return
     */
    public static String MD5_Encrypt(String dataStr) {
        try {

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
