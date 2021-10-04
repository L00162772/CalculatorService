pipeline {
    agent any

    //Setup the required environment variables
    environment {
        // The AWS access and secret key are created as jenkins secrets and referenced in this file
        // These credentials should never be stored in source control
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')

        // These environment variables are used in different stages of the project
        //They are the same for each app environemnt (develop, test and production) so that author added them as environment variables to demonstrate that knowledge
        // They could as easily been defined as parameters and that solution would have worked
        AWS_S3_BUCKET = 'elasticbeanstalk-eu-west-1-541450550503'
        AWS_EB_APP_VERSION = "${BUILD_ID}"
        BRANCH_NAME = "${GIT_BRANCH.split("/")[1]}"
    }  

    // Parametes are presented to the user when they click the build with parameters link in jenkins
    // From the screen, the user can run the job with the default parameters or change the parameters to suit their needs
    // this gives the person running the pipleine greater flexibilty on where they deploy code to
    parameters {
        string(name: 'jenkinsArtifact', defaultValue: './target/calculator-0.0.1-SNAPSHOT.jar', description: 'Artiftact to publish to elastic beanstalk that was generated with Jenkins')
        string(name: 's3ArtifactNamePostfix', defaultValue: 'calculator.jar', description: 'Postfix for the stored artifact in jenkins. The artifact is prefixed with the build number and branch')
        string(name: 'awsEBAppName', defaultValue: 'CalculatorService', description: 'The name of elastic beanstalk application to deploy to')
        string(name: 'awsEBEnvironmentPrefix', defaultValue: 'Calculatorservice', description: 'The prefix for the elastic beanstalk application environment to deploy to. The environment is')
        string(name: 'awsRegion', defaultValue: 'eu-west-1', description: 'The region to run the AWS deployment in.')
    }

    // The pipeline is made up of stages which each contain streps to be executed.
    stages {
        //The notification - Start stage sends a slack message to the user to tell them the pipeline has started in a particular job for either develop, test or main
        //Notice the use of environment variables in the message
        stage('Notification - Start') {
            steps {
                slackSend botUser: true, 
                          channel: '#damien-jenkins-lyit', 
                          color: '#00ff00', 
                          message: "STARTED: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL})",
                          tokenCredentialId: 'SlackBot-Jenkins'
            }
        }    

        // The build stage outputs the branch the code is being built from and then runs the standard maven comman to install all dependencies and install the code
        stage('Build') {
            steps {
                echo 'Pulling from branch...' + env.BRANCH_NAME
                sh 'mvn --version'
                sh 'mvn clean install -DskipTests=true'
            }
        }

        // The tests stage is only run on the develop or test branch and not for main
        // This decision as made to speed up a production deployment when building from the main branch
        stage('Tests') {
            when {
               not {
                   expression { env.BRANCH_NAME == 'main' }                  
               }
            }
            // The author decided the unit tests and integration tests can run in parralel - this will speed up the pipeline
            parallel{        
                stage("Running unit tests") {
                    steps{
                        sh "pwd"
                        sh "ls -latr"
                        sh "./mvnw test -Dmaven.test.failure.ignore=false"
                   
                   step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*Test.xml'])
                    } 
                }
                stage("Running integration tests") {
                    steps {
                        sh "./mvnw package -DskipTests=true"
                        sh "./mvnw test -PintegrationTest -Dmaven.test.failure.ignore=false"
                    }
                }
            }
        }
        // The development tools stage is only run against the develop branch - again this is to speed up test and production deployments
        // Also the feedback received from ther checkstyle and javadoc steps are mostly developer focused
        stage('Development Tools') {
            when {
               expression { env.BRANCH_NAME == 'develop' }                 
            }
            parallel{
                stage('Checkstyle Analysis') {
                    steps {
                          sh 'mvn checkstyle:checkstyle'
                    }
                }     
                stage('Javadoc') {
                    steps {
                          sh 'mvn javadoc:javadoc'
                    }
                }                  
            }
        }          
   
        // The deploy artifact is only run on the test and main branches
        // No deployments are done from the dev branch - this is done as the author decided to only have 2 AWS environments
        stage('Deploy Artifact') {
            when { 
                anyOf { 
                    expression { env.BRANCH_NAME == 'test' } 
                    expression { env.BRANCH_NAME == 'main' } 
                } 
            }
            // The first step is to package the code
            steps {
                sh './mvnw package'
            }
            post {
                // IF the code package was successful, the artifact is published to the correct AWS elastic beanstalk environment
                success {
                    archiveArtifacts 'target/*.jar'
                    sh "aws --version"
                    sh "aws configure set region ${params.awsRegion}"
                    sh "aws s3 cp ${params.jenkinsArtifact} s3://$AWS_S3_BUCKET/$AWS_EB_APP_VERSION-$BRANCH_NAME-${params.s3ArtifactNamePostfix}"
                    sh "aws elasticbeanstalk create-application-version --application-name ${params.awsEBAppName} --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$AWS_EB_APP_VERSION-$BRANCH_NAME-${params.s3ArtifactNamePostfix}"
                    sh "aws elasticbeanstalk update-environment --application-name ${params.awsEBAppName} --environment-name ${params.awsEBEnvironmentPrefix}-$BRANCH_NAME --version-label $AWS_EB_APP_VERSION"
                }
            }
        }                   
    }
    //The post block is used to run events regardless of the outcome of the other steps in the pipeline
    post {
        // At the end of every build - including failures - an email is sent to the client stating if the job was successful or not and also build information is sent with that email
        always {
            mail to: 'damien.gallagher@gmail.com',
                     subject: "${currentBuild.currentResult} Pipeline: ${currentBuild.fullDisplayName}",
                     body: "The status of the job ${env.BUILD_URL} is ${currentBuild.currentResult} \n\nBuild Time (timeInMillis): ${currentBuild.timeInMillis}\nBuild Time (startTimeInMillis): ${currentBuild.startTimeInMillis} \nBuild Time (duration): ${currentBuild.duration} \nBuild Time (durationString): ${currentBuild.durationString} \nCurrentBuild (number): ${currentBuild.number} \nCurrentBuild (currentResult): ${currentBuild.currentResult} \nCurrentBuild (displayName): ${currentBuild.displayName} \nCurrentBuild (fullDisplayName): ${currentBuild.fullDisplayName} \nCurrentBuild (projectName): ${currentBuild.projectName} \nCurrentBuild (fullProjectName): ${currentBuild.fullProjectName} \nCurrentBuild (description): ${currentBuild.description} \nCurrentBuild (id): ${currentBuild.id} \nCurrentBuild (absoluteUrl): ${currentBuild.absoluteUrl} \nCurrentBuild (buildVariables): ${currentBuild.buildVariables} \nCurrentBuild (changeSets): ${currentBuild.changeSets} \nCurrentBuild (keepLog): ${currentBuild.keepLog}" 
        }
        //On success a slack message is sent with a green colored marker
        success {
            slackSend botUser: true, 
                  channel: '#damien-jenkins-lyit', 
                  color: '#33cc33', 
                  message: "Success \n Completed: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL}).\n Duration (millis):  ${currentBuild.duration} \n Duration(string):  ${currentBuild.durationString} \n Result: ${currentBuild.currentResult}",
                  tokenCredentialId: 'SlackBot-Jenkins'                      
        }
        //On an unstable build, a slack message is sent with a yellow colored marker
        unstable {
            slackSend botUser: true, 
                  channel: '#damien-jenkins-lyit', 
                  color: '#ffff00', 
                  message: "Unstable \n Completed: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL}).\n Duration (millis):  ${currentBuild.duration} \n Duration(string):  ${currentBuild.durationString} \n Result: ${currentBuild.currentResult}",
                  tokenCredentialId: 'SlackBot-Jenkins'                      
        }   
        //On a failed build, a slack message is sent with a red colored marker
        failure {
            slackSend botUser: true, 
                  channel: '#damien-jenkins-lyit', 
                  color: '#ff0000', 
                  message: "Failure \n Completed: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL}).\n Duration (millis):  ${currentBuild.duration} \n Duration(string):  ${currentBuild.durationString} \n Result: ${currentBuild.currentResult}",
                  tokenCredentialId: 'SlackBot-Jenkins'                      
        }                      
    }      
}