package com.quixom.apps.deviceinfo.utilities.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import com.quixom.apps.deviceinfo.DeviceInfoApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ApkIconModelLoader implements ModelLoader<String, Drawable> {
    private final Context context;

    public ApkIconModelLoader(@NonNull Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public LoadData<Drawable> buildLoadData(@NonNull String model,
                                            int width,
                                            int height,
                                            @NonNull Options options) {
        return new LoadData<>(new ObjectKey(model), new ApkIconDataFetcher(context, model, options));
    }

    @Override
    public boolean handles(@NonNull String model) {
        return model.startsWith("package://") || model.endsWith(".apk");
    }
    public static class Factory implements ModelLoaderFactory<String, Drawable> {
        @NonNull
        @Override
        public ModelLoader<String, Drawable> build(MultiModelLoaderFactory multiFactory) {
            return new ApkIconModelLoader(DeviceInfoApp.get());
        }

        @Override
        public void teardown() {
        }
    }
}