package com.serialmmf.Anbattery.util;

import android.content.Context;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by juancarlos on 14/2/16.
 */
public class LanguageManager {
    private static LanguageManager mInstance;
    private final Context mContext;

    private ArrayList<LangChangeObserver> mObservers;

    public interface LangChangeObserver {
        void onLangChange();
    }

    private LanguageManager(Context context) {
        mContext = context.getApplicationContext();
        mObservers = new ArrayList<>();
    }

    public static LanguageManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LanguageManager(context);
        }

        return mInstance;
    }

    public void addObserver(LangChangeObserver observer) {
        mObservers.add(observer);
    }

    public void notifyObservers() {
        for(LangChangeObserver observer : mObservers) {
            observer.onLangChange();
        }
    }

    public String getLang() {
        String lang = PrefsManager.getInstance(mContext).getLang();

        if (lang != null) {
            setLang(lang);
        }

        return  Locale.getDefault().getLanguage();
    }

    public void setLang(String lang) {
        PrefsManager.getInstance(mContext).setLang(lang);

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getApplicationContext().getResources().updateConfiguration(config, null);

        notifyObservers();
    }
}
