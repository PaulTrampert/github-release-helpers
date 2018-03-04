import com.ptrampert.github.SemVer

def call() {
    def describeString
    if (isUnix()) {
        describeString = sh returnStdout: true, script: 'git describe --tags'
    }
    else {
        describeString = bat returnStdout: true, script: 'git describe --tags'
    }

    return SemVer.parse(describeString)
}