package pl.patom.model.template.content.checkbox

enum class HtmlCheckboxState(val htmlStringState: String){
    CHECKED("checked"),
    UNCHECKED("");

    companion object {
        fun fromBoolean(state: Boolean): HtmlCheckboxState =
            if (state) CHECKED
            else UNCHECKED
    }

}