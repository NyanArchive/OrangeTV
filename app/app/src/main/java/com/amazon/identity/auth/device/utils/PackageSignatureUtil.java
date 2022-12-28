package com.amazon.identity.auth.device.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;

import tv.orange.core.CoreHook;

public class PackageSignatureUtil {
    /* ... */

    private static Signature[] getAndroidSignaturesFor(String str, Context context) {
        PackageInfo packageInfo = null;

        /* ... */

        return CoreHook.hookPackageSignature(packageInfo.signatures); // TODO: __INJECT_CODE
    }

    /* ... */
}
