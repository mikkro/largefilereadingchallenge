FROM amazoncorretto:17 as JAR_EXTRACT
WORKDIR /app
ARG JAR_FILE=*.jar
COPY ./build/libs/${JAR_FILE} ./app.jar
RUN java -Djarmode=layertools -jar ./app.jar extract


FROM amazoncorretto:17
WORKDIR /application
COPY --from=JAR_EXTRACT /app/dependencies ./
COPY --from=JAR_EXTRACT /app/spring-boot-loader ./
COPY --from=JAR_EXTRACT /app/snapshot-dependencies ./
COPY --from=JAR_EXTRACT /app/application ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]