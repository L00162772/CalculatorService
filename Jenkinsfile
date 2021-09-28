pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
           parallel 
            {
                stage("Runing unit tests") {
                    steps{
                        sh "pwd"
                        sh "ls -latr"
                        sh "ls -latr spring-jenkins-pipeline"
                        sh "./mvnw -B clean install -PintegrationTest"
                   
                   step([$class: 'JUnitResultArchiver', testResults: 
                     '**/target/surefire-reports/TEST-*UnitTest.xml'])
                    }
                }
             stage('Integration tests') {
                stage("Runing integration tests") {
                    try {
                        sh "./mvnw  -B clean package -DskipTests=true"
                        sh "docker-compose -f docker-compose.yml up -d"
                    } catch(err) {
                        step([$class: 'JUnitResultArchiver', testResults: 
                          '**/target/surefire-reports/TEST-' 
                            + '*IntegrationTest.xml'])
                        throw err
                    }
                    step([$class: 'JUnitResultArchiver', testResults: 
                      '**/target/surefire-reports/TEST-' 
                        + '*IntegrationTest.xml'])
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
                   branch "main"                  
               }
            }
            steps {
                echo 'Hello World'
            }
        }              
    }
}