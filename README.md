# Dual Kafka Cluster Project

This project demonstrates an end-to-end data processing pipeline spanning across two separate Apache Kafka clusters (a ZooKeeper-based cluster and a modern KRaft-based cluster) connected via microservices with SSL encryption, and persisting results into MongoDB.

## Architecture Overview

1. **ZooKeeper Kafka Cluster (`kafka-zk`)**: Ingests incoming orders on the `orders` topic (supports PLAINTEXT on `9092` and SSL on `9093`).
2. **ZK Consumer Service (`zk-consumer-service`)**: Consumes orders from the ZooKeeper cluster and forwards enriched order events to the KRaft cluster over SSL (`processed-orders` topic).
3. **KRaft Kafka Cluster (`kafka-kraft`)**: Modern KRaft-mode Kafka cluster operating without ZooKeeper (PLAINTEXT on `9192`, SSL on `9193`).
4. **KRaft Processor Service (`kraft-processor-service`)**: Consumes processed orders from the KRaft cluster and stores them into MongoDB.
5. **MongoDB**: Persistent database storing finalized order records.
6. **Kafbat UI**: Web-based management and monitoring interface for both Kafka clusters.

## Prerequisites

- **Java 21** or later
- **Docker & Docker Compose**
- **Gradle** (or Gradle wrapper provided in service directories)

## Services & Port Mappings

| Service | Container / Component | Port(s) | Description |
| :--- | :--- | :--- | :--- |
| **ZooKeeper** | `zookeeper` | `2181` | ZooKeeper coordination for `kafka-zk` |
| **Kafka (ZK-mode)** | `kafka-zk` | `9092` (PLAINTEXT), `9093` (SSL) | Kafka broker running with ZooKeeper |
| **Kafka (KRaft-mode)** | `kafka-kraft` | `9192` (PLAINTEXT), `9193` (SSL) | Kafka broker running in KRaft mode |
| **Kafbat UI** | `kafbat-ui` | `8080` | Web UI for cluster management |
| **MongoDB** | `mongodb` | `27017` | Document database for processed orders |
| **ZK Consumer Service** | `zk-consumer-service` | `8081` | Spring Boot service consuming from ZK Kafka |
| **KRaft Processor Service** | `kraft-processor-service` | `8082` | Spring Boot service processing KRaft messages |

## Kafbat UI

Access the Kafbat UI at: [http://localhost:8080](http://localhost:8080) to inspect topics, brokers, consumer groups, and messages across both Kafka clusters (`kafka-zk-cluster` and `kafka-kraft-cluster`).

## Quick Start

### 1. Generate SSL Certificates
Generate the required CA certs, keystores, and truststores:
```bash
chmod +x generate-certs.sh && ./generate-certs.sh
```

### 2. Start Infrastructure
Start ZooKeeper, Kafka brokers, Kafbat UI, and MongoDB using Docker Compose:
```bash
docker compose up -d
```

### 3. Build Services
Build the application artifacts:
```bash
cd kraft-processor-service && ./gradlew bootJar && cd ..
cd zk-consumer-service && ./gradlew bootJar && cd ..
```

### 4. Run Service 2 First (KRaft Processor Service)
Start the KRaft consumer service that writes to MongoDB:
```bash
cd kraft-processor-service && ./gradlew bootRun
```

### 5. Run Service 1 (ZK Consumer Service)
In another terminal, start the ZooKeeper consumer service:
```bash
cd zk-consumer-service && ./gradlew bootRun
```

### 6. Send a Test Message
Send a test message to the ZK Kafka `orders` topic:
```bash
docker exec -i kafka-zk /opt/kafka/bin/kafka-console-producer.sh \
  --bootstrap-server localhost:9092 \
  --topic orders
```

### 7. Check MongoDB
Verify that the processed order has been stored in MongoDB:
```bash
docker exec -it mongodb mongosh --eval "use orders_db; db.processed_orders.find().pretty();"
```
