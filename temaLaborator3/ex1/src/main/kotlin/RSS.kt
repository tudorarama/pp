import org.jsoup.Jsoup
import org.jsoup.parser.Parser

data class RssItem(val title: String,val link:String,val description:String)
data class RssFeed(val title: String,val link: String,val description: String,val pubDate: String,val items: List<RssItem>)

fun main() {
    val url = "http://rss.cnn.com/rss/edition.rss"

    try{

        val doc=Jsoup.connect(url).parser(Parser.xmlParser()).get()
        val channel = doc.selectFirst("channel")

        val feedTitle = channel?.selectFirst("title")?.text() ?:""
        val feedLink = channel?.selectFirst("link")?.text() ?:""
        val feedDesc = channel?.selectFirst("description")?.text() ?:""
        val feedPubDate = channel?.selectFirst("pubDate")?.text() ?:""

        val itemsList = mutableListOf<RssItem>()
        val itemElements = doc.select("item")

        for(element in itemElements){
            val itemTitle = element.selectFirst("title")?.text() ?:""
            val itemLink = element.selectFirst("link")?.text() ?:""
            val itemDesc = element.selectFirst("description")?.text() ?:""
            itemsList.add(RssItem(itemTitle,itemLink,itemDesc))
        }


        val myRssFeed = RssFeed(feedTitle,feedLink,feedDesc,feedPubDate,itemsList)

        println(myRssFeed.title + " | " + myRssFeed.pubDate)
        println()


        for(item in myRssFeed.items ) {
            println("Titlu : " + item.title)
            println("Link : " + item.link)
            println("Descriere : " + item.description)
            println()
        }

    }
    catch (e: Exception){
        println("Eroare : "+e.message)
    }
}

