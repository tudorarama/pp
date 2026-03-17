fun main(){
    val urlJson = "https://jsonplaceholder.typicode.com/todos/1"

    val crawler = Crawler(urlJson)

    println("--- Testare JSON ---")
    crawler.processContent("json")

    val crawlerXml = Crawler("https://www.w3schools.com/xml/note.xml")
    crawlerXml.processContent("xml")
    println("\n--- Testare Format Necunoscut ---")
    crawler.processContent("txt")

}