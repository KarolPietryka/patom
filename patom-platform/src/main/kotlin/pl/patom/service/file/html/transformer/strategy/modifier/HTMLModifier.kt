package pl.patom.service.file.html.transformer.strategy.modifier

import pl.patom.model.template.content.html.entity.HtmlEntity

interface HTMLModifier {
    fun modify(entityToReplace: HtmlEntity, htmlText: String): String
}