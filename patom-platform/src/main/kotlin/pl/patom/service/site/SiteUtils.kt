package pl.patom.service.site

import org.alfresco.repo.tenant.MultiTAdminServiceImpl.STORE_BASE_ID_SPACES
import org.alfresco.rest.api.Sites
import org.alfresco.rest.api.model.Site
import org.alfresco.rest.api.model.SiteUpdate
import org.alfresco.rest.framework.resource.parameters.Params
import org.alfresco.service.cmr.repository.NodeRef
import org.alfresco.service.cmr.repository.StoreRef.PROTOCOL_WORKSPACE
import org.alfresco.service.cmr.site.SiteService
import org.alfresco.service.cmr.site.SiteVisibility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SiteUtils @Autowired constructor(
    private val sites: Sites,
    private val siteService: SiteService
){
    fun createSite(
        siteShortName: String,
        siteTitle: String,
        siteVisibility: SiteVisibility,
        params: Params = getDefaultParams()
    ): Site {
        val site = getSiteDefinition(siteShortName, siteTitle, siteVisibility)
        return sites.createSite(site, params)
    }

    private fun getSiteDefinition(siteId: String, siteTitle: String, siteVisibility: SiteVisibility): Site =
        Site().apply {
            id = siteId
            title = siteTitle
            visibility = siteVisibility
        }

    fun getSite(siteId: String): Site = sites.getSite(siteId)

    private fun getDefaultParams() = Params.valueOf("", "", null)

    fun setMembership(siteId: String, authorityName: String, role: String) =
        siteService.setMembership(siteId, authorityName, role)

    fun siteExists(siteId: String): Boolean = sites.validateSite(siteId) != null


}
