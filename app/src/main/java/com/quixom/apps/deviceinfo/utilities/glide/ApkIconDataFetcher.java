package com.quixom.apps.deviceinfo.utilities.glide;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.File;

import androidx.annotation.NonNull;

class ApkIconDataFetcher implements DataFetcher<Drawable> {

    private final Context context;
    private final String model;
    private final Options options;

    ApkIconDataFetcher(Context context, @NonNull String model, @NonNull Options options) {
        this.context = context;
        this.model = model;
        this.options = options;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Drawable> dataCallback) {
        Drawable d = null;
        PackageManager pm = context.getPackageManager();
        if (model.startsWith("package://")) {
            try {
                String packageName = model.substring(10);
                ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
                d = ai.loadIcon(pm);
            } catch (Exception e) {
                //
            }
        } else {
            d = getApkIcon(context, model);
        }
        dataCallback.onDataReady(d);
    }

    private static Drawable getApkIcon(@NonNull Context context, String apkPath) {
        if (!new File(apkPath).exists()) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);

        if (info == null) {
            return null;
        }
        ApplicationInfo appInfo = info.applicationInfo;
        appInfo.sourceDir = apkPath;
        appInfo.publicSourceDir = apkPath;
        try {
            return appInfo.loadIcon(pm);
        } catch (OutOfMemoryError e) {
            //ignore
        }
        return null;
    }


    @Override
    public void cleanup() {
    }

    @Override
    public void cancel() {
    }

    @NonNull
    @Override
    public Class<Drawable> getDataClass() {
        return Drawable.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}