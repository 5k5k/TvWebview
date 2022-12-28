package com.morladim.tvwebview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class ImageLoader {
    private static ImageLoader imageLoader = new ImageLoader();
    public static ImageLoader getInstance() {
        return imageLoader;
    }

    private ImageLoader() {

    }

    public void load(@NonNull String url, @NonNull ImageView imageView) {
        Glide.with(imageView).load(url).into(imageView);
    }


    public void load(@NonNull String url, @NonNull ImageView imageView, @Nullable ImageLoaderListener imageLoaderListener) {
        Glide.with(imageView).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (imageLoaderListener != null) {
                    imageLoaderListener.onFail();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (imageLoaderListener != null) {
                    imageLoaderListener.onSuccess(bitmap);
                }
                return false;
            }
        }).into(imageView);
    }

    public void download(@NonNull Context context, @NonNull String url, @Nullable ImageLoaderListener imageLoaderListener) {
        Glide.with(context).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (imageLoaderListener != null) {
                    imageLoaderListener.onFail();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (imageLoaderListener != null) {
                    imageLoaderListener.onSuccess(bitmap);
                }
                return false;
            }
        }).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    public File getFileCached(String mUrl) {
        //TODO get file cached
        return null;
    }


    public File getDiskCacheFile(String mUrl) {
        //TODO get cache file
        return null;
    }


    public interface ImageLoaderListener {
        void onSuccess(Bitmap bitmap);
        void onFail();
    }
}
