node {
    stage 'Clone the project'
    git 'https://github.com/eugenp/tutorials.git'
  
    dir('spring-jenkins-pipeline') {
                stage("Tests and Deployment") {
            parallel 'Unit tests': {
                stage("Runing unit tests") {
                    try {
                        sh "./mvnw -B clean install -PintegrationTest"
                    } catch(err) {
                        step([$class: 'JUnitResultArchiver', testResults: 
                          '**/target/surefire-reports/TEST-*UnitTest.xml'])
                        throw err
                    }
                   step([$class: 'JUnitResultArchiver', testResults: 
                     '**/target/surefire-reports/TEST-*UnitTest.xml'])
                }
            }, 'Integration tests': {
                stage("Runing integration tests") {
                    try {
                        sh "./mvnw  -B clean package -DskipTests=true &&
        docker-compose -f docker-compose.yml up -d"
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

        stage("Compilation and Analysis") {
            parallel 'Compilation': {
                sh "./mvnw clean install -DskipTests"
            }, 'Static Analysis': {
                stage("Checkstyle") {
                    sh "./mvnw checkstyle:checkstyle"
                    
                    step([$class: 'CheckStylePublisher',
                      canRunOnFailed: true,
                      defaultEncoding: '',
                      healthy: '100',
                      pattern: '**/target/checkstyle-result.xml',
                      unHealthy: '90',
                      useStableBuildAsReference: true
                    ])
                }
            }
        }
        

    }
}