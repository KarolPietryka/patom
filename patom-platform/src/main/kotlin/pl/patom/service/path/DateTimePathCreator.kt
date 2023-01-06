package pl.patom.service.path

import org.alfresco.model.ContentModel
import org.alfresco.service.cmr.model.FileFolderService
import org.alfresco.service.cmr.repository.NodeRef
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class DateTimePathCreator constructor(
    private val fileFolderService: FileFolderService,
    private val customPathsService: CustomPathsService,
){
    companion object{
        const val DATE_FORMAT = "yyyy/MM/dd/HH/mm/ss"
    }

    /**
     * @return return last in chain that is base on current date time
     */
    fun createCurrentDatePath(startWith: NodeRef): NodeRef {
        return customPathsService.customPathToPathElementsList(getCurrentTime())
        .run{
            var lastCreated = startWith
            this.forEach { childName ->
                val childDir = getChildIfExist(lastCreated, childName)
                    ?: fileFolderService.create(lastCreated, childName, ContentModel.TYPE_FOLDER).nodeRef
                lastCreated = childDir
            }
            return@run lastCreated
        }
    }

    private fun getChildIfExist(parentNodeRef: NodeRef, childName: String) =
        fileFolderService.resolveNamePath(
            parentNodeRef,
            customPathsService.customPathToPathElementsList(childName),
            false)?.nodeRef

    private fun getCurrentTime() =
        DateTimeFormatter.ofPattern(DATE_FORMAT).run{
            format(LocalDateTime.now())
        }

}
