# camelLogging
JMS logging to elastic using apache camel

A prototype to test the use of apache camel for logging BEAST and cs-studio jms messages into elasticsearch. 

### Setup ###
1. Install elastic and kibana (https://www.elastic.co/products).
2. Set up the indexes and their mapping using the elastic_setup.sh script
3. Build the camelLogging module using ```mvn clean install```, to start the logging run the ```java -jar camel-0.0.2-SNAPSHOT.jar```
