# Netflix Pricing Service

## Scope & Architecture of the Pricing service
- Refer 

## Set-up instructions
### Pre-REQ
1. Java 8 or higher
1. Install Redis & Start
	-[Redis Install from terminal](  https://redis.io/topics/quickstart )
	```bash
	wget http://download.redis.io/redis-stable.tar.gz
	tar xvzf redis-stable.tar.gz
	cd redis-stable
	make	
	```
	- OR  `brew install redis` [Install Redis with Brew](https://medium.com/@petehouston/install-and-config-redis-on-mac-os-x-via-homebrew-eb8df9a4f298)
	
1. For pricing data store, MySQL is used.  H2 in-memory DB is packaged with the app use that for demo; If you prefer MySQL Install [mysql ] https://dev.mysql.com/downloads/mysql/ & set the env properties
```bash
SPRING_PROFILES_ACTIVE=prod 
JDBC_USER_NAME=<user:>
JDBC_PASSWORD=<pwd>
```

### running the application (java 8 or higher)
- java -jar pricing-service.jar

## Swagger Documentation for APIs

Swagger Links:

|End-point   |Link                                 |
|------------|-------------------------------------|
|Swagger UI  |http://localhost:8081/swagger-ui.html|
|Swagger JSON|http://localhost:8081/swagger        |

