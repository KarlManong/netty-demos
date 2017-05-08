package com.ly.dc.netty.utils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;

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
public class DynamicPortFinder {
    /**
     * The minimum number of server port number.
     */
    public static final int MIN_PORT_NUMBER = 49152;

    /**
     * The maximum number of server port number.
     */
    public static final int MAX_PORT_NUMBER = 61000;

    private static final Random rd = new Random(System.currentTimeMillis());


    private DynamicPortFinder() {

    }

    public static int getNextAvailable() {
        int port = randomPort();
        while (!isAvalible(port)) {
            port = randomPort();
        }
        return port;
    }

    private static int randomPort() {
        return rd.nextInt(MAX_PORT_NUMBER - MIN_PORT_NUMBER) + MIN_PORT_NUMBER;
    }

    private static boolean isAvalible(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
            return false;
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException ignore) {
                    // ignore
                }
            }
        }
    }


}
