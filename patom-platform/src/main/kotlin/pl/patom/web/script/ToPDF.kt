package pl.patom.web.script

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.util.TempFileProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.extensions.webscripts.AbstractWebScript
import org.springframework.extensions.webscripts.WebScriptRequest
import org.springframework.extensions.webscripts.WebScriptResponse
import org.springframework.stereotype.Component
import pl.patom.model.rest.request.ToPDFRequest
import pl.patom.model.template.facade.HtmlFacadeModel
import pl.patom.service.properties.transform.ToPdfTransformationProperties
import pl.patom.service.properties.transform.ToPdfTransformationProperties.Companion.PROP_TRANSFORMATION_COMMAND
import pl.patom.service.file.content.ContentReadingService
import pl.patom.service.file.html.transformer.HtmlTransformService
import pl.patom.service.nodes.creator.HtmlFacadeNodeCreatorService
import pl.patom.service.nodes.creator.PdfTransformedDocumentCreatorService
import java.io.FileWriter
import java.io.InputStream
import java.nio.file.Path
import java.util.*

@Component("webscript.pl.patom.web.script.toPDF.post")
class ToPDF @Autowired constructor(
    private val contentReadingService: ContentReadingService,
    private val toPdfTransformationProperties: ToPdfTransformationProperties,
    private val htmlFacadeNodeCreatorService: HtmlFacadeNodeCreatorService,
    private val pdfTransformedDocumentCreatorService: PdfTransformedDocumentCreatorService,
    private val htmlTransformService: HtmlTransformService,
    ): AbstractWebScript() {

    companion object{
        val objectMapper = jacksonObjectMapper()
    }
    override fun execute(req: WebScriptRequest, res: WebScriptResponse?) {
        val htmlFacadeNodeRef = NodeRef(
            objectMapper.readValue(req.content.content, ToPDFRequest::class.java).documentNodeRef)

        val htmlFacade = htmlFacadeNodeCreatorService.getHtmlFacadeModel(htmlFacadeNodeRef)
        val outputPdfFilePath = getPdfEmptyFile()
        val inputHtmlFilePath = getHtmlTemplateFileWithContent(htmlFacade)

        val transformationCommand = generateWkHtmlToPdfCommand(inputHtmlFilePath, outputPdfFilePath)
        //Runs wkHtmlToPdf process with puts content into outputPdfFile
        ProcessBuilder(transformationCommand).start().waitFor()

        pdfTransformedDocumentCreatorService.createPdfForm(outputPdfFilePath)
    }

    private fun getPdfEmptyFile() =
        TempFileProvider.createTempFile(UUID.randomUUID().toString(), "pdf").toPath()
    private fun getHtmlTemplateFileWithContent(htmlFacade: HtmlFacadeModel): Path{
        val htmlFileContent = contentReadingService
            .getFileContentAsString(htmlFacade.htmlTemplateNodeRef)
            .let { htmlTransformService.modifyWithNodeProperties(it, htmlFacade.facadeNodeRef) }
        return TempFileProvider
            .createTempFile(htmlFileContent.byteInputStream(), UUID.randomUUID().toString(), ".html")
            .toPath()

    }
    private fun generateWkHtmlToPdfCommand(htmlFile: Path, pdfFile: Path) =
        mutableListOf(toPdfTransformationProperties.wkhtmltopdf)
            .apply {
                this.addAll(PROP_TRANSFORMATION_COMMAND)
                this.add(htmlFile.toString())
                this.add(pdfFile.toString())
            }

}
