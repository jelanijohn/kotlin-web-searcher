package webSearch;

import java.net.URL;

import channel.*;


class Browser {
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

    suspend fun process(searchTerm: String, browserChannel: Channel<String>, quit: Channel<Int>): Unit  {
        for(url in browserChannel){
            search(searchTerm, url, quit)

            if(WebSearchConfig.verbose){
                println(browserChannel)
            }


        }
    }
}