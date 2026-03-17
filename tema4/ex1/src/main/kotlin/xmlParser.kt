import org.jsoup.Jsoup
import org.jsoup.parser.Parser as JsoupParser
class xmlParser : Parser {

    override fun parse(text: String): Map<String, Any>{
        val doc = Jsoup.parse(text,"",JsoupParser.xmlParser())
        val result = mutableMapOf<String, Any>()
        val elements = doc.select("*")

        for(element in elements){
            if (element.children().isEmpty() ){
                result[element.tagName()] = element.text()

            }
        }

        return result

    }

}