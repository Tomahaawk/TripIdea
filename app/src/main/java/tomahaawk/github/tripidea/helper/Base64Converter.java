package tomahaawk.github.tripidea.helper;

import android.util.Base64;

public class Base64Converter {

    public static String toBase64(String text) {

        return  Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String fromBase64(String text) {

        return new String( Base64.decode(text, Base64.DEFAULT) );
    }
}
