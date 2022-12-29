FROM openjdk:8
EXPOSE 8082
ADD target/stream-service-0.0.1-SNAPSHOT.jar stream-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dtmbd_key=d7f6cbb170a30076b40db652d7ea26fc", "-Ddb_url=remotemysql.com", "-Ddb_schema=2Z60GpjKNb", "-Ddb_username=2Z60GpjKNb", "-Ddb_password=bH30tIPtql", "-Dstreamsb.key=39564wo02jyqfoax4vvqb", "-jar", "stream-service-0.0.1-SNAPSHOT.jar"]

