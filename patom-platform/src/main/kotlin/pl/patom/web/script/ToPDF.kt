package pl.patom.web.script

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.alfresco.model.ContentModel
import org.alfresco.repo.content.MimetypeMap
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.alfresco.service.cmr.repository.StoreRef
import org.alfresco.util.TempFileProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.extensions.webscripts.AbstractWebScript
import org.springframework.extensions.webscripts.WebScriptRequest
import org.springframework.extensions.webscripts.WebScriptResponse
import org.springframework.stereotype.Component
import pl.patom.model.rest.request.ToPDFRequest
import pl.patom.model.TYPE_PATOM_DOCUMENT
import pl.patom.model.properties.transform.ToPdfTransformationProperties
import pl.patom.model.properties.transform.ToPdfTransformationProperties.Companion.PROP_TRANSFORMATION_COMMAND
import pl.patom.service.file.content.ContentReadingService
import pl.patom.service.nodes.navigator.MainNodesGetter
import java.io.FileWriter
import java.nio.file.Path
import java.util.*

@Component("webscript.pl.patom.web.script.toPDF.post")
class ToPDF @Autowired constructor(
    @Qualifier("NodeService") private val nodeService: NodeService,
    private val contentReadingService: ContentReadingService,
    @Qualifier("ContentService") private val contentService: ContentService,
    private val mainNodesGetter: MainNodesGetter,
    private val toPdfTransformationProperties: ToPdfTransformationProperties,
    ): AbstractWebScript() {

    companion object{
        val objectMapper = jacksonObjectMapper()
    }
    override fun execute(req: WebScriptRequest, res: WebScriptResponse?) {
        val htmlTemplateNodeRef = NodeRef(
            StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
            objectMapper.readValue(req.content.content, ToPDFRequest::class.java).documentNodeRef)

        val outputPdfFilePath = getPdfEmptyFile()
        val inputHtmlFilePath = getHtmlTemplateFileWithContent(htmlTemplateNodeRef)

        val transformationCommand = generateWkHtmlToPdfCommand(inputHtmlFilePath, outputPdfFilePath)
        //Runs wkHtmlToPdf process with puts content into outputPdfFile
        ProcessBuilder(transformationCommand).start().waitFor()

        createPdfNode(outputPdfFilePath)
    }

    private fun getPdfEmptyFile() = TempFileProvider.getTempDir().toPath()
        .resolve("${UUID.randomUUID()}.pdf")
        .apply {
            this.toFile()
        }
    private fun getHtmlTemplateFileWithContent(htmlTemplateNodeRef: NodeRef) = TempFileProvider.getTempDir().toPath()
        .resolve("${UUID.randomUUID()}.html")
        .apply {
            putTemplateContentIntoFile(htmlTemplateNodeRef, this)
        }
    private fun putTemplateContentIntoFile(templateNodeRef: NodeRef, htmlFilePath: Path){
        val htmlFileContent = contentReadingService.getFileContentAsString(templateNodeRef)
        FileWriter(htmlFilePath.toFile()).write(htmlFileContent)
    }
    private fun generateWkHtmlToPdfCommand(htmlFile: Path, pdfFile: Path) =
        mutableListOf(toPdfTransformationProperties.wkhtmltopdf)
            .apply {
                this.addAll(PROP_TRANSFORMATION_COMMAND)
                this.add(htmlFile.toString())
                this.add(pdfFile.toString())
            }

    private fun createPdfNode(outputPdfFilePath: Path) {
        val pdfNodeRef = nodeService.createNode(
            mainNodesGetter.getHtmlTemplateDir(),
            ContentModel.ASSOC_CONTAINS,
            ContentModel.ASSOC_CONTAINS,
            TYPE_PATOM_DOCUMENT
        )
        val pdfFileContentWriter = contentService.getWriter(pdfNodeRef.childRef, ContentModel.PROP_CONTENT,true);
        pdfFileContentWriter.mimetype = MimetypeMap.MIMETYPE_PDF
        pdfFileContentWriter.setEncoding("UTF-8");
        pdfFileContentWriter.putContent(outputPdfFilePath.toFile())
    }
}
