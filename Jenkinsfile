pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
        JENKINS_ARTIFACT = './target/calculator-0.0.1-SNAPSHOT.jar'
        S3_ARTIFACT_NAME = 'calculator.jar'
        AWS_S3_BUCKET = 'elasticbeanstalk-eu-west-1-541450550503'
        AWS_EB_APP_NAME = 'CalculatorService'
        AWS_EB_ENVIRONMENT_PREFIX = 'Calculatorservice'
        AWS_EB_APP_VERSION = "${BUILD_ID}"
        BRANCH = "${BRANCH_NAME}"
        REGION = "eu-west-1"
    }  

    parameters {
        string(name: 'suiteFile', defaultValue: '', description: 'Suite File')
    }

    stages {

        stage('Tests123') {
            when {
               not {
                   branch 'origin/develop'                  
               }
            }
            steps {
                echo 'not develop branch'
            }
        }
        stage('Tests456') {
            when {
                branch 'origin/main'                  
            }
            steps {
                echo 'main branch'
            }
        }

        stage('Build') {
            steps {
                echo 'Pulling from branch...' + env.BRANCH_NAME
                echo 'Pulling from branch...' + env.GIT_BRANCH
                sh 'mvn --version'
                sh 'mvn clean install -DskipTests=true'
            }
        }

        stage('Tests') {
            when {
               not {
                   branch 'main'                  
               }
            }
            parallel{        
                stage("Running unit tests") {
                    steps{
                        sh "pwd"
                        sh "ls -latr"
                        sh "./mvnw test install"
                   
                   step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*Test.xml'])
                    } 
                }
                stage("Running integration tests") {
                    steps {
                        sh "./mvnw package -DskipTests=true"
                        sh "./mvnw test -PintegrationTest"
                    }
                }
            }
        }
        stage('Development Tools') {
            when {
               branch 'develop'                  
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
                    branch 'test'; 
                    branch 'main' 
                } 
            }
            steps {
                sh './mvnw package'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                    sh 'aws --version'
                    sh 'aws configure set region $REGION'
                    sh 'aws s3 cp $JENKINS_ARTIFACT s3://$AWS_S3_BUCKET/$AWS_EB_APP_VERSION-$S3_ARTIFACT_NAME'
                    sh 'aws elasticbeanstalk create-application-version --application-name $AWS_EB_APP_NAME --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$AWS_EB_APP_VERSION-$S3_ARTIFACT_NAME'
                    sh 'aws elasticbeanstalk update-environment --application-name $AWS_EB_APP_NAME --environment-name $AWS_EB_ENVIRONMENT_PREFIX-$BRANCH --version-label $AWS_EB_APP_VERSION'
                }
            }
        }     
    }
}