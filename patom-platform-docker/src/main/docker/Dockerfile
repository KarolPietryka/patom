FROM ${docker.acs.image}:${alfresco.platform.version}

ARG TOMCAT_DIR=/usr/local/tomcat

USER root
RUN yum install -y wget \
        less \
        libreoffice \
        libXrender \
        libXtst && \
    yum clean all && \
    rm -rf /var/cache/yum

# Copy Dockerfile to avoid an error if no JARs exist
COPY Dockerfile extensions/*.jar $TOMCAT_DIR/webapps/alfresco/WEB-INF/lib/

# Copy Dockerfile to avoid an error if no AMPs exist
COPY Dockerfile extensions/*.amp $TOMCAT_DIR/amps/
RUN echo 'java -jar $TOMCAT_DIR/alfresco-mmt/alfresco-mmt*.jar install $TOMCAT_DIR/amps/$1 $TOMCAT_DIR/webapps/alfresco -force -nobackup' > install_amp.sh && chmod +x install_amp.sh && \
        ./install_amp.sh javascript-console-repo-*.amp && \
        ./install_amp.sh polish-repo-*.amp && \
        rm install_amp.sh

COPY alfresco-global.properties $TOMCAT_DIR/shared/classes/alfresco-global.properties
COPY dev-log4j.properties $TOMCAT_DIR/shared/classes/alfresco/extension
COPY disable-webscript-caching-context.xml $TOMCAT_DIR/shared/classes/alfresco/extension

RUN wget https://github.com/wkhtmltopdf/wkhtmltopdf/releases/download/0.12.4/wkhtmltox-0.12.4_linux-generic-amd64.tar.xz && \
    tar xf  wkhtmltox-0.12.4_linux-generic-amd64.tar.xz -C /usr/bin && \
    rm -f wkhtmltox-0.12.4_linux-generic-amd64.tar.xz

# Copy Dockerfile to avoid an error if no license file exists
COPY Dockerfile license/*.* $TOMCAT_DIR/webapps/alfresco/WEB-INF/classes/alfresco/extension/license/

USER ${USERNAME}