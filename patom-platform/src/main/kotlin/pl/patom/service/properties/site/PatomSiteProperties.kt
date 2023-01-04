package pl.patom.service.properties.site

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PatomSiteProperties @Autowired constructor(
    @Value("\${patom.const.html.template.facade.name}") val htmlTemplateFacadeName: String,
    @Value("\${patom.const.pdf.form.name.base}") val pdfFormNameBase: String,
    @Value("\${patom.transformation.pdf.data.time.format}") val pdfFormDocumentNamesDataTimeFormat: String,
    @Value("\${patom.site.forms.pdf.dir.name}") val pdfFormsDirectoryName: String,
    @Value("\${patom.site.template.html.dir.name}") val htmlTemplatesDirectoryName: String,
    @Value("\${patom.site.template.html.workshop.dir.name}") val htmlTemplatesWorkshopDirectoryName: String,
){
}