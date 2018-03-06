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
            "${apiRoot}/repos/${owner}/${repo}/compare/${lastVersion.toString()}...${env.BRANCH_NAME ?: 'master'}".toString(),
            credentialsId
    )
    def pullRequests = []
    def commitsInPrs = []
    responseBody.commits.each {
        def matcher = (it.message =~ ~/Merge pull request #(\d+)/)
        if (matcher.find()) {
            def pr = makeRequest("${apiRoot}/repos/${owner}/${repo}/pulls/${matcher.group(1)}", credentialsId)
            def prCommits = makeRequest(pr.commits_url, credentialsId)
            pullRequests.add(pr)
            commitsInPrs.addAll prCommits
            commitsInPrs.add(it)
        }
    }

    pullRequests.each {
    }
}