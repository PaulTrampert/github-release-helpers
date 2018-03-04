def call(
        owner,
        repo,
        lastVersion,
        credentialsId = null,
        githubApiRoot = "https://api.github.com"
) {
    def changes = []
    def apiRoot = githubApiRoot.trim('/')
    def responseBody = makeRequest(
            url: "${apiRoot}/repos/${owner}/${repo}/compare/${lastVersion.toString()}...${env.BRANCH_NAME ?: 'master'}".toString(),
            credentialsId: credentialsId
    )
    def pullRequests = []
    def commitsInPrs = []
    responseBody.commits.each {
        def matcher = (it.message =~ ~/Merge pull request #(\d+)/)
        if (matcher.find()) {
            def pr = makeRequest(url: "${apiRoot}/repos/${owner}/${repo}/pulls/${matcher.group(1)}", credentialsId: credentialsId)
            def prCommits = makeRequest(url: pr.commits_url, credentialsId: credentialsId)
            pullRequests.add(pr)
            commitsInPrs.addAll(items: prCommits)
            commitsInPrs.add(it)
        }
    }

    pullRequests.each {
    }
}