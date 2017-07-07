## Birds From SaltSide
A rest service to play with birds.

### Problem
Implement a simple rest service for **posting/deleting/getting** birds as per these [specifications](https://gist.github.com/sebdah/265f4255cb302c80abd4).

### Solution
#### Tech-Stack
* **Java 8** for writing the application.
* **Spark** for the rest framework.
* **Guice** for dependency injection.
* **Morphia** for ORM.
* **Mongodb** as the database.
* **Groovy** for writing specs.
* **Spock** for the spec/test framework.
* **Fongo** for self contained spec tests.
* **Maven** for build and dependency management.

#### Application Design
* Most of the frameworks have been chosen because they are lightweight. It is easy to get a service started with these frameworks.
* I chose to implement the solution through a **DAO** pattern so as to abstract all the db interactions away from the controller.
 
#### Test Design
* I thoroughly enjoyed writing tests for this project because of **Spock**, a spec test framework in **Groovy**.
* In specs, **Fongo** is used instead of **Mongo** so as to keep the specs/tests self-contained. This would be helpful if the project was to go through a CI pipeline which may not have access to **MongoDB**.
* The application has two modules through which it can run, **TestModule** and **ProductionModule**. Based on the environment, appropriate module is used to launch the application.

### Requirements
* **Java 8**
* **Maven**

### How To

#### Run Tests?
```
mvn clean test
```

#### Build JAR?
```
mvn clean install
``` 
 
#### Run Application?
Make sure Mongodb is running and listening on port **27017**.
```
java -cp birds-1.0.jar com.saltside.birds.App
```   
Default listening port is **8080**.  

Once the application is running go to
```
http://localhost:8080/birds
```  
to test the application.

### Change Run Configurations(port, db name, etc)? 
The application uses **Properties** files in resources folder for configuration details like *port*, *db name*, etc. So, if you want it to run with some different configuration(s), go ahead and update the **Properties** files.   


### Future
* Implement application logging(non-existent right now).
* Some flexibility with endpoints, i.e. `GET /birds/` and `GET /birds` should both work. 
* Add more tests.