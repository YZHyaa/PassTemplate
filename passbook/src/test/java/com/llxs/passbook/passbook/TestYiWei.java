package com.llxs.passbook.passbook;


public class TestYiWei {

    public static void main(String[] args) {
        encrypt("1001&79808080");
//        decrpt("pkqqk");
//        valid("1001&79808080");
    }

    public static void encrypt(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] += 10;
        }
        System.out.println(new String(chars));
    }


    public static void decrpt(String secret) {
        char[] chars = secret.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] -= 10;
        }
        System.out.println(new String(chars));
    }

    public static void valid(String str) {
        String[] split = str.split("&");
        long goodsId = Long.valueOf(split[0]);
        long templateId = Long.valueOf(split[1]);
        System.out.println(goodsId);
        System.out.println(templateId);
    }


}
