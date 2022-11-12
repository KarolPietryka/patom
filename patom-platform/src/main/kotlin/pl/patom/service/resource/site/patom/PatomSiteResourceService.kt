package pl.patom.service.resource.site.patom

import org.alfresco.model.ContentModel
import org.alfresco.repo.content.MimetypeMap
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.patom.service.properties.resources.HtmlTemplateResourcesProperties
import pl.patom.service.resource.ResourceContentService

@Service
class PatomSiteResourceService @Autowired constructor(
    private val resourceContentService: ResourceContentService,
    private val contentService: ContentService,
    private val htmlTemplateResourcesProperties: HtmlTemplateResourcesProperties,

    ){
    fun putHtmlTemplateResourceContentToNode(htmlNodeRef: NodeRef){
        val contentWriter = contentService.getWriter(htmlNodeRef, ContentModel.PROP_CONTENT, true)
        contentWriter.mimetype = MimetypeMap.MIMETYPE_HTML
        contentWriter.putContent(
            resourceContentService.getContentInputStream(htmlTemplateResourcesProperties.htmlTemplateClassPath)
        )
    }
    fun putHtmlFacadeResourceContentToNode(pdfNodeRef: NodeRef){
        val contentWriter = contentService.getWriter(pdfNodeRef, ContentModel.PROP_CONTENT, true)
        contentWriter.mimetype = MimetypeMap.MIMETYPE_PDF
        contentWriter.putContent(
            resourceContentService.getContentInputStream(htmlTemplateResourcesProperties.pdfFacadeClassPath)
        )
    }
}