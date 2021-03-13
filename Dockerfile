FROM openjdk:15-jdk-alpine as build

ENV MAVEN_VERSION 3.6.3
ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

RUN wget http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz && \
 tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz && \
 rm apache-maven-$MAVEN_VERSION-bin.tar.gz && \
 mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

WORKDIR /workspace/app

COPY pom.xml .
COPY src src
RUN mvn install -DskipTests -Dgithook.plugin.skip=true
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:15-jdk-alpine
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","com.lyit.calculator.CalculatorApplication"]