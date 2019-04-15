# Website Searcher

Given a list of URLs, this program fetches each one and searches the body for a search term.
The results are written out into a file.


#### Notes

- This was my first time using Kotlin. As a language I found it fairly fun to pick up, I went through some of the Koans on their website before diving in.
- One issue I ran into involved channels and co-routines. I ran into an issue where the channel "browserChannel" was being consumed and the program
was ending while the coroutines were still waiting on URL requests to return.
  
  The way I ended up handling this was by introducing another channel "quitChannel" that kept track of which requests were processed and then closed both channels once everything finished.
- There is a config object, see below.
- Some of the urls take a while to run, usually due to a gateway or ssl timeout.
I thought of implementing an internal request timeout, but left it as is I feel its useful to see the actual timeout reason when the verbose flag is set.
 
 
### Install and Run

The lastest .jar is included so you can probably skip this step.

This was created using `Kotlin 1.3 (https://kotlinlang.org/docs/tutorials/command-line.html)` and can be run using java from the command line using the following steps.

- From the root directory create the package with 

`kotlinc src/* -include-runtime -d websiteSearcher.jar`

- Then run with

`java -jar websiteSearcher.jar`

### Input

Located in `data/urls.txt` in csv format. 

### Output

Will generate a file `results.txt` in csv format.
The file columns will be URL, Search Term, Result (true/false/error)

### Config

The config object is located in `src/webSearch/config.kt`
 
 
##### Config Object 

| variable | default value | description |
| --- | --- | --- |
| maxProcesses | _20_  | the maximum number of concurrent HTTP requests |
| searchTerm |_"html"_ | the current term being searched for |
| searchFile |_"data/urls.txt"_ | location of input file |
| searchFileUrlIndex |_20_ | the maximum number of concurrent HTTP |  
| urlPrefix | _"https://www."_ | protocol and subdomain to prefix to url string before fetching body |  
| outputFile |_"results.txt"_  |name of output file |
| resultHeader |_"URL,Search Term,Result"_ | the CSV headers for output file |
| verbose |_true_  | flag to print debugging information |