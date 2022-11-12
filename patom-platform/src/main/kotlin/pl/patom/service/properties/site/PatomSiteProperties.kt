package pl.patom.service.properties.site

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PatomSiteProperties @Autowired constructor(
    @Value("\${patom.const.html.template.facade.name}") val htmlTemplateFacadeName: String
){
}