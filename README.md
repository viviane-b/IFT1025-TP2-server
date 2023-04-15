# IFT1025-TP2-server

## FAQ

### What are you trying to do?/What is the goal of the project?
This is a project for the course IFT1025 at Université de Montréal.
We are trying to create a server and two clients for a course sign-up system. One client is a command-line interface (CLI) and the other is a graphical user interface (GUI). 
The server is responsible for storing and managing the data of the course sign-up system. 
The CLI client is responsible for managing the data of the course sign-up system.

### Project requirements
- Implement a MVC architecture for GUI client. 
- Implement Socket programming for both clients and server.

### Details of the project
- check the project description: ```Detail du projet.pdf```

### Location of JAR file
client_fx.jar: ```/out/artifacts/ClientGUI_jar3/client_fx.jar```
client_simple.jar: ```/out/artifacts/ClientCMD_jar2/client_simple.jar```
server.jar: ```/out/artifacts/Server_jar/Server.jar```

### How to run the JAR files
1. Open a terminal and navigate to the root directory of the project. This step is necessary because this is the relative path for the project, including resources and data files. If this step is skipped, the program will not be able to find the resources and data files.
2. Run the .jar file by typing the following command: ```java -jar <relative path to Jar file from the root directory>```

### How to run client_fx.jar
1. Add JavaFX library to the project by following this guide: 
```https://medium.com/codex/solved-error-javafx-runtime-components-are-missing-and-are-required-to-run-this-application-ec4779eb796d```

2. Additionally, you need to add the following VM options when running client_fx.jar: 
```--module-path "<Path to lib folder of JavaFX library>" --add-modules javafx.fxml,javafx.controls,javafx.graphics```

### Link to Github repository
```https://github.com/viviane-b/IFT1025-TP2-server```

### Demo video
- demo_client_simple: ```https://udemontreal-my.sharepoint.com/:v:/g/personal/hoang_quan_tran_1_umontreal_ca/ETALLOvNOspNl5RfuMKbifcBKbNXFjwlPl458aC1dVDw2Q?e=9jAaiQ```
- demo_client_fx: ```https://udemontreal-my.sharepoint.com/:v:/g/personal/hoang_quan_tran_1_umontreal_ca/ETALLOvNOspNl5RfuMKbifcBKbNXFjwlPl458aC1dVDw2Q?e=9jAaiQ```

### Our team members: 
- Viviane Binet: 20244728
- Hoang Quan Tran: 20249088
- Workloads are well distributed among team members. Commits come from a single Github account, but team members worked on the project together.