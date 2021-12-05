package pl.patom.model.rest.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ToPDFRequest (
    val documentNodeRef: String)