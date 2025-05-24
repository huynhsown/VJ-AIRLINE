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
