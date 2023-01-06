package pl.patom.service.file.html.transformer.strategy.modifier

import org.alfresco.repo.copy.CompoundCopyBehaviourCallback
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import pl.patom.model.template.content.html.entity.HtmlEntity
import java.text.SimpleDateFormat
import java.util.*

@Component("htmlDateModifier")
class HtmlDateModifier: HTMLModifier {
    private val logger = LogFactory.getLog(HtmlDateModifier::class.java)

    override fun modify(entityToReplace: HtmlEntity, htmlText: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd")
        logger.debug(entityToReplace.acsPropInHtmlNotation + entityToReplace.acsPropValue as? Date )
        return htmlText.replace(
            entityToReplace.acsPropInHtmlNotation,
            entityToReplace.acsPropValue?.let { df.format(it as Date) } ?: "") }
}