def releaseInfo
def branch = null

pipeline {
    agent any;

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Set branch') {
            when {
				expression {env.BRANCH_NAME != 'master'}
			}

            steps {
                script {
                    branch = env.BRANCH_NAME
                }
            }
        }

        stage('Generate Release Info Test') {
            steps {
                script {
                    releaseInfo = generateGithubReleaseInfo(
                        'PaulTrampert',
                        'github-release-helpers',
                        'v',
                        'Github User/Pass',
                        "https://api.github.com",
                        branch,
                        env.BUILD_NUMBER
                    )

                    echo releaseInfo.nextVersion().toString()
                    echo releaseInfo.changelogToMarkdown()
                }
            }
        }

        stage('Publish Release Test') {
            steps {
                script {
                    publishGithubRelease(
                            'PaulTrampert',
                            'github-release-helpers',
                            releaseInfo,
                            'v',
                            'Github User/Pass'
                    )
                }
            }
        }
    }
}