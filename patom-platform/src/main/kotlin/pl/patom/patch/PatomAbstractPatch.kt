package pl.patom.patch

import org.alfresco.repo.admin.patch.AbstractPatch

abstract class PatomAbstractPatch: AbstractPatch() {

    abstract val patchName: String
    override fun applyInternal(): String {
        execute()
        return "$patchName $APPLIED"
    }

    abstract fun execute()
}