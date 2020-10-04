#!/bin/sh

ARGS="-Dspring.config.location=classpath:/config/common/,classpath:/config/dev/ -Dspring.profiles.active=dev --enable-preview"

mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="$ARGS"