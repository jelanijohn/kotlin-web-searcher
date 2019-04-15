package main;

import channel.*;
import webSearch.*;

/**
* Main function
*
* This function reads urls from a csv file, sets up channels and kotlin co-routines to perform concurrent searches for a search term.
* All configurable options come from webSearch/config file.
*
* @param args command line arguments, currently not used
*/
fun main(args: Array<String>) = mainBlocking()  {

    val csv = CSV()
    val browser = Browser()

    val urls: ArrayList<String>? = csv.readColumn()
    val browserChannel = Channel<String>(WebSearchConfig.maxProcesses)
    val quitChannel = Channel<Int>(WebSearchConfig.maxProcesses)
    var numProcessed = 0;

    //truncate results file
    csv.newFile()

    if(urls !== null){
        csv.writeRow(WebSearchConfig.resultHeader)

        for(i in 1..WebSearchConfig.maxProcesses){ //launch co-routines
            go { browser.process(WebSearchConfig.searchTerm, browserChannel, quitChannel) }
        }

        for(url in urls){ //fill channel queue
           quitChannel.send(numProcessed)
           browserChannel.send(url)

           if(WebSearchConfig.verbose){
                println(url)
           }
           numProcessed++;
        }


        //using the quitChannel to block until any hanging URL requests finish
        for(i in 1.. numProcessed){
            quitChannel.send(0) //fill the rest of the quitChannel with zeros and wait until we find one
            if(WebSearchConfig.verbose){
                println(quitChannel)
            }

            if(quitChannel.viewBuffer.element() == 0){
                //close channels and exit
                quitChannel.close()
                browserChannel.close()
                println("Finished processing URLs.")
                break;
            }
        }


    } else {
        println("No URLs found to search.")
    }

}
