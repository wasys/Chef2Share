package br.com.chef2share;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;

import br.com.chef2share.domain.service.SuperService;

/**
 * Created by Jonas on 11/11/2015.
 */
@EApplication
public class SuperApplication extends Application{

    private static Context context;
    private RequestQueue mRequestQueue;
    private static SuperApplication instance;

    private static SuperCache superCache;


    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        init();


        instance = this;

        SuperApplication.context = getBaseContext();

        if(SuperService.SIMULA_SERVER) {
            SuperUtil.toast(this, "**** MODO FAKE LIGADO ****");
        }
    }

    public static SuperCache getSuperCache() {
        if(superCache == null){
            superCache = new SuperCache();
        }
        return superCache;
    }

    public static synchronized SuperApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Context getContext(){
       return context;
    }

    @Background
    void init() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
