package com.example.k8sserver;

import cn.hutool.system.HostInfo;
import cn.hutool.system.JvmInfo;
import cn.hutool.system.JvmSpecInfo;
import cn.hutool.system.SystemUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/create-memory")
    public String createMemory(@RequestParam(defaultValue = "1000") int byteSize) throws InterruptedException {
        byte[] b = new byte[byteSize];
        System.out.println(b.length);
        TimeUnit.SECONDS.sleep(30);
        JvmInfo jvmInfo = SystemUtil.getJvmInfo();
        JvmSpecInfo jvmSpecInfo = SystemUtil.getJvmSpecInfo();
        long freeMemory = SystemUtil.getFreeMemory() / 1024 / 1024;
        long totalMemory = SystemUtil.getTotalMemory() / 1024 / 1024;
        long maxMemory = SystemUtil.getMaxMemory() / 1024 / 1024;
        System.out.println(totalMemory);
        System.out.println(maxMemory);
        System.out.println(freeMemory);
        return "ok ";
    }
}
