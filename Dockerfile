FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Cài đặt MySQL client để test connection
RUN apt-get update && apt-get install -y mysql-client && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# Tạo script wait-for-mysql
RUN echo '#!/bin/bash\n\
echo "Đang chờ MySQL sẵn sàng..."\n\
until mysqladmin ping -h"$1" -P"$2" -u"$3" -p"$4" --silent; do\n\
  echo "MySQL chưa sẵn sàng - đang chờ..."\n\
  sleep 2\n\
done\n\
echo "MySQL đã sẵn sàng!"\n\
exec java -jar app.jar' > wait-for-mysql.sh

RUN chmod +x wait-for-mysql.sh

EXPOSE 8080

# Sử dụng script để đợi MySQL
CMD ["./wait-for-mysql.sh", "mysql", "3306", "user", "password"]