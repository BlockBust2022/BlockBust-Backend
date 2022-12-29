FROM openjdk:8
EXPOSE 8082
ADD target/stream-service-0.0.1-SNAPSHOT.jar stream-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","tmbd_key=d7f6cbb170a30076b40db652d7ea26fc","db_url=bd7sfrf0eh5ljnrywadq-mysql.services.clever-cloud.com","db_schema=bd7sfrf0eh5ljnrywadq","db_username=ubniahfyoffjxwe1","db_password=6wr7MknyyvCZpImzbKZK","streamsb.key=39564wo02jyqfoax4vvqb", "-jar", "stream-service-0.0.1-SNAPSHOT.jar"]

