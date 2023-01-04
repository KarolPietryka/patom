package pl.patom.service.file.html.transformer.strategy.context

import org.springframework.stereotype.Service
import pl.patom.service.file.html.transformer.strategy.modifier.HTMLModifier

class HtmlTransformStrategyContext constructor(
    val checkboxModifier: HTMLModifier,
    val textModifier: HTMLModifier,
    val dateModifier: HTMLModifier,
)