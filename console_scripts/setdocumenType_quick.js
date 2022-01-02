var ctx = Packages.org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
var nodeService = ctx["getBean(java.lang.String)"]("nodeService");


var type = Packages.org.alfresco.service.namespace.QName.createQName("http://www.patom.pl/model/content/1.0", "document");

nodeService.setType(document.nodeRef, type);