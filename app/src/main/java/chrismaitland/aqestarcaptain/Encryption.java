package chrismaitland.aqestarcaptain;

import java.security.MessageDigest;


public class Encryption {

    public static String encrypt(String value) throws Exception {

        //reference: https://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash

        byte[] inputBytes = value.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digestBytes = md.digest(inputBytes);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digestBytes.length; ++i) {
            sb.append(Integer.toHexString((digestBytes[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();

    }
}
