package pl.patom.model

import org.alfresco.service.namespace.QName

// Patom URIs
const val URI_PATOM_CONTENT = "http://www.patom.pl/model/content/1.0"

// Patom Types:
val TYPE_PATOM_DOCUMENT: QName = QName.createQName(URI_PATOM_CONTENT, "document")
