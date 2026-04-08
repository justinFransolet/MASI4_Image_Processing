# Initialization

## Initialization of the project into IntelliJ

1. Clone the project from GitHub
2. Open the project with IntelliJ IDEA
3. Configure the project SDK
    - Go to `File` > `Project Structure` > `Project`
    - Set the Project SDK to the Java version 1.8
    - Set the Project language level to 8
4. Configure the project dependencies
    - Go to `File` > `Project Structure` > `Modules`
    - Select the module and go to the `Dependencies` tab
    - Click on the `+` icon and select `JAR`
    - Select the libraries you added in the previous step and click `OK`
5. Build the project
    - Go to `Build` > `Build Project` to compile the project and ensure there are no errors
6. Run the project
    - Go to `Run` > `Edit Configurations`
    - Click on the `+` icon and select `Application`
    - Set the name of the configuration (e.g., "Run App")
    - Set the main class to `isilimageprocessing.IsilImageProcessing`
    - Click `OK` to save the configuration
    - Go to `Run` > `Run 'Run App'` to execute the project