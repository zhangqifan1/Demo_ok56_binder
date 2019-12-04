// ImessageService.aidl
package com.as.demo_ok56_binder;
import  com.as.demo_ok56_binder.Message; /// !!!! 注意这里是需要手写的
import  com.as.demo_ok56_binder.MessageReceiverListener; /// !!!! 注意这里是需要手写的
// 消息服务
interface ImessageService {
// 只有实体类  需要 in
        void sendMessage(in Message message);


        void registerMessageReceiver(MessageReceiverListener messageReceiverListener);


        void unRegisterMessageReceiver(MessageReceiverListener messageReceiverListener);


}
