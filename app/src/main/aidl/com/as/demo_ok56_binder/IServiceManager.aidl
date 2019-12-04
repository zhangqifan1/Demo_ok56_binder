// IServiceManager.aidl
package com.as.demo_ok56_binder;

interface IServiceManager {
    IBinder getService(String serviceName);
}
