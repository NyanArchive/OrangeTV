package com.amazon.identity.auth.device.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;

import tv.orange.core.Core;

public class PackageSignatureUtil {
    /* ... */

    private static Signature[] getAndroidSignaturesFor(String str, Context context) {
        PackageInfo packageInfo = null;

        /* ... */

        return Core.hookPackageSignature(packageInfo.signatures); // TODO: __INJECT_CODE
    }

    /* ... */
}
