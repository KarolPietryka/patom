package pl.patom.context.html.modifier

import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.io.Serializable

@Component("htmlTextModifier")
class HTMLTextModifier: HTMLModifier{
    companion object{
        private val LOG = KotlinLogging.logger {  }
    }
    override fun modify(replacePair: Pair<String, Serializable>, htmlText: String): String =
        htmlText.replace(replacePair.first, replacePair.second as String)

}