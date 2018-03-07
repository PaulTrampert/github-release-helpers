package com.ptrampert.github

class ReleaseInfo implements Serializable {
    private static final long serialVersionUID = 1234L

    SemVer previousVersion
    ArrayList<Change> changelog

    SemVer nextVersion() {
        ChangeLevel maxChange = changelog.max {
            it.changeLevel
        }.getChangeLevel()
        SemVer next = previousVersion.clone()
        next.increment(maxChange)
        return next
    }

    String changelogToMarkdown() {
        def majorChanges = []
        def minorChanges = []
        def patches = []

        for (def change : changeLog) {
            if (change.changeLevel == ChangeLevel.MAJOR) {
                majorChanges.add(change)
            }
            else if (change.changeLevel == ChangeLevel.MINOR) {
                minorChanges.add(change)
            }
            else {
                patches.add(change)
            }
        }

        def markdown = ""
        markdown += "## Changelog\r\n"
        for (def change : majorChanges) {
            markdown += "${change.toMarkdown()}"
        }
        for (def change : minorChanges) {
            markdown += "${change.toMarkdown()}"
        }
        for (def change : patches) {
            markdown += "${change.toMarkdown()}"
        }
        return markdown
    }
}
