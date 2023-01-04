package pl.patom.service.file.html.transformer.strategy.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.patom.service.file.html.transformer.strategy.modifier.HTMLModifier
import pl.patom.service.file.html.transformer.strategy.context.HtmlTransformStrategyContext

@Configuration
class HtmlModificationStrategyConfig constructor(
    @Qualifier("htmlTextModifier") private val htmlTextModifier: HTMLModifier,
    @Qualifier("htmlCheckboxModifier") private val htmlCheckboxModifier: HTMLModifier,
    @Qualifier("htmlDateModifier") private val htmlDateModifier: HTMLModifier,
){
    @Bean("htmlTransformStrategyContext")
    fun htmlTransformStrategyContext() =
        HtmlTransformStrategyContext(
            checkboxModifier = htmlCheckboxModifier,
            textModifier = htmlTextModifier,
            dateModifier = htmlDateModifier
        )
}