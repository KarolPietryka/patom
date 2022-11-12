package pl.patom.service.properties.resources

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class HtmlTemplateResourcesProperties @Autowired constructor(
    @Value("\${patom.resources.html.template.classpath}")
    val htmlTemplateClassPath: String,
    @Value("\${patom.resources.html.template.facade.classpath}")
    val pdfFacadeClassPath: String,
){
}