pipeline {
    agent any;

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Generate Release Info Test') {
            steps {
                def releaseInfo = generateGithubReleaseInfo(
                        owner: 'PaulTrampert',
                        repo: 'github-release-helpers',
                        tagPrefix: 'v',
                        credentialsId: 'github_token'
                )

                echo releaseInfo
            }
        }
    }
}