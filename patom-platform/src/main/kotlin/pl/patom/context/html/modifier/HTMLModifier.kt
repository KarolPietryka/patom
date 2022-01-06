package pl.patom.context.html.modifier

import mu.KotlinLogging
import java.io.Serializable

interface HTMLModifier {
    fun modify(replacePair: Pair<String, Serializable>, htmlText: String): String
}