package pl.patom.patch.dir.pdf.content

import org.alfresco.repo.action.executer.SpecialiseTypeActionExecuter
import org.alfresco.repo.admin.patch.AbstractPatch
import org.alfresco.service.cmr.action.ActionService
import org.alfresco.service.cmr.rule.Rule
import org.alfresco.service.cmr.rule.RuleService
import org.alfresco.service.cmr.rule.RuleType
import pl.patom.action.SET_PATOM_DOCUMENT_TYPE_RULE
import pl.patom.model.content.TYPE_PATOM_DOCUMENT
import pl.patom.patch.PatomAbstractPatch
import pl.patom.service.nodes.navigator.MainNodesGetter

class SetHtmlTemplatesDirContentTypeRulePatch constructor(
    private val actionService: ActionService,
    private val ruleService: RuleService,
    private val mainNodesGetter: MainNodesGetter
): PatomAbstractPatch(){
    companion object{
        val PATCH_NAME = "SetHtmlTemplatesDirContentTypeRulePatch"
    }

    override val patchName: String = PATCH_NAME
    override fun execute() {
        val setPatomDocumentTypeAction = actionService.createAction(SpecialiseTypeActionExecuter.NAME)
            .apply {
                this.setParameterValue(SpecialiseTypeActionExecuter.PARAM_TYPE_NAME, TYPE_PATOM_DOCUMENT)
            }
        val setPatomDocumentTypeRule = Rule().apply {
            this.action = setPatomDocumentTypeAction
            this.title = SET_PATOM_DOCUMENT_TYPE_RULE
            this.description = SET_PATOM_DOCUMENT_TYPE_RULE
            this.setRuleType(RuleType.INBOUND)
            this.applyToChildren(true)
        }
        ruleService.saveRule(mainNodesGetter.getPatomHtmlTemplateDir(), setPatomDocumentTypeRule)
    }
}