#!/bin/bash
cur_dir=`pwd`
docker stop tomcat
docker rm tomcat
docker run -d --name tomcat -p 8080:8080 -v ${cur_dir}/tomcat-persistence:/bitnami bitnami/tomcat:latest