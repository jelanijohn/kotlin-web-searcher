package webSearch;

import java.net.URL;

import channel.*;

/**
* Web Browser class
*
*/
class Browser {
    /**
    * Searches for the [searchTerm] in [url] and confirms finish by writing to the [quit] channel
    * Writes results ( true / false / error ) to output file specified in WebSearchConfig
    */
    private suspend fun search(searchTerm: String, url: String, quit: Channel<Int>): Unit {
        try {
            if(WebSearchConfig.verbose){
                println("Searching: ${url} for ${searchTerm}")
            }
            val urlBody = URL("${WebSearchConfig.urlPrefix}${url}").readText()//readText(Charset.forName("ISO-8859-1"))
            val result = urlBody.contains(searchTerm, ignoreCase = true)

            CSV().writeRow("${url},${searchTerm},${result}")

             quit.receive()
        } catch (e: Exception) {
            if(WebSearchConfig.verbose){
                println("Error Accessing URL - ${url}")
                e.printStackTrace()
            }

            CSV().writeRow("${url},${searchTerm},error")
            quit.receive()
        }
    }

    /**
    * Pulls from the [browserChannel] channel, and initiates the search
    *
    * @param searchTerm search term, passed through to search
    * @param browserChannel urls to search for pulled from channel
    * @param quit channel passeed through to search
    */
    suspend fun process(searchTerm: String, browserChannel: Channel<String>, quit: Channel<Int>): Unit  {
        for(url in browserChannel){
            search(searchTerm, url, quit)

            if(WebSearchConfig.verbose){
                println(browserChannel)
            }


        }
    }
}