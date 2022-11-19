package pl.patom.service.file.html.transformer

import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.alfresco.service.namespace.NamespaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.Serializable

@Service
class HTMLModifierService @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    private val namespaceService: NamespaceService
){
    companion object{
        val propArea_open = "\${"
        val propArea_close = "}"
    }
    fun modifyWithNodeProperties(htmlText: String, nodeRef: NodeRef): String{
        val nodeProperties = nodeService.getProperties(nodeRef)
        var parsedHTMLText = htmlText.replace("font-size: 3px", "font-size: 10px")
        nodeProperties
            .filter { prop -> namespaceService.getPrefixes(prop.key.namespaceURI).any { it == "pt" }}
            .mapKeys { propArea_open + it.key.toPrefixString(namespaceService) + propArea_close }
            .forEach { (propName, propValue) ->
                parsedHTMLText = doReplace(Pair(propName, propValue), parsedHTMLText)
        }
        return parsedHTMLText
    }
    private fun doReplace(replacePair:Pair<String, Serializable>, htmlText: String): String{
        return htmlText.replace(replacePair.first, replacePair.second?.toString() ?: "")
    }
}