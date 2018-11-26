#!/bin/bash
cur_dir=`pwd`
docker stop nexus
docker rm nexus
docker run -d -p 8081:8081 --name nexus -v  ${cur_dir}/data:/sonatype-work sonatype/nexus