package com.quixom.apps.deviceinfo.utilities.glide;


import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.request.BaseRequestOptions;
import com.google.android.gms.common.api.Api;

import androidx.annotation.NonNull;

@GlideExtension
public final class DIGlideExtension {

    private DIGlideExtension() {

    }

    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> squareThumb(BaseRequestOptions<?> requestOptions) {
        return requestOptions.centerCrop();
    }

//    @NonNull
//    @GlideOption
//    public static BaseRequestOptions<?> squareMiniThumb(BaseRequestOptions<?> requestOptions) {
//        return requestOptions.centerCrop().override(Api.SQUARE_THUMB_SIZE);
//    }
}