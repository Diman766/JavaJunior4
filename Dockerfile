FROM openjdk:17

COPY out/artifacts/task4_jar/task4.jar /tmp/task4.jar
WORKDIR /tmp
CMD ["java", "-jar", "/tmp/task4.jar"]