# healthcare-project

1. code structure:
   - **/healthcare-project/frontend:** contains all frontend code for the web interface.
   - **/healthcare-project/backend:** contains backend code, this directory is further divided into several important subdirectories:
     - **/app:** handles the data models and smart contracts.
     - **/controller:** Hosts the gateway for interacting with the Fabric network, and a Spring Boot application that provides smart contract APIs for the frontend to interact with the backend.
     - **/network-settings:** Includes the configuration and setup files for an instance of the Hyperledger Fabric network.

2. how to run the application:
   - first run the backend, there is a **backend-guide.md** file in /healthcare-project/healthcare-project/backend to provide help.
       - run the network with **network-guide.md** in /backend/network-settings folder
       - run the Spring Boot application (i.e. the **Application.java** in /backend/controller/src/main/java/controller)
    - run the frontend follwing the **frontend-guide.md** in /healthcare-project/frontend
