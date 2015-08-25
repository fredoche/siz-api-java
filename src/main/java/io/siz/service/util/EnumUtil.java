package io.siz.service.util;

import java.util.regex.Pattern;

/**
 *
 * @author fred
 */
public class EnumUtil {

    /**
     * A non-word character: [^\w]
     */
    static Pattern nonword = Pattern.compile("\\W");

    public static <T extends Enum<?>> T valueOf(Class<T> enumeration, String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (keepLetters(each.name())
                    .equalsIgnoreCase(keepLetters(search))) {
                return each;
            }
        }
        throw new IllegalArgumentException("no conversion from " + search + " to " + enumeration);
    }

    private static String keepLetters(String each) {
        return nonword.matcher(each).replaceAll("");
    }
}
