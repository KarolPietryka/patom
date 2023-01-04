package pl.patom.model.template.content.html.entity

import org.alfresco.service.cmr.dictionary.DictionaryService
import org.alfresco.service.namespace.QName
import org.springframework.stereotype.Service

@Service
class HtmlEntityService (
    private val dictionaryService: DictionaryService
){
    fun getPropertyType(htmlEntity: HtmlEntity) =
        dictionaryService.getProperty(htmlEntity.acsProp).dataType

}