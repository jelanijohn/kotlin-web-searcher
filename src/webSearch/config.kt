package webSearch;

object WebSearchConfig { //one way of doing a singleton, but in a larger app would want to load and parse a json file
    val maxProcesses: Int = 20 //todo - think about - these variables might be better capitalized as an indicator
    val searchTerm: String  = "html"
    val urlPrefix: String = "https://www."
    val resultHeader: String = "URL,Search Term,Result"
    val searchFile = "data/urls.txt"
    val searchFileUrlIndex = 1
    val outputFile = "results.txt"
    val verbose: Boolean = true
}