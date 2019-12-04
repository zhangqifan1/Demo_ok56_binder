package com.as.demo_ok56_binder;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * -----------------------------
 * Created by zqf on 2019/12/4.
 * ---------------------------
 * <p>
 * 管理和提供子进程的连接和消息服务
 */
public class RemoteService extends Service {

    private boolean isconnect = false;

    private RemoteCallbackList<MessageReceiverListener> messageReceiverListenerArrayList = new RemoteCallbackList<>();
    //    private ArrayList<MessageReceiverListener> messageReceiverListenerArrayList = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private IconnectionService connectionService = new IconnectionService.Stub() {


        @Override
        public void connect() throws RemoteException {
            //说是这里会阻塞 所以模拟一下阻塞

            try {
                Thread.sleep(3000);
                isconnect = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                });

                // 4 个参数

                // 1. 运行
                // 2.第一次运行的Delay
                // 3. 间隔
                // 4.单位
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
//
//                        for (MessageReceiverListener messageReceiverListener : messageReceiverListenerArrayList) {
//
//                            Message message = new Message();
//                            message.setContent(" 这里是从 RemoteService 接收到的消息");
//                            try {
//                                messageReceiverListener.onReceiveMessage(message);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }

                        int size = messageReceiverListenerArrayList.beginBroadcast();

                        for (int i = 0; i < size; i++) {
                            Message message = new Message();
                            message.setContent(" 这里是从 RemoteService 接收到的消息");
                            try {
                                MessageReceiverListener broadcastItem = messageReceiverListenerArrayList.getBroadcastItem(i);
                                broadcastItem.onReceiveMessage(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                        }
                        messageReceiverListenerArrayList.finishBroadcast();
                    }
                }, 5000, 5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            isconnect = false;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isConnect() throws RemoteException {
            return isconnect;
        }
    };


    private ImessageService imessageService = new ImessageService.Stub() {
        @Override
        public void sendMessage(final Message message) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });

            if (isconnect) {
                message.setSendSuccess(true);
            } else {
                message.setSendSuccess(false);
            }
        }

        @Override
        public void registerMessageReceiver(MessageReceiverListener messageReceiverListener) throws RemoteException {
            if (messageReceiverListenerArrayList != null) {
                messageReceiverListenerArrayList.register(messageReceiverListener);
            }

        }

        @Override
        public void unRegisterMessageReceiver(MessageReceiverListener messageReceiverListener) throws RemoteException {
            if (messageReceiverListenerArrayList != null) {
                messageReceiverListenerArrayList.unregister(messageReceiverListener);
            }
        }
    };


    private IServiceManager iServiceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            if (IconnectionService.class.getSimpleName().equals(serviceName)) {

                return connectionService.asBinder();
            } else if (ImessageService.class.getSimpleName().equals(serviceName)) {
                return imessageService.asBinder();
            }
            return null;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return iServiceManager.asBinder();
    }


    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;


    @Override
    public void onCreate() {
        super.onCreate();
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}
