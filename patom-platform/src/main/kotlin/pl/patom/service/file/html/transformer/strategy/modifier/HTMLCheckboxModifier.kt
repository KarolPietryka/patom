package pl.patom.service.file.html.transformer.strategy.modifier

import org.springframework.stereotype.Component
import java.io.Serializable

@Component("htmlCheckBoxModifier")
class HTMLCheckboxModifier: HTMLModifier {
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