
def call(
        owner,
        repo,
        lastVersion,
        credentialsId = null,
        githubApiRoot = "https://api.github.com"
) {
    def changes = []
    def apiRoot = githubApiRoot
    def responseBody = makeRequest(
            "${apiRoot}/repos/${owner}/${repo}/compare/${lastVersion.toString()}...${env.BRANCH_NAME ?: 'master'}".toString(),
            credentialsId
    )
    def commits = responseBody.commits
    def pullRequests = []
    def commitsInPrs = []
    for (def commit : commits) {
        echo commit.commit.message
        def matcher = (commit.commit.message =~ /Merge pull request #(\d+)/)
        if (matcher.find()) {
            def pr = makeRequest("${apiRoot}/repos/${owner}/${repo}/pulls/${matcher.group(1)}", credentialsId)
            def prCommits = makeRequest(pr.commits_url, credentialsId)
            pullRequests.add(pr)
            commitsInPrs.addAll prCommits
            commitsInPrs.add(commit)
        }
    }

/*    for (def pr : pullRequests) {
        Change change = new Change()
        change.author = new Link(text: pr.user.login, href: pr.user.html_url)
        change.change = new Link(text: pr.id, href: pr.html_url)
        change.description = pr.title
        ChangeLevel maxChange = ChangeLevel.PATCH
        for (def label : pr.labels) {
           try {
                ChangeLevel labeledChangeLevel = ChangeLevel.valueOf(label.name.toUpperCase())
                if (labeledChangeLevel.getValue() > maxChange.getValue()) {
                    maxChange = labeledChangeLevel
                }
            } catch (Exception e) {
               echo "Unrecognized label ${label.name}"
            }
        }
        change.changeLevel = maxChange
        changes.add(change)
    }*/
    //return changes
}