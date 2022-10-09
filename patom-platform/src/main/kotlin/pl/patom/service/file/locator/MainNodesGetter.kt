package pl.patom.service.file.locator

import org.alfresco.model.ContentModel
import org.alfresco.repo.nodelocator.NodeLocatorService
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.patom.service.path.CustomPathsService

@Service
class MainNodesGetter @Autowired constructor(
    private val nodeLocatorService: NodeLocatorService,
    @Value("\${patom.htmlTemplates.path}") private val htmlTemplatesPaths: String,
    @Value("\${patom.mainTemplateName}") private val templateFileName: String,
    private val fileFolderService: FileFolderService,
    private val customPathsService: CustomPathsService,
    @Qualifier("NodeService") private val nodeService: NodeService,
    ) {
    companion object{
        private val companyhomeDirName = "companyhome"
    }
    fun getCompanyHome(): NodeRef =
        nodeLocatorService.getNode(companyhomeDirName, null, null)

    fun getHtmlTemplateDir(): NodeRef =
        fileFolderService.resolveNamePath(
            getCompanyHome(),
            pathToPathElementsList(htmlTemplatesPaths)
        ).nodeRef

    fun getHtmlTemplate(): NodeRef =
        nodeService.getChildByName(
            getHtmlTemplateDir(),
            ContentModel.ASSOC_CONTAINS,
            templateFileName
        )
    private fun pathToPathElementsList(path: String) =
        customPathsService.customPathToPathElementsList(path)


}