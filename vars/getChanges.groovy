import com.ptrampert.github.ChangeLevel

@NonCPS
def call(
        owner,
        repo,
        lastVersion,
        credentialsId = null,
        githubApiRoot = "https://api.github.com"
) {
    def changes = new ArrayList<Change>()
    def apiRoot = githubApiRoot
    def responseBody = makeRequest(
            "${apiRoot}/repos/${owner}/${repo}/compare/${lastVersion.toString()}...${env.BRANCH_NAME ?: 'master'}".toString(),
            credentialsId
    )
    def commits = responseBody.commits
    def pullRequests = []
    def commitsInPrs = []
    commits.each {
        echo it.commit.message
        def matcher = (it.commit.message =~ /Merge pull request #(\d+)/)
        if (matcher.find()) {
            def pr = makeRequest("${apiRoot}/repos/${owner}/${repo}/pulls/${matcher.group(1)}", credentialsId)
            def prCommits = makeRequest(pr.commits_url, credentialsId)
            pullRequests.add(pr)
            commitsInPrs.addAll prCommits
            commitsInPrs.add(it)
        }
    }

    pullRequests.each {
        Change change = new Change()
        change.author = new Link(text: it.user.login, href: it.user.html_url)
        change.change = new Link(text: it.id, href: it.html_url)
        change.description = it.title
        ChangeLevel maxChange = ChangeLevel.PATCH
        pr.labels.each {
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
    }
    return changes
}