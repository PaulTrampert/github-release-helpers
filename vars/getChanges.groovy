import com.ptrampert.github.ChangeLevel

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
    commits.each {
        def matcher = (it.message =~ ~/Merge pull request #(\d+)/)
        if (matcher.find()) {
            def pr = makeRequest("${apiRoot}/repos/${owner}/${repo}/pulls/${matcher.group(1)}", credentialsId)
            def prCommits = makeRequest(pr.commits_url, credentialsId)
            pullRequests.add(pr)
            commitsInPrs.addAll prCommits
            commitsInPrs.add(it)
        }
    }

    pullRequests.each {pr ->
        def change = new Change()
        change.author = new Link(pr.user.login, pr.user.html_url)
        change.change = new Link(pr.id, pr.html_url)
        change.description = pr.title
        def maxChange = ChangeLevel.PATCH
        pr.labels.each {label ->
            try {
                def labeledChangeLevel = ChangeLevel.valueOf(label.name.toUpperCase())
                if (labeledChangeLevel.getValue() > maxChange.getValue()) {
                    maxChange = labeledChangeLevel
                }
            } catch (Exception e) {
                echo "Unrecognized label ${label.name}"
            }
        }
        change.changeLevel = maxChange
        changes.add(change)
    }
    return changes
}