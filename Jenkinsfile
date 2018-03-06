pipeline {
    agent any;

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Generate Release Info Test') {
            steps {
                script {
                    def releaseInfo = generateGithubReleaseInfo(
                            'PaulTrampert',
                            'github-release-helpers',
                            'v',
                            'github_token'
                    )

                    echo releaseInfo
                }
            }
        }
    }
}