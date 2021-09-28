pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
        stage('Stg1') {
            steps {
               echo "Step 1"
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