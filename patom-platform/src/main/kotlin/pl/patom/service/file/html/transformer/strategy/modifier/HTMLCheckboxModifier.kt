package pl.patom.service.file.html.transformer.strategy.modifier

import org.springframework.stereotype.Component
import pl.patom.model.template.content.checkbox.HtmlCheckboxState
import pl.patom.model.template.content.html.entity.HtmlEntity

@Component("htmlCheckboxModifier")
class HTMLCheckboxModifier: HTMLModifier {
    override fun modify(entityToReplace: HtmlEntity, htmlText: String): String {
        var htmlTextParsed = htmlText
        val checkBoxState = HtmlCheckboxState.fromBoolean(entityToReplace.acsPropValue as? Boolean ?: false)
        val propOccurIndex = htmlText.indexOf(entityToReplace.acsPropInHtmlNotation)
        while (htmlTextParsed.indexOf(entityToReplace.acsPropInHtmlNotation) != -1){
            //remove ">" before
            htmlTextParsed = htmlTextParsed.removeRange(propOccurIndex-1, propOccurIndex)
            htmlTextParsed = htmlTextParsed.replace(entityToReplace.acsPropInHtmlNotation, checkBoxState.htmlStringState + ">")
        }
        return htmlTextParsed
    }
}