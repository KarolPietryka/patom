package pl.patom.service.file.content

import org.alfresco.model.ContentModel
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ContentReadingService @Autowired constructor(
    @Qualifier("ContentService") private val contentService: ContentService,

    )
{
    fun getFileContentAsString(file: NodeRef) =
        contentService.getReader(file, ContentModel.PROP_CONTENT).contentString

    fun getFileContentInputStream(file: NodeRef) =
    contentService.getReader(file, ContentModel.PROP_CONTENT).contentInputStream
}