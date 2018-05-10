FROM java:8-jre

ENV SERVER_PORT 8080

ADD build/libs/pricing-service*.jar /home/pricing-service.jar
CMD ["java", "-jar", "/home/pricing-service.jar"]
