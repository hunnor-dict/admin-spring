FROM maven:3.6-jdk-13 as maven
COPY . /opt/hunnor-dict/admin-spring
WORKDIR /opt/hunnor-dict/admin-spring
RUN mvn verify

FROM openjdk:13-jdk
RUN groupadd --system hunnor && \
	useradd --system --gid hunnor hunnor && \
	mkdir /hunnor && \
	chown --recursive hunnor:hunnor /hunnor
COPY --from=maven /opt/hunnor-dict/admin-spring/target/admin-spring-1.0.0.jar /hunnor
EXPOSE 8080
USER hunnor:hunnor
WORKDIR /hunnor
ENTRYPOINT ["java", "-jar", "/hunnor/admin-spring-1.0.0.jar"]
