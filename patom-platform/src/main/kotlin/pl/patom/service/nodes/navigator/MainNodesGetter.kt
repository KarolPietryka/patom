package pl.patom.service.nodes.navigator

import org.alfresco.repo.nodelocator.NodeLocatorService
import org.alfresco.rest.api.model.Site
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.NodeService
import org.alfresco.service.cmr.site.SiteService
import org.alfresco.service.cmr.site.SiteVisibility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.patom.service.path.CustomPathsService
import pl.patom.service.site.SiteUtils

@Service
class MainNodesGetter @Autowired constructor(
    private val nodeLocatorService: NodeLocatorService,
    @Value("\${patom.site.template.html}") private val htmlTemplatesPaths: String,
    @Value("\${patom.mainTemplateName}") private val templateFileName: String,
    private val fileFolderService: FileFolderService,
    private val customPathsService: CustomPathsService,
    @Qualifier("NodeService") private val nodeService: NodeService,
    @Value("\${patom.site.name}") private val siteName: String,
    @Value("\${patom.site.id}") private val siteId: String,
    private val siteUtils: SiteUtils,
    private val siteService: SiteService,
    ) {
    companion object{
        private val companyhomeDirName = "companyhome"
    }
    fun getCompanyHome(): NodeRef =
        nodeLocatorService.getNode(companyhomeDirName, null, null)

    fun getPatomSite() = siteUtils.createSite(siteId, siteName, SiteVisibility.PUBLIC)

    fun getPatomHtmlTemplateDir(): NodeRef =
        fileFolderService.resolveNamePath(
            getPatomSiteDocumentLibrary(getPatomSite()),
            pathToPathElementsList(htmlTemplatesPaths)
        ).nodeRef

    fun getPatomSiteDocumentLibrary(site: Site): NodeRef {
        val patomSiteInfo = siteService.getSite(site.id)
        return fileFolderService.resolveNamePath(
            patomSiteInfo.nodeRef,
            pathToPathElementsList(SiteService.DOCUMENT_LIBRARY)
        ).nodeRef
    }
    private fun pathToPathElementsList(path: String) =
        customPathsService.customPathToPathElementsList(path)


}