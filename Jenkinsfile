pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')

        AWS_S3_BUCKET = 'elasticbeanstalk-eu-west-1-541450550503'
        AWS_EB_APP_VERSION = "${BUILD_ID}"
        REGION = "eu-west-1"
        BRANCH_NAME = "${GIT_BRANCH.split("/")[1]}"
    }  

    parameters {
        string(name: 'jenkinsArtifact', defaultValue: './target/calculator-0.0.1-SNAPSHOT.jar', description: 'Artiftact to publish to elastic beanstalk that was generated with Jenkins')
        string(name: 's3ArtifactNamePostfix', defaultValue: 'calculator.jar', description: 'Postfix for the stored artifact in jenkins. The artifact is prefixed with the build number and branch')
        string(name: 'awsEBAppName', defaultValue: 'CalculatorService', description: 'The name of elastic beanstalk application to deploy to')
        string(name: 'awsEBEnvironmentPrefix', defaultValue: 'Calculatorservice', description: 'The prefix for the elastic beanstalk application environment to deploy to. The environment is')
    }

    stages {

        stage('Notification - Start') {
            steps {
                slackSend botUser: true, 
                          channel: '#damien-jenkins-lyit', 
                          color: '#00ff00', 
                          message: "STARTED: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL})",
                          tokenCredentialId: 'SlackBot-Jenkins'
            }
        }    

        stage('Build') {
            steps {
                echo 'Pulling from branch...' + env.BRANCH_NAME
                sh 'mvn --version'
                sh 'mvn clean install -DskipTests=true'
            }
        }


        stage('Tests') {
            when {
               not {
                   expression { env.BRANCH_NAME == 'main' }                  
               }
            }
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
   
        stage('Publish Artifact') {
            when { 
                anyOf { 
                    expression { env.BRANCH_NAME == 'test' } 
                    expression { env.BRANCH_NAME == 'main' } 
                } 
            }
            steps {
                sh './mvnw package'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                    sh "aws --version"
                    sh "aws configure set region $REGION"
                    sh "aws s3 cp ${params.jenkinsArtifact} s3://$AWS_S3_BUCKET/$AWS_EB_APP_VERSION-$BRANCH_NAME-${params.s3ArtifactNamePostfix}"
                    sh "aws elasticbeanstalk create-application-version --application-name ${params.awsEBAppName} --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$AWS_EB_APP_VERSION-$BRANCH_NAME-${params.s3ArtifactNamePostfix}"
                    sh "aws elasticbeanstalk update-environment --application-name ${params.awsEBAppName} --environment-name ${params.awsEBEnvironmentPrefix}-$BRANCH_NAME --version-label $AWS_EB_APP_VERSION"
                }
            }
        }                   
    }
    post {
        always {
            mail to: 'damien.gallagher@gmail.com',
                     subject: "${currentBuild.currentResult} Pipeline: ${currentBuild.fullDisplayName}",
                     body: "The status of the job ${env.BUILD_URL} is ${currentBuild.currentResult} \n\n Build Time (timeInMillis): ${currentBuild.timeInMillis} \nBuild Time (startTimeInMillis): ${currentBuild.startTimeInMillis} \n Build Time (duration): ${currentBuild.duration} \n Build Time (durationString): ${currentBuild.durationString} \n CurrentBuild (number): ${currentBuild.number} \nCurrentBuild (currentResult): ${currentBuild.currentResult} \n CurrentBuild (displayName): ${currentBuild.displayName} \n CurrentBuild (fullDisplayName): ${currentBuild.fullDisplayName} \n CurrentBuild (projectName): ${currentBuild.projectName} \nCurrentBuild (fullProjectName): ${currentBuild.fullProjectName} \n CurrentBuild (description): ${currentBuild.description} \nCurrentBuild (id): ${currentBuild.id} \n CurrentBuild (absoluteUrl): ${currentBuild.absoluteUrl} \n CurrentBuild (buildVariables): ${currentBuild.buildVariables} \nCurrentBuild (changeSets): ${currentBuild.changeSets} \n CurrentBuild (keepLog): ${currentBuild.keepLog}" 
        }
        success {
            slackSend botUser: true, 
                  channel: '#damien-jenkins-lyit', 
                  color: '#33cc33', 
                  message: "Success \n Completed: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL}).\n Duration (millis):  ${currentBuild.duration} \n Duration(string):  ${currentBuild.durationString} \n Result: ${currentBuild.currentResult}",
                  tokenCredentialId: 'SlackBot-Jenkins'                      
        }
        unstable {
            slackSend botUser: true, 
                  channel: '#damien-jenkins-lyit', 
                  color: '#ffff00', 
                  message: "Unstable \n Completed: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL}).\n Duration (millis):  ${currentBuild.duration} \n Duration(string):  ${currentBuild.durationString} \n Result: ${currentBuild.currentResult}",
                  tokenCredentialId: 'SlackBot-Jenkins'                      
        }   
        failure {
            slackSend botUser: true, 
                  channel: '#damien-jenkins-lyit', 
                  color: '#ff0000', 
                  message: "Failure \n Completed: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL}).\n Duration (millis):  ${currentBuild.duration} \n Duration(string):  ${currentBuild.durationString} \n Result: ${currentBuild.currentResult}",
                  tokenCredentialId: 'SlackBot-Jenkins'                      
        }                      
    }      
}