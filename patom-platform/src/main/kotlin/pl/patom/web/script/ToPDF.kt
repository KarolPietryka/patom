package pl.patom.web.script

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import mu.KotlinLogging
import org.alfresco.model.ContentModel
import org.alfresco.repo.nodelocator.NodeLocatorService
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.ContentWriter
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.json.JSONObject
import org.json.simple.parser.JSONParser
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.extensions.webscripts.AbstractWebScript
import org.springframework.extensions.webscripts.WebScriptRequest
import org.springframework.extensions.webscripts.WebScriptResponse
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component
import org.xhtmlrenderer.pdf.ITextRenderer;
import pl.patom.model.rest.request.ToPDFRequest
import java.io.OutputStream
import pl.patom.model.*

@Component("webscript.pl.patom.web.script.toPDF.post")
class ToPDF @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    @Qualifier("ContentService") private val contentService: ContentService,
    private val fileFolderService: FileFolderService,
    private val nodeLocatorService: NodeLocatorService,
    @Value("\${patom.htmlTemplates.path}") private val htmlTemplatesPaths: String,
    @Value("\${patom.general.pathSeparator}") private val patomPathSeparator: String
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
        var boas: ByteArrayOutputStream? = null
        val documentNodeRef = NodeRef(objectMapper.readValue(req.content.content, ToPDFRequest::class.java).documentNodeRef)
        try {
            val outputPDFFile =
                htmlTemplateName + Date().toString() + ".pdf"
            val templateNodeRef = fileFolderService.resolveNamePath(
                nodeLocatorService.getNode("companyhome", null, null),
                htmlTemplateFullPath).nodeRef
            val htmlTemplatePathContent = contentService.getReader(templateNodeRef, ContentModel.PROP_CONTENT)
            val document = Jsoup.parse(htmlTemplatePathContent.contentString)
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
            //document.body().select(".DOC_GENERATED_DATE").html(nodeService.getProperty(documentNodeRef, ContentModel.PROP_NAME) as String)
            renderer.setDocumentFromString(document.html())
            renderer.layout()

            val pdfFile = nodeService.createNode(
                fileFolderService.resolveNamePath(
                    nodeLocatorService.getNode("companyhome", null, null),
                    htmlTemplatesPaths.split(patomPathSeparator)
                ).nodeRef, ContentModel.ASSOC_CONTAINS, ContentModel.ASSOC_CONTAINS, TYPE_PATOM_DOCUMENT)
            val pdfFileContentWriter = contentService.getWriter(pdfFile.childRef, ContentModel.PROP_CONTENT,true);
            pdfFileContentWriter.mimetype = "application/pdf";
            val myNodeOut = pdfFileContentWriter.contentOutputStream
            try {
                renderer.createPDF(myNodeOut)
                myNodeOut.close()
//                Files.newOutputStream(Paths.get(outputPDFFile)).use { os ->
//                    renderer.createPDF(os)
//                    os.close()
                    //val reader = PdfReader(outputPDFFile)
                    //boas = ByteArrayOutputStream()
                    //val stamper = PdfStamper(reader, boas)
                    //stamper.setPageAction(PdfWriter.PAGE_OPEN, PdfAction(PdfAction.PRINTDIALOG), 1)
                    //stamper.close()
//                }
            } catch (ex: DocumentException) {
                LOG.error{ex}
            }
        } catch (ex: IOException) {
            LOG.error{ex}
        }
    }
}
