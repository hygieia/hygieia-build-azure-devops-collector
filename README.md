# Project Title

Hygieia Azure DevOps Build Collector

## Getting Started

>Hygieia Azure DevOps Build Collector contains the logic that allows generating the calculation of the "Build" of the applications.

>Hygieia uses Spring Boot to package the API as an executable JAR file with dependencies.


### Prerequisites

>System prerrequisites:
>>- maven
>>- jdk8
>>- BD mongo
>>- SpringBoot sts


### Setup Instructions
>To configure the Hygieia Azure DevOps Build Collector, first fork and clone the Azure DevOps Build repo. Then, execute the following steps:

**Step 1: Run Maven Build**

>> `mvn install`

>The output file azure-devops-build-collector-1.0.0-SNAPSHOT.jar is generated in the hygieia-build-azure-devops-collector\target folder.


**Step 2: Run maven test**

>>`mvn test`


**Step 3: Set Parameters in Application Properties File**

>Set the configurable parameters in the application.properties file to connect to the Dashboard MongoDB database instance, including properties required by the Azure DevOps Build Collector.

>To configure parameters for the Azure DevOps  Build Collector, refer to the sample application.properties file.

>For information about sourcing the application properties file, refer to the [Spring Boot Documentation.](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files)


**Step 3: Deploy the Executable File**

>To deploy the azure-devops-build-collector-1.0.0-SNAPSHOT.jar file, change directory to hygieia-build-azure-devops-collector\target, and then execute the following from the command prompt:

>>`java -jar azure-devops-build-collector-1.0.0-SNAPSHOT.jar --spring.config.name=azure-devops-build-collector --spring.config.location=[path to application.properties file]`


**Sample Application Properties File**

>The sample application.properties file lists parameters with sample values to configure the Azure DevOps Build Collector. Set the parameters based on your environment setup.


```
# Database Name
dbname=dashboarddb
# Database HostName - default is localhost
dbhost=localhost
# Database Port - default is 27017
dbport=27017
# Database Username - default is blank
dbusername=dashboarduser
# Database Password - default is blank
dbpassword=dbpassword
# Logging File location
logging.file=./logs/azure-devops-build.log
#Collector schedule (required)
azure-build.cron=0 0/2 * * * *
#host (optional, defaults to "dev.azure.com")
azure-build.host=dev.azure.com
#accountName(required)
azure-build.account=accountName
#Project
azure-build.projectId=projectId
#protocol https
azure-build.protocol=https
#port (optional, defaults to protocol default port)
azure-build.port=
#API Token (required, access token can be retrieved through Azure DevOps, collector will have the permissions of the user associated to the token)
azure-build.apiTokens=
#vsts API Version (optional, defaults to current version of 5.0)
azure-build.apiVersion=5.0
#Azure DevOps configurations
azure-build.configName=
```