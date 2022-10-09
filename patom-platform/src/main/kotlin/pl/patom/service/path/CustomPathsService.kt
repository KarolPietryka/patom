package pl.patom.service.path

import org.alfresco.repo.nodelocator.NodeLocatorService
import org.alfresco.service.cmr.model.FileFolderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CustomPathsService @Autowired constructor(
    @Value("\${patom.general.pathSeparator}") private val patomPathSeparator: String,
){
    fun customPathToPathElementsList(customPath: String) =
        customPath.split(patomPathSeparator)
}