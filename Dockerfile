FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Cài đặt netcat để test connection
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# Tạo script đợi MySQL
COPY <<EOF wait-for-it.sh
#!/bin/bash
host=\$1
port=\$2
timeout=\${3:-60}

echo "Đang chờ \$host:\$port sẵn sàng trong \$timeout giây..."

for i in \$(seq \$timeout); do
  if nc -z \$host \$port > /dev/null 2>&1; then
    echo "\$host:\$port đã sẵn sàng!"
    exec java -jar app.jar
  fi
  echo "Đang chờ \$host:\$port... (\$i/\$timeout)"
  sleep 1
done

echo "Timeout: \$host:\$port không sẵn sàng sau \$timeout giây"
exit 1
EOF

RUN chmod +x wait-for-it.sh

EXPOSE 8080

CMD ["./wait-for-it.sh", "mysql", "3306", "90"]