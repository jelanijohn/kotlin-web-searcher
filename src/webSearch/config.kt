package webSearch;

/**
* Singleton config object used throughout this package
*
*/
object WebSearchConfig {
    val maxProcesses: Int = 20
    val searchTerm: String  = "html"
    val urlPrefix: String = "https://www."
    val resultHeader: String = "URL,Search Term,Result"
    val searchFile = "data/urls.txt"
    val searchFileUrlIndex = 1
    val outputFile = "results.txt"
    val verbose: Boolean = true
}