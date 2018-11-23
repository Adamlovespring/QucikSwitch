package com.example.warningwang.quickswitch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;

public class ProxyActivity extends Activity {

    private static int SYSPROPS_TRANSACTION = ('_' << 24) | ('S' << 16) | ('P' << 8) | 'R';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SystemPropPoker().execute();
        finish();
    }

    static class SystemPropPoker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String[] services;
                try {
                    Method listServicesMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("listServices");
                    services = (String[]) listServicesMethod.invoke(null);
                } catch (Exception e) {
                    return null;
                }
                for (String service : services) {
                    Method checkServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("checkService", String.class);
                    IBinder obj = (IBinder) checkServiceMethod.invoke(null, service);
                    if (obj != null) {
                        Parcel data = Parcel.obtain();
                        try {
                            obj.transact(SYSPROPS_TRANSACTION, data, null, 0);
                        } catch (RemoteException e) {
                        } catch (Exception e) {
                        }
                        data.recycle();
                    }
                }

            } catch (Exception e) {
                return null;
            }

            return null;
        }
    }
}
