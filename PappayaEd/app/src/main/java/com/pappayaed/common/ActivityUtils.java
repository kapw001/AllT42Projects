package com.pappayaed.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yasar on 2/5/18.
 */

public class ActivityUtils {


    public static void startActivity(Context packageContext, Class<?> cls, Bundle bundle) {

        Intent intent = getIntent(packageContext, cls, bundle);
        packageContext.startActivity(intent);

    }

    public static Intent getIntent(Context packageContext, Class<?> cls, Bundle bundle) {

        Intent intent = new Intent(packageContext, cls);
        intent.putExtras(bundle);

        return intent;
    }

    public static Intent getIntentWithAction(Context packageContext, Class<?> cls, Bundle bundle, String action) {

        Intent intent = new Intent(packageContext, cls);
        intent.setAction(action);
        intent.putExtras(bundle);

        return intent;
    }
}
