FROM openjdk:11

COPY . /javaecs

WORKDIR /javaecs

RUN javac /javaecs

EXPOSE 8080

CMD ["java", "Main"]