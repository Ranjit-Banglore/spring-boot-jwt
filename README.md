## Project description:
> Spring boot application that publishes Bank Account Service APIs 

## Reponse from bankaccounts endpoint -
```<path-to-context>/bank/api/{customer_id}/bankaccounts```

```json
[
    {
        "id": 2,
        "iban": "DE96514388914485185983",
        "type": "PRIVATE_LOAN",
        "balance": 83136352.05,
        "active": true,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8081/bank/api/1/bankaccounts/2"
            },
            {
                "rel": "transactions",
                "href": "http://localhost:8081/bank/api/DE96514388914485185983/transactions"
            }
        ]
    },
    {
        "id": 1,
        "iban": "DE59578454054765551413",
        "type": "CHECKINGS",
        "balance": 5760389749.04,
        "active": true,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8081/bank/api/1/bankaccounts/1"
            },
            {
                "rel": "transactions",
                "href": "http://localhost:8081/bank/api/DE59578454054765551413/transactions"
            }
        ]
    },
    {
        "id": 3,
        "iban": "DE21682132570987500492",
        "type": "SAVINGS",
        "balance": 578041573.45,
        "active": true,
        "links": [
            {
                "rel": "self",
                "href": "http://localhost:8081/bank/api/1/bankaccounts/3"
            },
            {
                "rel": "transactions",
                "href": "http://localhost:8081/bank/api/DE21682132570987500492/transactions"
            }
        ]
    }
]
```

## Prerequisites : A running postgres database
> update connection urls based on running postgres 

## Download jasypt-1.9.3.jar and use below command to decrypt database password key
```$java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="<db-password>" password=<encyption-key> algorithm=PBEWithMD5AndDES```

## To run application locally:
```$java -jar <path-to-jar>/bank-account-service-0.0.1-SNAPSHOT.jar --jasypt.encryptor.password=<jasypt-key> --server.port=<port>```

## create a basic auth user. 
```BcryptTest.java is provided in project to generate encrypted password for a user```

## Generate jwt token :
```<path-to-context>/bank/token```

## APIs path : 
```<path-to-context>/bank/api/*```

## Swagger file location: resources/swagger/swagger.json
```http://localhost:8081/bank/swagger-ui/#/bank-account-controller```

## In production secret can be provided as environment variable 
```--export CATALINA_OPTS=”-Djasypt.encryptor.password=MY_SECRET”```

## Tests are provided to check functionality of the application 
```
BankAccountServiceTest.java
```
```
BankAccountControllerTest.java
```
```
BankAccountServiceIntegrationTest.java
```
