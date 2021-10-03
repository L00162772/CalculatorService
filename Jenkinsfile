pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                echo 'Pulling...' + env.BRANCH_NAME
                sh 'mvn --version'
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('Execute tests') {

            parallel{        
                stage("Running unit tests") {
                    steps{
                        sh "pwd"
                        sh "ls -latr"
                        sh "./mvnw test install"
                   
                   step([$class: 'JUnitResultArchiver', testResults: 
                     '**/target/surefire-reports/TEST-*Test.xml'])
                    } 
                }
                stage("Running integration tests") {
                    steps {
                        sh "./mvnw package -DskipTests=true"
                        //sh "docker-compose -f docker-compose.yml up -d"
                        sh "./mvnw test -PintegrationTest"
                    step([$class: 'JUnitResultArchiver', testResults: 
                      '**/target/surefire-reports/TEST-*Test.xml'])
                    }
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