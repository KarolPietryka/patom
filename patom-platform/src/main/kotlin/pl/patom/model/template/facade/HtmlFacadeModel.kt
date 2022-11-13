package pl.patom.model.template.facade

import org.alfresco.service.cmr.repository.NodeRef

data class HtmlFacadeModel (
    val facadeNodeRef: NodeRef,
    val htmlTemplateNodeRef: NodeRef,
)
