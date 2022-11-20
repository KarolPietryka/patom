package pl.patom.service.file.html.transformer

import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.alfresco.service.namespace.NamespaceService
import org.alfresco.service.namespace.QName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import pl.patom.model.content.PATOM_NAMESPACE_PREFIX
import pl.patom.model.properties.HTML_PROPERTY_PREFIX
import pl.patom.model.properties.HTML_PROPERTY_SUFFIX
import pl.patom.service.file.html.transformer.strategy.context.HtmlTransformStrategyContext
import java.io.Serializable

@Service
class HtmlTransformService @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    private val namespaceService: NamespaceService,
    @Qualifier("htmlTransformStrategyContext")
    private val htmlTransformStrategyContext: HtmlTransformStrategyContext
){
    fun modifyWithNodeProperties(htmlText: String, nodeRef: NodeRef): String{
        //var parsedHTMLText = htmlText.replace("font-size: 3px", "font-size: 10px")
        val parsedHTMLText = StringBuilder(htmlText)
        nodeService.getProperties(nodeRef)
            .filter (this::filterPatomProperties)
            .mapKeys (this::parsePropNameToHtmlForm)
            .map { Pair(it.key, it.value) }
            .forEach { it ->
                when(it.second){
                    (it.second is Boolean) -> htmlTransformStrategyContext.checkboxModifier.modify(it, parsedHTMLText.toString())
                    (it.second is String) -> htmlTransformStrategyContext.textModifier.modify(it, parsedHTMLText.toString())
                }
        }
        return parsedHTMLText.toString()
    }
    private fun filterPatomProperties(prop: Map.Entry<QName, Serializable>) =
        namespaceService.getPrefixes(prop.key.namespaceURI).any { it == PATOM_NAMESPACE_PREFIX }
    private fun parsePropNameToHtmlForm(prop: Map.Entry<QName, Serializable>) =
        HTML_PROPERTY_PREFIX + prop.key.toPrefixString(namespaceService) + HTML_PROPERTY_SUFFIX
}