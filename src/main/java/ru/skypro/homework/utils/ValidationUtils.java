package ru.skypro.homework.utils;

public class ValidationUtils {

    public static boolean isNotEmptyAndNotBlank(String str){
        return !(str==null||str.isEmpty()||str.isBlank());
    }

}
