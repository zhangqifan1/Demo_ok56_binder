// MessageReceiverListener.aidl
package com.as.demo_ok56_binder;
import  com.as.demo_ok56_binder.Message; /// !!!! 注意这里是需要手写的

// 监听消息接收
interface MessageReceiverListener {

      void onReceiveMessage( in Message message);

}
