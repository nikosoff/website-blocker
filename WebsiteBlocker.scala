import scala.io.Source
import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter
import java.io.PrintWriter
import java.io.FileNotFoundException
import java.util.Calendar
import java.lang.Thread

object WebsiteBlocker {

    def main(args: Array[String]) {
    
        val filename = "C:\\Windows\\System32\\drivers\\etc\\hosts"
        val redirect = "127.0.0.1"
        val websiteList = Source.fromFile("block_websites.txt").getLines.toList  
        var lowerHourBound = -1
        var upperHourBound = -1
        
        if (args.length == 2) {
            try {
                lowerHourBound = args(0).toInt
                upperHourBound = args(1).toInt
            } catch {
                case nfe: NumberFormatException => { 
                    println("The arguments must be integers")
                    System.exit(0)
                }
            }
        }
        else {
            println("You should give two arguments that specify the time interval for which websites must be blocked")
            println("For example: scala WebsiteBlocker.scala 11 16")
            System.exit(0)
        }
    
        while (true) {

            val now = Calendar.getInstance()
            val currentHour = now.get(Calendar.HOUR_OF_DAY)

            if (lowerHourBound <= currentHour && currentHour < upperHourBound) {
                
                try {

                    val content = Source.fromFile(filename).mkString
                    val outputFile = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename),true)))
            
            
                    websiteList.foreach(website => if (!content.contains(website)) 
                                                            outputFile.append(redirect + "\t" + website + "\n"))
                   
                    outputFile.close()
            
                } catch {
                    case fnf: FileNotFoundException => {
                            println("The file \"" + filename +"\" does not exist")
                            System.exit(0)
                        }
                }
            
            }
            else {
                
                try {
                    val lines = Source.fromFile(filename).getLines().toList
                    val outputFile = new PrintWriter(new BufferedWriter(new FileWriter(new File(filename))))
                    
                    lines.foreach(line => if (websiteList.forall(website => !line.contains(website))) 
                                                outputFile.write(line + "\n"))
         
                    outputFile.close()
                
                } catch {
                    case fnf: FileNotFoundException => {
                            println("The file \"" + filename + "\" does not exist")
                            System.exit(0)
                        }
                }
            }
            
            Thread.sleep(5000)

        }        


    }

}








