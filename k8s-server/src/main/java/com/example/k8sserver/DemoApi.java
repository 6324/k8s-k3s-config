package com.example.k8sserver;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.SystemUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class DemoApi {

    @GetMapping("/ping")
    public String ping() {
        HostInfo hostInfo = SystemUtil.getHostInfo();
        String name = hostInfo.getName();
        String address = hostInfo.getAddress();
        return "ok|hostname: [" + name + "]  address: [" + address + "] ";
    }

    @GetMapping("/up-memory")
    public String upMemory(@RequestParam(defaultValue = "1") int tag) throws InterruptedException {
        System.out.println("start >" + SystemUtil.getFreeMemory() / 1024.0 / 1024.0);
        byte[] b = new byte[tag * 1024 * 1024];
        System.out.println("end   >" + SystemUtil.getFreeMemory() / 1024.0 / 1024.0);
        TimeUnit.SECONDS.sleep(5);
        return "ok";
    }


    static volatile boolean startWhile = true;
    static List<Thread> threadList = new ArrayList<>();

    @GetMapping("/up-cpu")
    public String upCpu(@RequestParam(defaultValue = "1") int tag) {
        startWhile = true;
        for (int i = 0; i < tag; i++) {
            Thread thread = new Thread(() -> {
                while (startWhile) {
//                    double v = rand_pi(RandomUtil.randomInt(10, 100));
//                    System.out.println(Thread.currentThread().getName() + " " + v);
                }
            }, RandomUtil.randomString(5));
            threadList.add(thread);
            thread.start();
        }
        return "ok";
    }

    @GetMapping("/down-cpu")
    public String downCpu() {
        startWhile = false;
        Iterator<Thread> iterator = threadList.iterator();
        while (iterator.hasNext()){
            Thread thread = iterator.next();
            thread.interrupt();
            System.out.println(thread.getName() + " 停止");
            iterator.remove();
        }
        return "ok";
    }

    public static double rand_pi(int n) {
        int numInCircle = 0;
        double x, y;
        double pi;
        for (int i = 0; i < n; i++) {
            x = Math.random();
            y = Math.random();
            if (x * x + y * y < 1) {
                numInCircle++;
            }
        }
        pi = (4.0 * numInCircle) / n;
        return pi;
    }
}