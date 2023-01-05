package pl.patom.patch.dir.html.workshop

import org.alfresco.repo.action.executer.SpecialiseTypeActionExecuter
import org.alfresco.service.cmr.action.ActionService
import org.alfresco.service.cmr.rule.Rule
import org.alfresco.service.cmr.rule.RuleService
import org.alfresco.service.cmr.rule.RuleType
import pl.patom.action.executer.WriteHtmlContentToPatomFacadeExecuter
import pl.patom.model.content.TYPE_PATOM_DOCUMENT
import pl.patom.patch.PatomAbstractPatch
import pl.patom.patch.dir.pdf.content.SetHtmlTemplatesDirContentTypeRulePatch
import pl.patom.service.nodes.navigator.MainNodesGetter

class AddRewriteRuleToWorkshopDirPatch constructor(
    private val actionService: ActionService,
    private val ruleService: RuleService,
    private val mainNodesGetter: MainNodesGetter
): PatomAbstractPatch(){

    override val patchName: String = "AddRewriteRuleToWorkshopDirPatch"
    companion object{
        private const val REWRITE_HTML_CONTENT_TO_PATOM_FACADE_RULE = "rewriteHtmlContentToPatomFacadeRule"
    }

    override fun execute() {
        val rewriteHtmlContentToPatomFacadeAction = actionService.createAction(WriteHtmlContentToPatomFacadeExecuter.NAME)
        val rewriteHtmlContentToPatomFacadeRule = Rule().apply {
            this.action = rewriteHtmlContentToPatomFacadeAction
            this.title = REWRITE_HTML_CONTENT_TO_PATOM_FACADE_RULE
            this.description = REWRITE_HTML_CONTENT_TO_PATOM_FACADE_RULE
            this.setRuleType(RuleType.INBOUND)
            this.applyToChildren(true)
        }
        ruleService.saveRule(mainNodesGetter.getPatomHtmlTemplatesWorkshopDir(), rewriteHtmlContentToPatomFacadeRule)
    }

}