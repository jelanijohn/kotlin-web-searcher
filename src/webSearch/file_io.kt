package webSearch;

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.FileWriter


/**
* CSV class
*
* This class is used for reading and writing to a csv file
*
*/
class CSV {
    /**
    * reads a column from [searchFile] at column index [urlIndex]
    *
    * @returns ArrayList<String> of column results
    */
    public fun readColumn(searchFile: String = WebSearchConfig.searchFile, urlIndex:Int = WebSearchConfig.searchFileUrlIndex): ArrayList<String>{
        var fileReader: BufferedReader? = null
        val urls = ArrayList<String>()

        try {
            var line: String?

            fileReader = BufferedReader(FileReader(searchFile))

            // Read CSV header
            fileReader.readLine()

            // Read the file line by line starting from the second line
            line = fileReader.readLine()
            while (line != null) {
              val items = line.split(",")
              if (items.size > urlIndex) {
                urls.add(items[urlIndex].replace("\"",""))
              }

              line = fileReader.readLine()
            }

        } catch (e: Exception) {
            println("Error Reading CSV")
            e.printStackTrace()
        } finally {
            try {
              fileReader!!.close()
            } catch (e: IOException) {
                println("Error closing fileReader")
                e.printStackTrace()
            }

            return urls;
        }
    }

    /**
    * Writes a row [csvRow] tp [outputFile]
    *
    */
    public fun writeRow(csvRow: String, outputFile: String = WebSearchConfig.outputFile): Unit {
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(outputFile, true) //2nd argument true to append

            fileWriter.append(csvRow + '\n')
        } catch (e: Exception) {
            println("Error Writing CSV")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Error closing/flushing fileReader")
                e.printStackTrace()
            }
        }
    }

    /**
    * Truncates or creates a new file [outputFile]
    *
    */
     public fun newFile(outputFile: String = WebSearchConfig.outputFile): Unit {
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(outputFile)
        } catch (e: Exception) {
            println("Error Creating/Truncating CSV")
            e.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Error closing/flushing fileReader")
                e.printStackTrace()
            }
        }
    }
}