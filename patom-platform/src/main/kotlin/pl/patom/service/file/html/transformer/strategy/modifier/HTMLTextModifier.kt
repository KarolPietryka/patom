package pl.patom.service.file.html.transformer.strategy.modifier

import mu.KotlinLogging
import org.springframework.stereotype.Component
import pl.patom.model.template.content.html.entity.HtmlEntity

@Component("htmlTextModifier")
class HTMLTextModifier: HTMLModifier {
    companion object{
        private val LOG = KotlinLogging.logger {  }
    }
    override fun modify(entityToReplace: HtmlEntity, htmlText: String): String =
        htmlText.replace(entityToReplace.acsPropInHtmlNotation, entityToReplace.acsPropValue as? String ?: "")

}