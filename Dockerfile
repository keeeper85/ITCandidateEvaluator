FROM openjdk:17
WORKDIR /usr/src/app
COPY out/artifacts/ITCandidateEvaluator_jar/ITCandidateEvaluator.jar /usr/src/app/target/*.jar ./
CMD ["java", "-jar", "ITCandidateEvaluator.jar"]
