package network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by rahul on 30-01-2016.
 * Singleton class that provides a reference to Volley's RequestQueue.
 * All JSON is parsed after a querying the network
 */
public class VolleySingleton {


    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    // Singleton class
    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    // Obtain a singleton reference to a Volley object
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    // Obtain a RequestQueue that queues network requests and provides responses asynchronously
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    // get reference to an ImageLoader if images need to be loaded via Volley.
    // Note- Picasso is used in this project
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
