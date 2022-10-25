package pl.patom.service.nodes.navigator

import org.alfresco.model.ContentModel
import org.alfresco.repo.nodelocator.NodeLocatorService
import org.alfresco.rest.api.model.Site
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.alfresco.service.cmr.search.SearchService
import org.alfresco.service.cmr.site.SiteInfo
import org.alfresco.service.cmr.site.SiteService
import org.alfresco.service.namespace.NamespaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.patom.patch.InitPatomMainSitePatch
import pl.patom.service.path.CustomPathsService

@Service
class MainNodesGetter @Autowired constructor(
    private val nodeLocatorService: NodeLocatorService,
    @Value("\${patom.htmlTemplates.path}") private val htmlTemplatesPaths: String,
    @Value("\${patom.mainTemplateName}") private val templateFileName: String,
    private val fileFolderService: FileFolderService,
    private val customPathsService: CustomPathsService,
    @Qualifier("NodeService") private val nodeService: NodeService,
    private val siteService: SiteService,
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

    fun getSiteDocumentLibrary(site: Site): NodeRef {
        val patomSiteInfo = siteService.getSite(site.id)
        return fileFolderService.resolveNamePath(
            patomSiteInfo.nodeRef,
            pathToPathElementsList(SiteService.DOCUMENT_LIBRARY)
        ).nodeRef
    }
    fun getHtmlTemplate(): NodeRef =
        nodeService.getChildByName(
            getHtmlTemplateDir(),
            ContentModel.ASSOC_CONTAINS,
            templateFileName
        )
    private fun pathToPathElementsList(path: String) =
        customPathsService.customPathToPathElementsList(path)


}