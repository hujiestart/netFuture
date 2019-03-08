package com.idig8.api.curator.lock;

/*
 * idi8.com
 * 公众号：编程坑太多
 */

import com.idig8.ZookeeperUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorInterProcessMutex {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 5);//重试策略
    //工厂创建连接
    CuratorFramework   cf= CuratorFrameworkFactory.builder()
            .connectString(ZookeeperUtil.connectString)
            .sessionTimeoutMs(ZookeeperUtil.sessionTimeout)
            .retryPolicy(retryPolicy)
            .build();

     InterProcessMutex lock;
    public CuratorInterProcessMutex(String path) {
        lock=new InterProcessMutex(cf, path);
        cf.start();//链接
    }

    /***
     * 获取资源
     * @see org.apache.curator.framework.recipes.locks.LockInternals
     * @throws Exception
     */
    public void acquire() throws Exception {
        lock.acquire();
    }

    /****
     * 释放资源
     * @throws Exception
     */
    public void release() throws Exception {
        lock.release();
    }


}
