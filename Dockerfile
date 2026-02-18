FROM maven:3.9-eclipse-temurin-17 as maven
COPY . /opt/hunnor-dict/admin-spring
WORKDIR /opt/hunnor-dict/admin-spring
RUN mvn verify

FROM eclipse-temurin:17
RUN groupadd --system hunnor && \
	useradd --system --gid hunnor hunnor && \
	mkdir /hunnor && \
	chown --recursive hunnor:hunnor /hunnor
COPY --from=maven /opt/hunnor-dict/admin-spring/target/admin-spring-1.0.0.jar /hunnor
EXPOSE 8080
USER hunnor:hunnor
WORKDIR /hunnor
ENTRYPOINT ["java", "-jar", "/hunnor/admin-spring-1.0.0.jar"]
