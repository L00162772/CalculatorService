pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
                sh 'mvn clean install -DskipTests'
            }
        }
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
                      '**/target/surefire-reports/TEST-' 
                        + '*Test.xml'])
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
                echo 'Hello World'
            }
        }   
        stage('Example') {
            steps {
                script { 
                    if (env.BRANCH_NAME != 'main') {
                        echo 'This is not main but it is:' + env.BRANCH_NAME
                        echo 'env:' + env
                    } else {
                        echo 'things and stuff'
                    }
                }
            }
        }           
    }
}