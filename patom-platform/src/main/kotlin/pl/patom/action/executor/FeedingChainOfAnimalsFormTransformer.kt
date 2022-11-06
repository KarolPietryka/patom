package pl.patom.action.executor

import mu.KotlinLogging
import org.alfresco.model.ContentModel
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase
import org.alfresco.service.cmr.action.Action
import org.alfresco.service.cmr.action.ParameterDefinition
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.apache.pdfbox.pdmodel.PDDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import pl.patom.model.TYPE_PATOM_DOCUMENT

class FeedingChainOfAnimalsFormTransformer @Autowired constructor(
    private val nodeService: NodeService
): ActionExecuterAbstractBase() {

    companion object {
        private val log = KotlinLogging.logger { }
        const val NAME = "feeding-chain-of-animals-transformer"
        const val DESCRIPTION = "Transform to PDF version of feeding chain of animals form"
        const val TITLE = "Transform feeding chain of animals form"
    }

    override fun executeImpl(action: Action, nodeRef: NodeRef) {
        log.info("Executing action $NAME on node $nodeRef")
        nodeService.setType(nodeRef, TYPE_PATOM_DOCUMENT)
    }

    override fun addParameterDefinitions(paramList: MutableList<ParameterDefinition>?) {
    }
}
