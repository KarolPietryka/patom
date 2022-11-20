package pl.patom.service.file.html.transformer.strategy.modifier

import java.io.Serializable

interface HTMLModifier {
    fun modify(replacePair: Pair<String, Serializable>, htmlText: String): String
}