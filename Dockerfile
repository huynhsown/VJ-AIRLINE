### 1. Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

### 2. Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Cài netcat và ping để debug kết nối
RUN apt-get update && \
    apt-get install -y netcat-openbsd iputils-ping && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# Đặt script chờ MySQL
COPY <<EOF wait-for-it.sh
#!/bin/bash
host="$1"
port="$2"
timeout=${3:-60}

echo "[wait-for-it] Đang chờ $host:$port sẵn sàng trong $timeout giây..."
for i in $(seq $timeout); do
  if nc -z "$host" "$port"; then
    echo "[wait-for-it] $host:$port đã sẵn sàng!"
    exec java -jar app.jar
  fi
  sleep 1
done

echo "[wait-for-it] Timeout: $host:$port không sẵn sàng sau $timeout giây"
exit 1
EOF

RUN chmod +x wait-for-it.sh

EXPOSE 8080

CMD ["./wait-for-it.sh", "mysql", "3306", "90"]