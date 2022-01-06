package pl.patom.context.html.modifier

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class HTMLModifierContext @Autowired constructor(
    @Qualifier("htmlTextModifier") private val htmlModifier: HTMLModifier
){
}