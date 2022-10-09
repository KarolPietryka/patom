package pl.patom.web.script

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itextpdf.text.DocumentException;
import mu.KotlinLogging
import org.alfresco.model.ContentModel
import org.alfresco.repo.nodelocator.NodeLocatorService
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.extensions.webscripts.AbstractWebScript
import org.springframework.extensions.webscripts.WebScriptRequest
import org.springframework.extensions.webscripts.WebScriptResponse
import org.springframework.stereotype.Component
import org.xhtmlrenderer.pdf.ITextRenderer;
import pl.patom.HTMLModifierService
import pl.patom.model.rest.request.ToPDFRequest
import pl.patom.model.TYPE_PATOM_DOCUMENT
import pl.patom.service.file.content.ContentReadingService
import pl.patom.service.file.locator.MainNodesGetter

@Component("webscript.pl.patom.web.script.toPDF.post")
class ToPDF @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    @Qualifier("ContentService") private val contentService: ContentService,
    @Value("\${patom.htmlTemplates.path}") private val htmlTemplatesPaths: String,
    @Value("\${patom.general.pathSeparator}") private val patomPathSeparator: String,
    private val htmlModifierService: HTMLModifierService,
    private val mainNodesGetter: MainNodesGetter,
    private val contentReadingService: ContentReadingService
)
: AbstractWebScript() {
    private val htmlTemplateFullPath : List<String>
    init {
        htmlTemplateFullPath = htmlTemplatesPaths
            .split(patomPathSeparator)
            .toMutableList()
            .apply {  add(htmlTemplateName) }
    }
    companion object{
        val LOG = KotlinLogging.logger {  }
        val objectMapper = jacksonObjectMapper()
        const val htmlTemplateName = "template.html"
    }
    override fun execute(req: WebScriptRequest, res: WebScriptResponse) {
        val renderer = ITextRenderer()
        val documentNodeRef = NodeRef(objectMapper.readValue(req.content.content, ToPDFRequest::class.java).documentNodeRef)
        try {
            val templateNodeRef = mainNodesGetter.getHtmlTemplate()
            val template = Jsoup.parse(contentReadingService.getFileContentAsString(templateNodeRef))
            template.outputSettings().syntax(Document.OutputSettings.Syntax.html)
            //Jaka forma jest tutaj w temaplete.html? czy tu mamy jeszcze polskie znaki?
            val modifiedHTML = htmlModifierService.modifyWithNodeProperties(template.html(), documentNodeRef)
            renderer.setDocumentFromString(modifiedHTML)
            renderer.layout()
            //renderer.fontResolver.addFont(BaseFont.TIMES_ROMAN, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//Spróbój ustawić czcionke taką jaką mają w tych 14 w koncu to resolver
            //Sprawdź kiedy jest nadawana czcionka dl renderera
            val pdfFile = saveAsPdfFile()
            val myNodeOut = contentService.getWriter(pdfFile, ContentModel.PROP_CONTENT, true).contentOutputStream
            try {
                renderer.createPDF(myNodeOut)
                myNodeOut.close()
            } catch (ex: DocumentException) {
                LOG.error{ex}
            }
        } catch (ex: IOException) {
            LOG.error{ex}
        }
    }

    private fun saveAsPdfFile(): NodeRef{
        val pdfFile = nodeService.createNode(
            mainNodesGetter.getHtmlTemplateDir(),
            ContentModel.ASSOC_CONTAINS,
            ContentModel.ASSOC_CONTAINS,
            TYPE_PATOM_DOCUMENT)
        val pdfFileContentWriter = contentService.getWriter(pdfFile.childRef, ContentModel.PROP_CONTENT,true);
        pdfFileContentWriter.mimetype = "application/pdf";
        return pdfFile.childRef
    }
}
