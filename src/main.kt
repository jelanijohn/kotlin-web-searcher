package main;

import channel.*;
import webSearch.*;

fun main(args: Array<String>) = mainBlocking()  { //chose not to do command line arguments, options come from webSearch/config file

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


        //using the quitChannel to block until the other processes finish
        for(i in 1.. numProcessed){
            quitChannel.send(0) //fill the rest of the quitChannel with zeros and wait until we find one
            if(WebSearchConfig.verbose){
                println(quitChannel)
            }
            println(quitChannel)
            if(quitChannel.viewBuffer.element() == 0){
                //close channels and exit
                quitChannel.close()
                browserChannel.close()
                break;
            }
        }


    } else {
        println("No URLs found to search.")
    }

}
