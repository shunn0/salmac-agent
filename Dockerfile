FROM maven:3.8.2-jdk-8
WORKDIR .
COPY target/target-0.0.1-SNAPSHOT.jar .
#COPY . .
#RUN mvn clean install
CMD java -jar target-0.0.1-SNAPSHOT.jar
#CMD mvn spring-boot:run