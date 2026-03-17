import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.parser.Parser as JsoupParser


class jsonParser : Parser {

    override fun parse(text:String):Map<String,Any>{
        println("Se parseaza continutul JSON");



        val jsonObject = JSONObject(text)
        val result = mutableMapOf<String,Any>()

        val  keys = jsonObject.keys()
        while(keys.hasNext()) {
            val key = keys.next()
            result[key] = jsonObject.get(key)
        }
        return result




    }


}