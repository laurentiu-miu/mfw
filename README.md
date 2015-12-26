# mfw
#--Spring--#
#Run application for development
mvn spring-boot:run

#--Docker--#

#Build docker image cmd:
$ mvn package docker:build

#Push image to repository cmd:
$ docker push laurentiumiu/web-framework

#Start docker continer # my ip to access is http://192.168.99.100:8080
docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t laurentiumiu/web-framework
#inspect continer ip
docker inspect $(docker ps -q) | grep IPA
