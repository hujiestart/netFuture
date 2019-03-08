package com.idig8.api.zkclient.crud;

/*
 * idi8.com
 * 公众号：编程坑太多
 */

import com.idig8.ZookeeperUtil;
import org.I0Itec.zkclient.ZkClient;

public class ZkClientCrud {
    ZkClient zkClient;

    public static void main(String[] args) {
        ZkClient zkClient=new ZkClient(ZookeeperUtil.connectString,ZookeeperUtil.sessionTimeout);
        //面试说 可以递归创建节点 但是都是null空节点
        zkClient.createPersistent("/abc/ccc/ddd",true);

       // zkClient.createPersistent("/abc/ccc/ddd",true);
    }



}
