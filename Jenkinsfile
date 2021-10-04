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
        stage('Metrics') {
            steps {
                echo "Build Time (currentBuild): ${currentBuild}" 
                echo "Build Time (timeInMillis): ${currentBuild.timeInMillis}" 
                echo "Build Time (startTimeInMillis): ${currentBuild.startTimeInMillis}" 
                echo "Build Time (duration): ${currentBuild.duration}" 
                echo "Build Time (durationString): ${currentBuild.durationString}"            
                echo "CurrentBuild (number): ${currentBuild.number}"    
                echo "CurrentBuild (currentResult): ${currentBuild.currentResult}"  
                echo "CurrentBuild (result): ${currentBuild.result}"  
                echo "CurrentBuild (displayName): ${currentBuild.displayName}"  
                echo "CurrentBuild (fullDisplayName): ${currentBuild.fullDisplayName}"  
                echo "CurrentBuild (projectName): ${currentBuild.projectName}"  
                echo "CurrentBuild (fullProjectName): ${currentBuild.fullProjectName}"                                                                                                                                                                                                         
                echo "CurrentBuild (description): ${currentBuild.description}"  
                echo "CurrentBuild (id): ${currentBuild.id}"  
                echo "CurrentBuild (absoluteUrl): ${currentBuild.absoluteUrl}"                                         
                echo "CurrentBuild (buildVariables): ${currentBuild.buildVariables}"        
                echo "CurrentBuild (changeSets): ${currentBuild.changeSets}"        
                echo "CurrentBuild (keepLog): ${currentBuild.keepLog}"                                                                           
            }
        }        
    }
}