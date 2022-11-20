package pl.patom.context.html.modifier

import java.io.Serializable

interface HTMLModifier {
    fun modify(replacePair: Pair<String, Serializable>, htmlText: String): String
}