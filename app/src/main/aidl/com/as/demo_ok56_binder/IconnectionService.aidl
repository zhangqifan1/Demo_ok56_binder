// IconnectionService.aidl
package com.as.demo_ok56_binder;

// Declare any non-default types here with import statements

//连接服务  右键项目 app  ->new -> Aidl文件   这个里边的方法写完之后 运行一下 需要让他编译出来 实现类
interface IconnectionService {

/**
 *这里一般会阻塞 会影响到主线程
 * 会使用到oneway 关键字
 * 但是一旦使用了这个关键字 该方法不可以有返回值
 *
 *
 * 可以看到加上oneway之后不影响 主线程了  button 的 按住背景色 也没了
 */
   oneway  void connect();

    void disconnect();

    boolean isConnect();
}
