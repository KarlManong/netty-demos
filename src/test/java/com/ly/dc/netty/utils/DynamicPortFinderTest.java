package com.ly.dc.netty.utils;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

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
public class DynamicPortFinderTest {

    @Test
    public void testAvailable() {
        for (int i = 0; i < 1000; i++) {
            int port = DynamicPortFinder.getNextAvailable();
            assertThat(port, Matchers.both(Matchers.lessThan(DynamicPortFinder.MAX_PORT_NUMBER)).and(Matchers.greaterThan(DynamicPortFinder.MIN_PORT_NUMBER)));
        }

    }

}