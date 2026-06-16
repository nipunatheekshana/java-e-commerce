# ---------- Build stage: compile the Java sources into a runnable jar ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY src ./src
RUN mkdir -p out \
    && find src/main/java -name "*.java" > sources.txt \
    && javac -d out @sources.txt \
    && jar --create --file ecommerce.jar --main-class com.ecommerce.Main -C out .

# ---------- Run stage: small JRE image that only carries the jar ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/ecommerce.jar ./ecommerce.jar

# Console app reads from stdin, so run with:  docker run -it <image>
ENTRYPOINT ["java", "-jar", "ecommerce.jar"]
