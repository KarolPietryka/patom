package pl.patom.action.config

import org.alfresco.repo.action.executer.ActionExecuterAbstractBase
import org.alfresco.service.cmr.repository.ContentService
import org.alfresco.service.cmr.repository.NodeService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import pl.patom.action.executer.WriteHtmlContentToPatomFacadeExecuter
import pl.patom.service.file.content.ContentReadingService
import pl.patom.service.nodes.creator.HtmlFacadeNodeCreatorService
import pl.patom.service.nodes.navigator.MainNodesGetter

@Configuration
@ComponentScan("pl.patom.action.executer")
class ExecuterConfig (
){
    @Bean
    fun writeHtmlContentToPatomFacadeExecuter(
        contentService: ContentService,
        contentReadingService: ContentReadingService,
        mainNodesGetter: MainNodesGetter,
        htmlFacadeNodeCreatorService: HtmlFacadeNodeCreatorService,
        nodeService: NodeService
    ) : ActionExecuterAbstractBase =
        WriteHtmlContentToPatomFacadeExecuter(
            contentService,
            contentReadingService,
            mainNodesGetter,
            htmlFacadeNodeCreatorService,
            nodeService,
        )
}