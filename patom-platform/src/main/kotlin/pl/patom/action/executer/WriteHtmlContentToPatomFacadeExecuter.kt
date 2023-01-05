package pl.patom.action.executer

import org.alfresco.model.ContentModel
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase
import org.alfresco.repo.content.MimetypeMap
import org.alfresco.service.cmr.action.Action
import org.alfresco.service.cmr.action.ParameterDefinition
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.apache.commons.logging.LogFactory
import pl.patom.service.file.content.ContentReadingService
import pl.patom.service.nodes.creator.HtmlFacadeNodeCreatorService
import pl.patom.service.nodes.navigator.MainNodesGetter

class WriteHtmlContentToPatomFacadeExecuter constructor(
    private val contentService: ContentService,
    private val contentReadingService: ContentReadingService,
    private val mainNodesGetter: MainNodesGetter,
    private val htmlFacadeNodeCreatorService: HtmlFacadeNodeCreatorService,
    private val nodeService: NodeService,

    ): ActionExecuterAbstractBase() {
    private val logger = LogFactory.getLog(WriteHtmlContentToPatomFacadeExecuter::class.java)
     companion object{
         const val NAME = "writeHtmlContentToPatomFacadeExecuter"
     }

    override fun addParameterDefinitions(paramList: MutableList<ParameterDefinition>?) {
    }

    override fun executeImpl(action: Action?, actionedUponNodeRef: NodeRef?) {
        val newHtmlContent = contentReadingService.getFileContentAsString(actionedUponNodeRef!!)
        val htmlFacadeTemplate = htmlFacadeNodeCreatorService.getHtmlFacadeModel(
            mainNodesGetter.getPatomHtmlFacade()
        ).htmlTemplateNodeRef
        val contentWriter = contentService.getWriter(htmlFacadeTemplate, ContentModel.PROP_CONTENT, true)
        contentWriter.mimetype = MimetypeMap.MIMETYPE_HTML
        contentWriter.putContent(newHtmlContent)
        nodeService.deleteNode(actionedUponNodeRef)
    }
}