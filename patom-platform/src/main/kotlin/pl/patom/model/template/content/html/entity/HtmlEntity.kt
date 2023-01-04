package pl.patom.model.template.content.html.entity

import org.alfresco.service.namespace.QName
import org.alfresco.service.namespace.QName.NAMESPACE_PREFIX
import pl.patom.model.content.PATOM_NAMESPACE_PREFIX
import pl.patom.model.content.PATOM_NAMESPACE_PREFIX_URI
import pl.patom.model.properties.HTML_PROPERTY_PREFIX
import pl.patom.model.properties.HTML_PROPERTY_SUFFIX
import java.io.Serializable

data class HtmlEntity(
    val acsPropInHtmlNotation: String,
    val acsPropValue: Serializable?
){
    val acsProp: QName = QName.createQName(PATOM_NAMESPACE_PREFIX_URI, acsPropInHtmlNotation.let {
        return@let it.removePrefix(HTML_PROPERTY_PREFIX + PATOM_NAMESPACE_PREFIX + NAMESPACE_PREFIX).removeSuffix(HTML_PROPERTY_SUFFIX)
    })
}