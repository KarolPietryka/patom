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
    @Value("\${patom.site.root.folder}") private val workspaceDir: String,
    private val fileFolderService: FileFolderService,
    private val customPathsService: CustomPathsService,
    @Value("\${patom.site.id}") private val siteId: String,
    private val siteUtils: SiteUtils,
    private val siteService: SiteService,
    ) {
    companion object{
        private val companyhomeDirName = "companyhome"
    }
    fun getCompanyHome(): NodeRef =
        nodeLocatorService.getNode(companyhomeDirName, null, null)
    fun getPatomSite() = siteUtils.getSite(siteId)

    fun getPatomWorkspaceDir(): NodeRef =
        fileFolderService.resolveNamePath(
            getPatomSiteDocumentLibrary(getPatomSite()),
            pathToPathElementsList(workspaceDir)
        ).nodeRef
    fun getPatomHtmlTemplateDir(): NodeRef =
        fileFolderService.resolveNamePath(
            getPatomWorkspaceDir(),
            pathToPathElementsList(htmlTemplatesPaths)
        ).nodeRef

    fun getPatomSiteDocumentLibrary(site: Site): NodeRef =
        fileFolderService.resolveNamePath(
            siteService.getSite(getPatomSite().id).nodeRef,
            pathToPathElementsList(SiteService.DOCUMENT_LIBRARY)
        ).nodeRef

    private fun pathToPathElementsList(path: String) =
        customPathsService.customPathToPathElementsList(path)


}