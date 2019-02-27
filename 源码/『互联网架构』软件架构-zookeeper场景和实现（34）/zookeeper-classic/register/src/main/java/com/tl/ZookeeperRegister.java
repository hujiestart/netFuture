package com.tl;



import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperRegister {

  ZooKeeper zooKeeper;
  public static final String ROOT="/tl";


    public  ZooKeeper getConnection(Watcher watcher) {
        try {
            zooKeeper=new ZooKeeper("192.168.69.101:2181,192.168.69.102:2181,192.168.69.103:2181",6000,watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  zooKeeper;

    }



}
