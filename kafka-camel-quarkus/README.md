Camel Quarkus com Kafka
=========================

Para rodar esse exemplo, primeiro execute o comando abaixo para subir uma instância do Kafka:

```shell
docker-compose up
```

E depois execute o exemplo passando o endereço da instância do Kafka:


```
mvn -Dkafka.bootstrap.address=localhost:9092 clean compile quarkus:dev
```