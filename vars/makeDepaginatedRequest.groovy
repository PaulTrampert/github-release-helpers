def call(
        url,
        credentialsId
) {
    def results = []
    def nextUrl = url
    while(nextUrl) {
        def response = httpRequest(
                url: url,
                authentication: credentialsId
        )
        if (response.status != 200) {
            throw new Exception("Failed to make request to $url")
        }
        results.addAll(readJSON(text: response.content))
        try {
            def headers = response.customHeaders
            echo headers.toString()
            def linkHeader = headers.find {it.name.toLowerCase() == "link"}
            echo linkHeader.toString()
            def matcher = linkHeader =~ /<([^>]+)>;\s+rel="next"/
        }catch(Exception e) {
            nextUrl = null
        }
    }
}
