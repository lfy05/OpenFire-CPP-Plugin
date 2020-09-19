package com.OF.protocol;

public class Utilities {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static void addToByteArray(byte[] dest, int startIndex, byte[] source){
        for(int sourceLoc = 0; sourceLoc < source.length; sourceLoc++){
            dest[startIndex] = source[sourceLoc];
            startIndex++;
        }
    }
}
