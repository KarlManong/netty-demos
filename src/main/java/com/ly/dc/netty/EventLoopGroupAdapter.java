package com.ly.dc.netty;

import io.netty.channel.SelectStrategyFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorChooserFactory;
import io.netty.util.concurrent.RejectedExecutionHandler;

import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class EventLoopGroupAdapter extends NioEventLoopGroup implements CloseableEventLoopGroup {
    public EventLoopGroupAdapter() {
        super();
    }

    public EventLoopGroupAdapter(int nThreads) {
        super(nThreads);
    }

    public EventLoopGroupAdapter(int nThreads, ThreadFactory threadFactory) {
        super(nThreads, threadFactory);
    }

    public EventLoopGroupAdapter(int nThreads, Executor executor) {
        super(nThreads, executor);
    }

    public EventLoopGroupAdapter(int nThreads, ThreadFactory threadFactory, SelectorProvider selectorProvider) {
        super(nThreads, threadFactory, selectorProvider);
    }

    public EventLoopGroupAdapter(int nThreads, ThreadFactory threadFactory, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory) {
        super(nThreads, threadFactory, selectorProvider, selectStrategyFactory);
    }

    public EventLoopGroupAdapter(int nThreads, Executor executor, SelectorProvider selectorProvider) {
        super(nThreads, executor, selectorProvider);
    }

    public EventLoopGroupAdapter(int nThreads, Executor executor, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory) {
        super(nThreads, executor, selectorProvider, selectStrategyFactory);
    }

    public EventLoopGroupAdapter(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory) {
        super(nThreads, executor, chooserFactory, selectorProvider, selectStrategyFactory);
    }

    public EventLoopGroupAdapter(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        super(nThreads, executor, chooserFactory, selectorProvider, selectStrategyFactory, rejectedExecutionHandler);
    }

    @Override
    public void close() throws IOException {
        this.shutdownGracefully().syncUninterruptibly();
    }
}
