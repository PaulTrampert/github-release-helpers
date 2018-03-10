@NonCPS
def call(message) {
    def matcher = (message =~ /Merge pull request #(\d+)/)
    if (matcher.find()) {
        return matcher.group(1)
    }
    return null
}
