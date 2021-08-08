FROM openjdk:8

EXPOSE 8092

ADD target/activity-0.0.1-SNAPSHOT.jar activity-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/activity-0.0.1-SNAPSHOT.jar"]