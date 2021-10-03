pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('jenkins-aws-secret-key-id')
        AWS_SECRET_ACCESS_KEY = credentials('jenkins-aws-secret-access-key')
        ARTIFACT_NAME = 'calculator.jar'
        AWS_S3_BUCKET = 'YOUR S3 BUCKET NAME'
        AWS_EB_APP_NAME = 'calculator'
        AWS_EB_ENVIRONMENT = 'Calculator-env'
        AWS_EB_APP_VERSION = "${BUILD_ID}"
    }  

    stages {
        stage('Build') {
            steps {
                echo 'Pulling from branch...' + env.BRANCH_NAME
                sh 'mvn --version'
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('Test') {
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
        stage('Checkstyle Analysis') {
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
        }        
        stage('Publish Artifact') {
            steps {
                sh './mvnw package'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                    sh 'aws --version'
                    sh 'aws configure set region us-east-1'
                    sh 'aws s3 cp ./target/calculator-0.0.1-SNAPSHOT.jar s3://$AWS_S3_BUCKET/$ARTIFACT_NAME'
                    sh 'aws elasticbeanstalk create-application-version --application-name $AWS_EB_APP_NAME --version-label $AWS_EB_APP_VERSION --source-bundle S3Bucket=$AWS_S3_BUCKET,S3Key=$ARTIFACT_NAME'
                    sh 'aws elasticbeanstalk update-environment --application-name $AWS_EB_APP_NAME --environment-name $AWS_EB_ENVIRONMENT --version-label $AWS_EB_APP_VERSION'
                }
            }
        }        
        stage('Stg2') {

            parallel{
                stage('Stg2.1') {
                    steps {
                        echo "Step 2.1"
                     }
                }
                stage('Stg2.2') {
                    steps {
                        echo "Step 2.2"
                     }
                }                
            }
        }  
        stage('Stg3') {
            when {
               not {
                   branch 'main'                  
               }
            }
            steps {
                echo 'Hello World this is not main but the branch is:'+ env.BRANCH_NAME
            }
        }   
        stage('Example') {
            steps {
                script { 
                    if (env.BRANCH_NAME != 'main') {
                        echo 'This is not main but it is:' + env.BRANCH_NAME
                        echo 'env:' + env
                    } else {
                        echo 'This is the main branch'
                    }
                }
            }
        }           
    }
}