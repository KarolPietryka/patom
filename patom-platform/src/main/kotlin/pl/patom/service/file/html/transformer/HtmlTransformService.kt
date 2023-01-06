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
import pl.patom.model.template.content.html.entity.HtmlEntity
import pl.patom.service.file.html.transformer.strategy.context.HtmlTransformStrategyContext
import java.io.Serializable
import java.util.*
import org.alfresco.service.cmr.dictionary.DataTypeDefinition.*
import org.alfresco.service.cmr.dictionary.DictionaryService
import pl.patom.model.content.ASPECT_PATOM_FACADE
import pl.patom.model.template.content.html.entity.HtmlEntityService
import java.util.zip.DataFormatException

@Service
class HtmlTransformService @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    private val namespaceService: NamespaceService,
    @Qualifier("htmlTransformStrategyContext")
    private val htmlTransformStrategyContext: HtmlTransformStrategyContext,
    private val htmlEntityService: HtmlEntityService,
    private val dictionaryService: DictionaryService

){
    fun modifyWithNodeProperties(htmlText: String, nodeRef: NodeRef): String{
        val nodeProperties = nodeService.getProperties(nodeRef)
        var parsedHtmlText = htmlText
        dictionaryService.getAspect(ASPECT_PATOM_FACADE).properties
            .mapValues { nodeProperties[it.value.name] }
            .mapKeys (this::parsePropNameToHtmlForm)
            .map { HtmlEntity(it.key, it.value) }
            .forEach {
                parsedHtmlText =
                    when (htmlEntityService.getPropertyType(it).name) {
                        BOOLEAN -> htmlTransformStrategyContext.checkboxModifier.modify(it, parsedHtmlText)
                        TEXT -> htmlTransformStrategyContext.textModifier.modify(it, parsedHtmlText)
                        DATE -> htmlTransformStrategyContext.dateModifier.modify(it, parsedHtmlText)
                        else -> {
                            throw DataFormatException(
                                "Property ${it.acsPropInHtmlNotation} has unknown type ${it.acsProp.localName}" +
                                        " in terms of HTML transformation"
                            )
                        }
                    }

        }
        return parsedHtmlText
    }
    private fun filterPatomProperties(prop: Map.Entry<QName, Serializable>) =
        namespaceService.getPrefixes(prop.key.namespaceURI).any { it == PATOM_NAMESPACE_PREFIX }
    private fun parsePropNameToHtmlForm(prop: Map.Entry<QName, Serializable?>) =
        HTML_PROPERTY_PREFIX + prop.key.toPrefixString(namespaceService) + HTML_PROPERTY_SUFFIX
}