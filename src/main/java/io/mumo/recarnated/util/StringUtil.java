package io.mumo.recarnated.util;

import io.github.encryptorcode.pluralize.Pluralize;
import org.springframework.stereotype.Component;

//@Component
public class StringUtil {

    public static String camelCase(String str) {
        str = str.trim();
        str = String.valueOf(str.charAt(0)).toLowerCase() + str.substring(1);
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNextChar = false;
        for (int i = 0; i < str.length(); i++) {
            String character = String.valueOf(str.charAt(i));
            if (character.equals("_")) {
                capitalizeNextChar = true;
                continue;
            }
            if (capitalizeNextChar) {
                capitalizeNextChar = false;
                sb.append(character.toUpperCase());
            } else {
                sb.append(character);
            }

        }
        return sb.toString();
    }

    public static String titleCase(String str) {
        String s = camelCase(str);
        return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
    }

    public static String snakeCase(String str) {
        str = camelCase(str.trim());
        String character = String.valueOf(str.charAt(0));
        StringBuilder sb = new StringBuilder(character.toLowerCase());
        for (int i = 1; i < str.length(); i++) {
             character = String.valueOf(str.charAt(i));
            if (Character.isAlphabetic(str.charAt(i)) && Character.isUpperCase(str.charAt(i))) {
                sb.append("_");
            }
            sb.append(character.toLowerCase());
        }
        return sb.toString();
    }

    public static String pluralize(String str) {
        return Pluralize.pluralize(str);
    }

    public static String singular(String str) {
        return Pluralize.singular(str);
    }


}
