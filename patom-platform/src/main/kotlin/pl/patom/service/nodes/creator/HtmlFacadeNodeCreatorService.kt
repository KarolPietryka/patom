package pl.patom.service.nodes.creator

import org.alfresco.model.ContentModel
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import pl.patom.model.content.HTML_TEMPLATE_CHILD_ASSOC_QNAME
import pl.patom.model.content.HTML_TEMPLATE_FACADE_CHILD_ASSOC_QNAME
import pl.patom.model.content.TYPE_PATOM_DOCUMENT
import pl.patom.service.nodes.navigator.MainNodesGetter
import pl.patom.service.properties.site.PatomSiteProperties
import pl.patom.service.resource.site.patom.PatomSiteResourceService

@Service
class HtmlFacadeNodeCreatorService @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    private val mainNodesGetter: MainNodesGetter,
    private val patomSiteProperties: PatomSiteProperties,
    private val patomSiteResourceService: PatomSiteResourceService
    ){

    fun createHtmlFacadeInPatom(){
        val pdfEmptyNode = createEmptyHtmlFacadeNode()
        val htmlTemplateEmptyNode = createEmptyHtmlTemplateNode(pdfEmptyNode)
        patomSiteResourceService.putHtmlTemplateResourceContentToNode(htmlTemplateEmptyNode)
        patomSiteResourceService.putHtmlFacadeResourceContentToNode(pdfEmptyNode)
    }

    private fun createEmptyHtmlFacadeNode() =
        nodeService.createNode(
            mainNodesGetter.getPatomHtmlTemplateDir(),
            ContentModel.ASSOC_CONTAINS,
            HTML_TEMPLATE_FACADE_CHILD_ASSOC_QNAME,
            TYPE_PATOM_DOCUMENT,
            mapOf(
                ContentModel.PROP_NAME to patomSiteProperties.htmlTemplateFacadeName
            )).childRef

    private fun createEmptyHtmlTemplateNode(htmlFacadeNodeRef: NodeRef) =
        nodeService.createNode(
            htmlFacadeNodeRef,
            ContentModel.ASSOC_CONTAINS,
            HTML_TEMPLATE_CHILD_ASSOC_QNAME,
            ContentModel.TYPE_CONTENT
        ).childRef


}