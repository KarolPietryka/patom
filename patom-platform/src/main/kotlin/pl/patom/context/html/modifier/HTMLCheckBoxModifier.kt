package pl.patom.context.html.modifier

import org.springframework.stereotype.Component
import java.io.Serializable

@Component("htmlCheckBoxModifier")
class HTMLCheckBoxModifier: HTMLModifier {
    enum class CheckBoxState(val stringState: String){
        CHECKED("checked"),
        UNCHECKED("")
    }
    override fun modify(replacePair: Pair<String, Serializable>, htmlText: String): String {
        val checkBoxState =
            when((replacePair.second as String).toBoolean()) {
                true -> CheckBoxState.CHECKED
                false -> CheckBoxState.UNCHECKED
            }
        return htmlText.replace(replacePair.first, checkBoxState.stringState)
    }
}