package pl.patom.model.properties.transform

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class ToPdfTransformationProperties @Autowired constructor(
    @Value("\${patom.transformation.package.wkhtmltopdf.path}") val wkhtmltopdf: String,
    ){
    companion object{
        val PROP_TRANSFORMATION_COMMAND = listOf(
            "--disable-javascript",
            "--disable-external-links",
            "--disable-local-file-access",
            "--encoding",
            "utf-8"
        )
    }
}
