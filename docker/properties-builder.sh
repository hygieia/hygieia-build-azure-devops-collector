#!/bin/sh

cat > $PROP_FILE <<EOF
#Database Name
dbname=${HYGIEIA_DATABASE:-dashboarddb}

#Database HostName - default is localhost
dbhost=${MONGODB_HOST:-localhost}

#Database Mongo Replicaset - default is false
dbreplicaset=${MONGODB_REPLICASET:-false}

#Database Mongo Replicaset HOst - default is localhost:27017
dbhostport=${MONGODB_REPLICASET_HOST:-localhost:27017}

#Database Port - default is 27017
dbport=${MONGODB_PORT:-27017}

#Database Username - default is blank
dbusername=${HYGIEIA_MONGODB_USERNAME:-dashboarduser}

#Database Password - default is blank
dbpassword=${HYGIEIA_MONGODB_PASSWORD:-dbpassword}

#Collector schedule (required)
azure-build.cron=${AZURE_DEVOPS_BUILD_CRON:-0 0/5 * * * *}

#Logging File location
logging.file=./logs/azure-devops-build.log

#Azure DevOps host (optional, defaults to "dev.azure.com")
azure-build.host=${AZURE_DEVOPS_HOST:-dev.azure.com}

#Azure DevOps accountName(required)
azure-build.accountId=${AZURE_DEVOPS_ACCOUNTID:-}

#Azure DevOps Project
azure-build.projectId=${AZURE_DEVOPS_PROJECTID:-}

#Azure DevOps protocol (optional, defaults to 'https')
azure-build.protocol=${AZURE_DEVOPS_PROTOCOL:-https}

#Azure DevOps port (optional, defaults to protocol default port)
azure-build.port=${AZURE_DEVOPS_PORT:-}

#Azure DevOps API Version (optional, defaults to current version of 5.0)
azure-build.apiVersion=${AZURE_DEVOPS_API_VERSION:-5.0}

#Azure DevOps API Tokens (required, access token can be retrieved through Azure DevOps, collector will have the permissions of the user associated to the token, multipes token separated by commas)
azure-build.apiTokens=${AZURE_DEVOPS_API_TOKENS:-}

#Azure DevOps configurations
azure-build.configName=${CONFIG_NAME:-}

EOF

echo "

===========================================
Properties file created `date`:  $PROP_FILE
Note: apiTokens and password are hidden
===========================================
`cat $PROP_FILE | egrep -vi 'password|apiTokens'`
"

exit 0
