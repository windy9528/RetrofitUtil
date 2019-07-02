package bwie.com.yanggaofeng20190629.util;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * date:2019/7/1
 * name:windy
 * function:
 */
@GlideModule
public class GlideUtil extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);

        int CacheSize = 1024 * 1204 * 20;
        builder.setMemoryCache(new LruResourceCache(CacheSize));

        Log.i("windy", "glide缓存" + context.getCacheDir().getAbsolutePath());

        ExternalPreferredCacheDiskCacheFactory cacheFactory = new ExternalPreferredCacheDiskCacheFactory(
                context, "glide", CacheSize
        );
        builder.setDiskCache(cacheFactory);
    }
}
