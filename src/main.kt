package main;

import channel.*;
import webSearch.*;

fun main(args: Array<String>) = mainBlocking()  { //chose not to do command line arguments, options come from webSearch/config file

    val csv = CSV()
    val browser = Browser()

    val urls: ArrayList<String>? = csv.readColumn()
    val browserChannel = Channel<String>(WebSearchConfig.maxProcesses)
    //val quitChannel = Channel<Int>(20) //todo - investigate how to properly close channel
    var numProcessed = 0;

    //truncate results file
    csv.newFile()

    if(urls !== null){
        csv.writeRow(WebSearchConfig.resultHeader)
        for(i in 1..WebSearchConfig.maxProcesses){ //launch co-routines
            go { browser.process(WebSearchConfig.searchTerm, browserChannel/*, quitChannel*/) }
        }
        for(url in urls){ //fill channel queue
           browserChannel.send(url)
           println(url)
        }

        /*
        for(i in quitChannel){
            println(i)
            numProcessed++;
            println("processed: ${numProcessed}")
        }
        */
    } else {
        println("No URLs found to search.")
    }

}
