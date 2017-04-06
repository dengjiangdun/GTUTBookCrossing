package com.deng.johndon.gdutbookcrossing.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DELL on 2017/3/16.
 */

public class CheckUtil {

    public static boolean checkIsEmail(String email){
        String string = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean checkIsPhoneNumber(String phoneNumber){
        String regex= "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
        Pattern p = Pattern.compile(regex);
        Matcher matcher =p.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean checkISBN(String isbn) {
       /* String regex = "/^\\d{3}-\\d-\\d{3}-\\d{5}-\\d$/";
        Pattern pattern =Pattern.compile(regex);
        Matcher matcher = pattern.matcher(isbn);
        //return  matcher.matches();*/
        String frontStr = isbn.substring(0, isbn.length()-1);
        String backStr = isbn.substring(isbn.length() - 1);
        boolean isNum = frontStr.matches("[0-9]+");
        if (!isNum || !(frontStr.length() == 9 || frontStr.length() == 12))
        {
            return false;
        }
        char[] tmp = frontStr.toCharArray();
        int sum = 0;
        int count = 10;
        if (frontStr.length() == 9)
        {//验证10位的ISBN
            for (int i = 0; i < 9; i++)
            {

                int dd = Integer.parseInt(tmp[i] + "");

                sum = sum + count * dd;

                count--;

            }
            int n = 11 - sum % 11;
            String s = "";
            if (n == 11)
            {
                s = "0";
            }
            else if (n == 10)
            {
                s = "x";
            }
            else
            {
                s = "" + n;
            }

            if (backStr.toLowerCase().equals(s))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (frontStr.length() == 12)
        {//验证13位的ISBN
            String str = isbn.substring(0,3);
            if(!(str.equals("979")||str.equals("978"))){
                return false;
            }
            for (int i = 0; i < 12; i++)
            {
                int dd = Integer.parseInt(tmp[i] + "");
                if(i%2==0){
                    sum = sum +1*dd;
                }else{
                    sum = sum +3*dd;
                }
            }
            String s = ""+(10-sum%10);

            if (backStr.equals(s))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

    }


}
