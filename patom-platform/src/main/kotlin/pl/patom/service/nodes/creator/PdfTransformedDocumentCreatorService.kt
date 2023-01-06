package pl.patom.service.nodes.creator

import org.alfresco.model.ContentModel
import org.alfresco.repo.content.MimetypeMap
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import pl.patom.model.content.PDF_FORM_CHILD_ASSOC_QNAME
import pl.patom.model.properties.PDF_FORM_ENCODING
import pl.patom.service.nodes.navigator.MainNodesGetter
import pl.patom.service.path.DateTimePathCreator
import pl.patom.service.properties.site.PatomSiteProperties
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*

@Service
class PdfTransformedDocumentCreatorService @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    @Qualifier("ContentService") private val contentService: ContentService,
    private val mainNodesGetter: MainNodesGetter,
    private val patomSiteProperties: PatomSiteProperties,
    private val dateTimePathCreator: DateTimePathCreator,
    ){
     fun createPdfForm(outputPdfFilePath: Path): NodeRef {
        val pdfNodeRef = nodeService.createNode(
            dateTimePathCreator.createCurrentDatePath(mainNodesGetter.getPatomPdfFormsDir()),
            ContentModel.ASSOC_CONTAINS,
            PDF_FORM_CHILD_ASSOC_QNAME,
            ContentModel.TYPE_CONTENT,
            mapOf(ContentModel.PROP_NAME to "${patomSiteProperties.pdfFormNameBase}--${getCurrentDataTime()}")
        )
        val pdfFileContentWriter = contentService.getWriter(pdfNodeRef.childRef, ContentModel.PROP_CONTENT,true);
        pdfFileContentWriter.mimetype = MimetypeMap.MIMETYPE_PDF
        pdfFileContentWriter.encoding = PDF_FORM_ENCODING;
        pdfFileContentWriter.putContent(outputPdfFilePath.toFile())
        return pdfNodeRef.childRef
    }

    private fun getCurrentDataTime(): String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat(patomSiteProperties.pdfFormDocumentNamesDataTimeFormat)
        return formatter.format(time)
    }
}