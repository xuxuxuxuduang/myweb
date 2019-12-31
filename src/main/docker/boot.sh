if [ -n "$SPRING_PROFILES_ACTIVE" ]; then
	java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -Djava.security.egd=file:/dev/./urandom -jar /app.jar
else
    java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
fi