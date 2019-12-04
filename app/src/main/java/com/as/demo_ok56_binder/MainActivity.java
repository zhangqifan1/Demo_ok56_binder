package com.as.demo_ok56_binder;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IconnectionService connectionServiceProxy;
    private ImessageService messageServiceProxy;
    private IServiceManager serviceManagerProxy;
    private Button butConnect;
    private Button butDisConnect;
    private Button butIsConnect;
    private Button sendMessage;
    private Button receiveMessage;
    private Button unReceiveMessage;

    private MessageReceiverListener messageReceiverListener = new MessageReceiverListener.Stub() {
        @Override
        public void onReceiveMessage(final Message message) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "" + message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                serviceManagerProxy = IServiceManager.Stub.asInterface(iBinder);

                try {
                    IBinder con_binder = serviceManagerProxy.getService(IconnectionService.class.getSimpleName());
                    IBinder msg_binder = serviceManagerProxy.getService(ImessageService.class.getSimpleName());
                    connectionServiceProxy = IconnectionService.Stub.asInterface(con_binder);
                    messageServiceProxy = ImessageService.Stub.asInterface(msg_binder);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Service.BIND_AUTO_CREATE);


    }

    private void initView() {
        butConnect = (Button) findViewById(R.id.butConnect);
        butDisConnect = (Button) findViewById(R.id.butDisConnect);
        butIsConnect = (Button) findViewById(R.id.butIsConnect);

        butConnect.setOnClickListener(this);
        butDisConnect.setOnClickListener(this);
        butIsConnect.setOnClickListener(this);

        sendMessage = (Button) findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(this);
        receiveMessage = (Button) findViewById(R.id.receiveMessage);
        receiveMessage.setOnClickListener(this);
        unReceiveMessage = (Button) findViewById(R.id.unReceiveMessage);
        unReceiveMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butConnect:
                try {
                    connectionServiceProxy.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.butDisConnect:
                try {
                    connectionServiceProxy.disconnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.butIsConnect:
                try {
                    boolean connect = connectionServiceProxy.isConnect();
                    Toast.makeText(this, "" + connect, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sendMessage:
                Message message = new Message();
                message.setContent("从主页面发的消息");
                try {
                    messageServiceProxy.sendMessage(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.receiveMessage:
                try {
                    messageServiceProxy.registerMessageReceiver(messageReceiverListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.unReceiveMessage:
                try {
                    messageServiceProxy.unRegisterMessageReceiver(messageReceiverListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
