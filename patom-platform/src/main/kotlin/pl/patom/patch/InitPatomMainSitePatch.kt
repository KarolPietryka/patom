package pl.patom.patch

import org.alfresco.model.ContentModel
import org.alfresco.repo.admin.patch.AbstractPatch
import org.alfresco.repo.security.authentication.AuthenticationUtil
import org.alfresco.service.cmr.action.ActionService
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.rule.RuleService
import org.alfresco.service.cmr.search.SearchService
import org.alfresco.service.cmr.site.SiteService
import org.alfresco.service.cmr.site.SiteVisibility
import org.alfresco.service.namespace.NamespaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import pl.patom.service.nodes.navigator.MainNodesGetter
import pl.patom.service.site.SiteUtils

class InitPatomMainSitePatch @Autowired constructor(
    @Value("\${patom.site.name}") private val siteName: String,
    @Value("\${patom.site.id}") private val siteId: String,
    @Value("\${patom.site.root.folder}") private val workspaceDir: String,
    @Value("\${patom.site.template.html}") private val htmlTemplatesDir: String,
    @Value("\${patom.site.forms.pdf}") private val pdfFormsDir: String,
    ): AbstractPatch()  {
    @Autowired
    private lateinit var siteUtils: SiteUtils
    @Autowired
    private lateinit var fileFolderService: FileFolderService
    @Autowired
    private lateinit var mainNodesGetter: MainNodesGetter
    companion object{
        const val PATH_APPLIED = "InitPatomMainSitePatch applied"
    }
    override fun applyInternal(): String {
        AuthenticationUtil.setFullyAuthenticatedUser(AuthenticationUtil.getAdminUserName())
        val patomSite = siteUtils.createSite(siteId, siteName, SiteVisibility.PUBLIC)
        val siteDocumentLibrary: NodeRef = mainNodesGetter.getSiteDocumentLibrary(patomSite)
        val patomSiteWorkspaceDir = createWorkspaceDir(siteDocumentLibrary)
        createHtmlTemplatesDir(patomSiteWorkspaceDir)
        createPdfFormsDir(patomSiteWorkspaceDir)
        return PATH_APPLIED
    }
    private fun createWorkspaceDir(siteDocumentLibrary: NodeRef):NodeRef = fileFolderService.create(
        siteDocumentLibrary,
        workspaceDir,
        ContentModel.TYPE_FOLDER).nodeRef
    private fun createHtmlTemplatesDir(siteWorkspaceDir: NodeRef) = fileFolderService.create(
        siteWorkspaceDir,
        htmlTemplatesDir,
        ContentModel.TYPE_FOLDER)
    private fun createPdfFormsDir(siteWorkspaceDir: NodeRef) = fileFolderService.create(
        siteWorkspaceDir,
        pdfFormsDir,
        ContentModel.TYPE_FOLDER)
}