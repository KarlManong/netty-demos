package com.ly.dc.netty;

import com.ly.dc.netty.clients.TestClient;
import com.ly.dc.netty.servers.TestServer;
import com.ly.dc.netty.utils.DynamicPortFinder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
public class MainTest {
    private static TestServer testServer;
    private static TestClient testClient;

    @BeforeClass
    public static void startAll() throws InterruptedException {
        int port = DynamicPortFinder.getNextAvailable();
        testServer = new TestServer(port);
        testClient = new TestClient(port);
        Thread serverThread = new Thread(testServer);
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(500); // wait startup
        Thread clientThread = new Thread(testClient);
        clientThread.setDaemon(true);
        clientThread.start();
        Thread.sleep(1000); // wait startup
    }

    @Test
    public void test() throws InterruptedException {
        assertTrue(testServer.isRunning());
    }

    @Test
    public void testLogin() throws InterruptedException {
        testClient.login("test", "test");
        Thread.sleep(3000);
    }
}
