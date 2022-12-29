FROM openjdk:8
EXPOSE 8082
ADD target/stream-service-0.0.1-SNAPSHOT.jar stream-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dtmbd_key=d7f6cbb170a30076b40db652d7ea26fc","-Ddb_url=bd7sfrf0eh5ljnrywadq-mysql.services.clever-cloud.com","-Ddb_schema=bd7sfrf0eh5ljnrywadq","-Ddb_username=ubniahfyoffjxwe1","-Ddb_password=6wr7MknyyvCZpImzbKZK","-Dstreamsb.key=39564wo02jyqfoax4vvqb", "-jar", "stream-service-0.0.1-SNAPSHOT.jar"]

