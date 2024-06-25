### instructions on how to set up backend (on vscode)
1. install following extensions:
    - Extension Pack for Java
    - Gradle for Java

2. download jdk and set up environment variable:
    - download jdk 11 (in my case I download it from oracle)
    - search for settings.json in the top search bar of vscode
    - vscode checks jdk version by multiple env variables, make sure to modify all of them:
        - "java.jdt.ls.java.home": "/path/to/jdk"
        - "jdk.jdkhome": "/path/to/jdk"
        - "java.configuration.runtimes": {
            "name": "JavaSE-11",
            "path": "/path/to/jdk",
        }
3. build backend application:
    - open the project from root directory, containing both front and backend
    - in ideal case, gradle icon is activated at the left side bar. Navigaate to gradle tab and click on the **"run a gradle build"** at the top. Enter **"build"** in the top search bar and the gradle will build the project

4. start the network:
    - follow instructions in /backend/network-settings/network-guide.md

5. run backend application:
    - navigate to /backend/controller/src/main/java/controller folder, and run application.java