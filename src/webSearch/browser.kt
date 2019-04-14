package webSearch;

import java.net.URL;

import channel.*;


class Browser {
    private fun search(searchTerm: String, url: String): Unit {
        try {
            if(WebSearchConfig.verbose){
                println("Searching: ${url} for ${searchTerm}")
            }
            val urlBody = URL("${WebSearchConfig.urlPrefix}${url}").readText()//readText(Charset.forName("ISO-8859-1"))
            val result = urlBody.contains(searchTerm, ignoreCase = true)

            CSV().writeRow("${url},${searchTerm},${result}")
        } catch (e: Exception) {
            if(WebSearchConfig.verbose){
                println("Error Accessing URL - ${url}")
                e.printStackTrace()
            }

            CSV().writeRow("${url},${searchTerm},error")
        }
    }

    suspend fun process(searchTerm: String, browserChannel: Channel<String>, quit: Channel<Int>? = null ): Unit  {
        for(url in browserChannel){
            if(WebSearchConfig.verbose){
                println(browserChannel)
            }
            search(searchTerm, url)
            //quit?.send(1) //fixme - this shouldnt be nullable, fix if this experiement works
            //println("---")

        }

        //println("closing")
        //browserChannel.close()
    }
}