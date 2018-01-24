package ostrich.com.ostrich_robot.utils;


public class SerialDataUtils {

    /**
     * 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
     * @param num
     * @return
     */

    public static int isOdd(int num) {
        return num & 1;
    }

    /**
     * Hex字符串转int
     * @param inHex
     * @return
     */
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    /**
     * Hex字符串转byte
     * @param inHex
     * @return
     */
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * 1字节转2个Hex字符
     * @param inByte
     * @return
     */
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    /**
     * 字节数组转转hex字符串
     * @param inBytArr
     * @return
     */
    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    /**
     * 字节数组转转hex字符串，可选长度
     * @param inBytArr
     * @param offset
     * @param byteCount
     * @return
     */
    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    /**
     * 转hex字符串转字节数组
     * @param inHex
     * @return
     */
    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }
}
