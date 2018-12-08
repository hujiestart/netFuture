#!/bin/bash
echo "创建网络"
docker network create  --subnet=172.18.0.0/24 net1

echo "创建5个docker卷"
docker volume create v1
docker volume create v2
docker volume create v3
docker volume create v4
docker volume create v5

echo "创建节点 node1"
docker run -d -p 3306:3306  --net=net1 --name=node1 \
        -e CLUSTER_NAME=PXC \
        -e MYSQL_ROOT_PASSWORD=a123456 \
        -e XTRABACKUP_PASSWORD=a123456 \
        -v v1:/var/lib/mysql \
        --privileged \
        --ip 172.18.0.2 \
        percona/percona-xtradb-cluster
sleep 1m 
echo "创建节点 node2"
docker run -d -p 3307:3306  --net=net1 --name=node2 \
        -e CLUSTER_NAME=PXC \
        -e MYSQL_ROOT_PASSWORD=a123456 \
        -e XTRABACKUP_PASSWORD=a123456 \
        -e CLUSTER_JOIN=node1 \
        -v v2:/var/lib/mysql \
        --privileged \
        --ip 172.18.0.3 \
        percona/percona-xtradb-cluster
sleep 1m 
echo "创建节点 node3"
docker run -d -p 3308:3306  --net=net1 --name=node3 \
        -e CLUSTER_NAME=PXC \
        -e MYSQL_ROOT_PASSWORD=a123456 \
        -e XTRABACKUP_PASSWORD=a123456 \
        -e CLUSTER_JOIN=node1 \
        -v v3:/var/lib/mysql \
        --privileged \
        --ip 172.18.0.4 \
        percona/percona-xtradb-cluster
sleep 1m 
echo "创建节点 node4"
docker run -d -p 3309:3306  --net=net1 --name=node4 \
        -e CLUSTER_NAME=PXC \
        -e MYSQL_ROOT_PASSWORD=a123456 \
        -e XTRABACKUP_PASSWORD=a123456 \
        -e CLUSTER_JOIN=node1 \
        -v v4:/var/lib/mysql \
        --privileged \
        --ip 172.18.0.5 \
        percona/percona-xtradb-cluster
sleep 1m 
echo "创建节点 node5"
docker run -d -p 3310:3306  --net=net1 --name=node5 \
        -e CLUSTER_NAME=PXC \
        -e MYSQL_ROOT_PASSWORD=a123456 \
        -e XTRABACKUP_PASSWORD=a123456 \
        -e CLUSTER_JOIN=node1 \
        -v v5:/var/lib/mysql \
        --privileged \
        --ip 172.18.0.6 \
        percona/percona-xtradb-cluster