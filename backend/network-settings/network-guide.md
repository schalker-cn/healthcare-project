### env variables 

export FABRIC_ROOT="/mnt/c/Users/mazen/Programming/TUM/healthcare-project"
export FABRIC_HOME="/mnt/c/Users/mazen/Programming/TUM/healthcare-project/backend/network-settings/test-network"
export PATH="$PATH:$BREW_HOME:/usr/local/go/bin:$FABRIC_HOME/../bin:$JAVA_HOME/bin"
export FABRIC_CFG_PATH="$FABRIC_HOME/../config/"
export CORE_PEER_TLS_ENABLED=true

# peer0
export CORE_PEER_LOCALMSPID=Org1MSP
export CORE_PEER_TLS_ROOTCERT_FILE=${FABRIC_HOME}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
export CORE_PEER_MSPCONFIGPATH=${FABRIC_HOME}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
export CORE_PEER_ADDRESS=localhost:7051
export CC_PACKAGE_ID=basic_1.0:ed1f500cbf57b9f00707560e4782646364b913053b0be99c0db7abe09c995995

# peer1
export CORE_PEER_LOCALMSPID=Org2MSP
export CORE_PEER_TLS_ROOTCERT_FILE=${FABRIC_HOME}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt
export CORE_PEER_MSPCONFIGPATH=${FABRIC_HOME}/organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp
export CORE_PEER_ADDRESS=localhost:9051
export CC_PACKAGE_ID=basic_1.0:ed1f500cbf57b9f00707560e4782646364b913053b0be99c0db7abe09c995995

### Bring up the network
1. run network and create a channel with necessary certificate file:

    ```
    ./network.sh up createChannel -ca
    ```

### Lifecycle of the contract

1. package chaincode:

    - CLI command:

    ```
    peer lifecycle chaincode package basic.tar.gz --path ../../app/ --lang java --label basic_1.0
    ```
      
    - in this case, all smart contracts in /dir/to/project/healthcare-project/backend/app will be packaged in the single chaincode named basic.

1. install chaincode:
    - CLI command:


    ```
      peer lifecycle chaincode install basic.tar.gz
    ```
    
    - make sure the chaincode is installed on all orgs. Switch between orgs by modifying org environment variables in ~/.bashrc file. Also remember to apply the change with command **source ~/.bashrc**

2. approve chaincode:

    - run
      
    ```
      peer lifecycle chaincode queryinstalled
    ```

    to get the id of the installed package, and export an environment variable named CC_PACKAGE_ID in ~/.bashrc
    - approve chaincode as org2:
      
    ```
    peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name basic --version 1.0 --package-id $CC_PACKAGE_ID --sequence 1 --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem"
    ```
    
    - switch the identity, approve chaincode as org1:
    
    ```
    peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name basic --version 1.0 --package-id $CC_PACKAGE_ID --sequence 1 --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem"
    ```

4. commit chaincode:

    - CLI command:
    
    ```
    peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name basic --version 1.0 --sequence 1 --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt"
    ```

5. invoke chaincode (from default contract):

    - CLI command to invoke InitLedger function:
    
    ```
    peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c '{"function":"InitLedger","Args":[]}'
    ```
    
    - in order to check if the InitLedger successfully create new record:
    
    ```
    peer chaincode query -C mychannel -n basic -c '{"Args":["GetAllRecords"]}'
    ```

    
    - create a health record:
    
    ```
    peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c '{"function":"CreateHealthRecord","Args":["1234","2024-06-27","1111","2222","headache","get cold","take rest for one week","3333"]}'
    ```

    ## attention: 

    - the commands above only works on methods of the default contract(i.e. HelahRecord). If you want to call methods from other contracts, make sure to follow the format like contractName:methodName.

    - example: 
        - run InitLedger function of Prescription contract:
        
        ```
        - peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c '{"function":"PrescriptionContract:InitLedger","Args":[]}'
        ```
  
        
        - run query all function of Prescription contract:
        
        ```
        - peer chaincode query -C mychannel -n basic -c '{"Args":["PrescriptionContract:GetAllPrescriptions"]}'
        ```
