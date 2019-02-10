package gr.kalymnos.sk3m3l10.ddosdroid.utils;

import android.os.Bundle;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

public final class ValidationUtils {

    public static boolean bundleContainsKey(Bundle bundle, String key) {
        if (bundle != null && bundle.containsKey(key)) {
            return true;
        }
        return false;
    }
}
