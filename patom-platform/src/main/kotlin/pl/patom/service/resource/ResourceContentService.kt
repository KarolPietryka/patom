package pl.patom.service.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import pl.patom.service.properties.resources.HtmlTemplateResourcesProperties
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

@Service
class ResourceContentService @Autowired constructor(
) {
    fun getContentOfResourceFile(classPath: String): String =
        getContentInputStream(classPath).let {
            BufferedReader(InputStreamReader(it)).lines().collect(Collectors.joining("\n"))

    }
    fun getContentInputStream(classPath: String) =
        ClassPathResource(classPath).inputStream
}