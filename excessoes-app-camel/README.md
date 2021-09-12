Gerenciamento de Erros Com Camel
=========================

Para rodar esse exemplo, primeiro execute:

```shell
mvn clean package
```

E depois execute o exemplo com um dos gerenciadores de erro disponíveis:


Armadilha (usando onException)
```
mvn -Dtipo=armadilha camel:run
```

Usando a DSL (Usando doTry/doCatch/doFinally)
```
mvn -Dtipo=dsl camel:run
```

Usando um manipulador de erros (Error Handler)
```
mvn -Dtipo=errorhandler camel:run
```

Sem gerenciamento de erros
```
mvn -Dtipo=desligado camel:run
```