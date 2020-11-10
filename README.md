# bank-account-kata 

# Requierement

https://gist.github.com/abachar/d20bdcd07dac589feef8ef21b487648c

# Assumption and scope

This implementation is based on following assumption:
 - The bank account is created by another service and here we are only using accountID
 
 - Only 2 operation are supported 
     - DEPOSIT to increase account balance
     - WITHDRAWAL to decrease account balance
     
 - There is no validation performed 
     - account id is not ckecked (could be done in next iteration)
     - No overdraft limit (for simplicity and could be done as next step)
     
 - Test are covering basis use cases for simplicity 
     
# Description of implementation
This implementation is handling all user operations as Event and store them.
  - ElasticSearch is used as event storage (for simplicity i'm using a cluster with single node running on docker)
      Quick setup:
      - pull docker image
      ```
      docker pull docker.elastic.co/elasticsearch/elasticsearch:7.9.3
      ```
      - run container
      ```
      docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" docker.elastic.co/elasticsearch/elasticsearch:7.9.3
      ```
      - validate that ES container is up
      
      ```
         curl -X GET "localhost:9200/_cat/nodes?v&pretty"
         
      ```

With this approach, we can compute the account balance on demand by pulling all events matching with some criteria (like date) and aggragete them based on the type of event (DEPOSIT or WITHDRAWAL),

# Constraint, limitation
- Eventual consistency of the system due to distributed nature of storage
