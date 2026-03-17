import org.jsoup.Jsoup
import khttp.responses.Response
import java.net.URL


class Crawler(private val url : String) {
//    fun getResource() : khttp.responses.Response {
//        return  khttp.get(url)
//    }
    fun getResource() : String {
        return  URL(url).readText()
    }

    fun processContent(contentType : String){
        val response = getResource()


        var parser:Parser ? = null

        if(contentType.equals("json", ignoreCase = true)){
            parser = jsonParser()
        }

        if(contentType.equals("xml", ignoreCase = true)){
            parser = xmlParser()
        }

        val result = parser?.parse(response) ?: emptyMap()

        println("-----Date procesate ($contentType): ")
        result.forEach { (k,v) -> println("$k: $v") }

    }



}