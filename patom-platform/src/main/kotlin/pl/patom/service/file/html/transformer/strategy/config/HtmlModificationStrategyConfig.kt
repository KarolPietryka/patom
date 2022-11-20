package pl.patom.service.file.html.transformer.strategy.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.patom.context.html.modifier.HTMLModifier
import pl.patom.service.file.html.transformer.strategy.context.HtmlTransformStrategyContext

@Configuration
class HtmlModificationStrategyConfig constructor(
    @Qualifier("htmlTextModifier") private val htmlModifier: HTMLModifier,
    @Qualifier("htmlCheckboxModifier") private val htmlCheckboxModifier: HTMLModifier,
){
    @Bean("htmlTransformStrategyContext")
    fun htmlTransformStrategyContext() =
        HtmlTransformStrategyContext(htmlModifier, htmlCheckboxModifier)
}