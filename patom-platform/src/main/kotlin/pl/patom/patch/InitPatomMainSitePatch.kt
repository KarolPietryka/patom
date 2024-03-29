package pl.patom.patch

import org.alfresco.model.ContentModel
import org.alfresco.repo.admin.patch.AbstractPatch
import org.alfresco.repo.security.authentication.AuthenticationUtil
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.site.SiteVisibility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import pl.patom.service.nodes.navigator.MainNodesGetter
import pl.patom.service.properties.site.PatomSiteProperties
import pl.patom.service.site.SiteUtils

class InitPatomMainSitePatch @Autowired constructor(
    @Value("\${patom.site.name}") private val siteName: String,
    @Value("\${patom.site.id}") private val siteId: String,
    @Value("\${patom.site.root.folder}") private val workspaceDir: String,
    private val patomSiteProperties: PatomSiteProperties,
    ): AbstractPatch()  {
    @Autowired
    private lateinit var siteUtils: SiteUtils
    @Autowired
    private lateinit var fileFolderService: FileFolderService
    @Autowired
    private lateinit var mainNodesGetter: MainNodesGetter
    companion object{
        const val PATCH_NAME = "InitPatomMainSitePatch"
    }
    override fun applyInternal(): String {
        AuthenticationUtil.setFullyAuthenticatedUser(AuthenticationUtil.getAdminUserName())
        val patomSite = siteUtils.createSite(siteId, siteName, SiteVisibility.PUBLIC)
        val siteDocumentLibrary: NodeRef = mainNodesGetter.getPatomSiteDocumentLibrary(patomSite)
        createWorkspaceDir(siteDocumentLibrary).also { workspaceDir ->
            createHtmlTemplatesDir(workspaceDir).also { htmlTemplatesDir ->
                createHtmlWorkshopDir(htmlTemplatesDir)
            }
            createPdfFormsDir(workspaceDir)
        }
        return "$PATCH_NAME $APPLIED"
    }
    private fun createWorkspaceDir(siteDocumentLibrary: NodeRef):NodeRef = fileFolderService.create(
        siteDocumentLibrary,
        workspaceDir,
        ContentModel.TYPE_FOLDER).nodeRef
    private fun createHtmlTemplatesDir(siteWorkspaceDir: NodeRef) = fileFolderService.create(
        siteWorkspaceDir,
        patomSiteProperties.htmlTemplatesDirectoryName,
        ContentModel.TYPE_FOLDER).nodeRef
    private fun createPdfFormsDir(siteWorkspaceDir: NodeRef) = fileFolderService.create(
        siteWorkspaceDir,
        patomSiteProperties.pdfFormsDirectoryName,
        ContentModel.TYPE_FOLDER).nodeRef
    private fun createHtmlWorkshopDir(htmlTemplatesDir: NodeRef) = fileFolderService.create(
        htmlTemplatesDir,
        patomSiteProperties.htmlTemplatesWorkshopDirectoryName,
        ContentModel.TYPE_FOLDER).nodeRef

}