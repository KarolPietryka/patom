package pl.patom.patch.dir.html.template

import org.springframework.beans.factory.annotation.Autowired
import pl.patom.patch.PatomAbstractPatch
import pl.patom.service.nodes.creator.HtmlFacadeNodeCreatorService


class AddHtmlFacadeToTemplateDirPatch @Autowired constructor(
    private val htmlFacadeNodeCreatorService: HtmlFacadeNodeCreatorService
) : PatomAbstractPatch(){

    companion object{
        const val PATCH_NAME = "AddHtmlFacadeToTemplateDirPatch"
    }
    override val patchName: String = PATCH_NAME

    override fun execute()  {
        htmlFacadeNodeCreatorService.createHtmlFacadeInPatom()
    }
}