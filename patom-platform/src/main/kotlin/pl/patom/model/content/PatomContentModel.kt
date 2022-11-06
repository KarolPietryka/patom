package pl.patom.model.content

import org.alfresco.service.namespace.QName

const val CONTENT_MODEL_NAMESPACE_TO_LOCALNAME_SEPARATOR = ":"


//HEADER
const val PATOM_NAMESPACE_PREFIX_URI = "http://www.patom.pl/model/content/1.0"
const val PATOM_NAMESPACE_PREFIX = "pt"


// TYPES
val PATOM_TYPE_DOCUMENT = QName.createQName(PATOM_NAMESPACE_PREFIX_URI, "document")
