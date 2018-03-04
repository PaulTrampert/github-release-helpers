def call(
        url,
        credentialsId
) {
    def response = httpRequest(
            url: url,
            authentication: credentialsId
    )
    if (response.status != 200) {
        throw new Exception("Failed to make request to $url")
    }
    return readJSON(text: response.content)
}
