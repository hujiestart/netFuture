cur_dir=`pwd`
docker stop gitlab-postgresql
docker rm gitlab-postgresql
docker stop gitlab-redis
docker rm gitlab-redis
docker stop gitlab
docker rm gitlab
docker run --name gitlab-postgresql -d \
    --env 'DB_NAME=gitlabhq_production' \
    --env 'DB_USER=gitlab' --env 'DB_PASS=password' \
    --env 'DB_EXTENSION=pg_trgm' \
    --volume ${cur_dir}/postgresql:/var/lib/postgresql \
    sameersbn/postgresql:10
docker run --name gitlab-redis -d \
    --volume ${cur_dir}/redis:/var/lib/redis \
    sameersbn/redis:4.0.9-1
docker run --name gitlab -d \
    --link gitlab-postgresql:postgresql --link gitlab-redis:redisio \
    --publish 10022:22 --publish 10080:80 \
    --env 'GITLAB_PORT=10080' --env 'GITLAB_SSH_PORT=10022' \
    --env 'GITLAB_ROOT_PASSWORD=123456789qwe' \
    --env 'GITLAB_SECRETS_DB_KEY_BASE=long-and-random-alpha-numeric-string' \
    --env 'GITLAB_SECRETS_SECRET_KEY_BASE=long-and-random-alpha-numeric-string' \
    --env 'GITLAB_SECRETS_OTP_KEY_BASE=long-and-random-alpha-numeric-string' \
    --volume ${cur_dir}/gitlab:/home/git/data \
    sameersbn/gitlab