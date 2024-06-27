### Bring up the network
1. run network and create a channel with necessary certificate file:
    - ./network.sh up createChannel -ca


### Lifecycle of the contract

1. package chaincode:
    - CLI command: **peer lifecycle chaincode package basic.tar.gz --path ../../app/ --lang java --label basic_1.0**
    - in this case, all smart contracts in /dir/to/project/healthcare-project/backend/app will be packaged in the single chaincode named basic.

2. install chaincode:
    - CLI command: **peer lifecycle chaincode install basic.tar.gz**
    - make sure the chaincode is installed on all orgs. Switch between orgs by modifying org environment variables in ~/.bashrc file. Also remember to apply the change with command **source ~/.bashrc**

3. approve chaincode:
    - run **peer lifecycle chaincode queryinstalled** to get the id of the installed package, and export an environment variable named CC_PACKAGE_ID in ~/.bashrc
    - approve chaincode as org2: **peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name basic --version 1.0 --package-id $CC_PACKAGE_ID --sequence 1 --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem"**
    - switch the identity, approve chaincode as org1: **peer lifecycle chaincode approveformyorg -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name basic --version 1.0 --package-id $CC_PACKAGE_ID --sequence 1 --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem"**

4. commit chaincode:
    - CLI command: **peer lifecycle chaincode commit -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --channelID mychannel --name basic --version 1.0 --sequence 1 --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt"**

5. invoke chaincode (from default contract):
    - CLI command to invoke InitLedger function: **peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c '{"function":"InitLedger","Args":[]}'**
    - in order to check if the InitLedger successfully create new record: **peer chaincode query -C mychannel -n basic -c '{"Args":["GetAllRecords"]}'**
    - create a health record: **peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c '{"function":"CreateHealthRecord","Args":["1234","2024-06-27","1111","2222","headache","get cold","take rest for one week","3333"]}'**

    ## attention: 
    - the commands above only works on methods of the default contract(i.e. HelahRecord). If you want to call methods from other contracts, make sure to follow the format like contractName:methodName.
    - example: 
        - run InitLedger function of Prescription contract: **peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c '{"function":"PrescriptionContract:InitLedger","Args":[]}'**
        - run query all function of Prescription contract: **peer chaincode query -C mychannel -n basic -c '{"Args":["PrescriptionContract:GetAllPrescriptions"]}'**