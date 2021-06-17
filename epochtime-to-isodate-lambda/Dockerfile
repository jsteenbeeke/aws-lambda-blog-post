# Use Amazon's Java 11 base image
FROM public.ecr.aws/lambda/java:11

# Copy all classes to the Java root of the image
COPY target/classes ${LAMBDA_TASK_ROOT}

# Copy all dependency libraries to the library folder
COPY target/dependency/* ${LAMBDA_TASK_ROOT}/lib/

# Set the handler class and method as the command to execute
CMD [ "nl.jeroensteenbeeke.tech.isodate.lambda.ISODateFormatter::handleRequest" ]