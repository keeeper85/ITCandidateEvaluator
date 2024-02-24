package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * CandidateFactory is an immutable final class containing only static methods.
 * This class sole purpose is to find pdf files in the given directory and extract them to pair: "firstName lastName"
 * File extension, non-letter characters and resume-related tags are removed.
 * If a file name consist only of removable characters, the output will be like: "???"
 */

public final class CandidateFactory {
    private final static Pattern REMOVE_PDF_EXTENSION = Pattern.compile("pdf");;
    private final static Pattern REMOVE_NON_LETTER_CHARACTERS = Pattern.compile("[^a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ]");
    private final static Pattern REMOVE_CV_TAGS = Pattern.compile("resume|CV", Pattern.CASE_INSENSITIVE);
    private CandidateFactory() {}

    public static List<Candidate> getCandidatesFromResumes(String directoryWithResumesInPdf, Recruitment recruitment){
        Map<String,String> fileNames = getFileNames(directoryWithResumesInPdf);
        iterateOverMapElementsAndReplace(fileNames, REMOVE_PDF_EXTENSION);
        iterateOverMapElementsAndReplace(fileNames, REMOVE_NON_LETTER_CHARACTERS);
        iterateOverMapElementsAndReplace(fileNames, REMOVE_CV_TAGS);
        separateFirstAndLastNames(fileNames);
        List<Candidate> candidates = createCandidates(recruitment, fileNames);
        return candidates;
    }

    public static Map<String,String> getFileNames(String directoryWithResumesInPdf){
        HashMap<String,String> filesAndTheirNames = new HashMap<>();

        try (Stream<Path> paths = Files.list(Paths.get(directoryWithResumesInPdf))) {
            paths.filter(file -> file.toString().endsWith(".pdf"))
                    .forEach(file -> {
                        filesAndTheirNames.put(file.toString(), file.getFileName().toString());
                    });
        } catch (IOException e) {
            Model.logger.error("Error reading candidate's resume: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return filesAndTheirNames;
    }

    private static void iterateOverMapElementsAndReplace(Map<String,String> fileNames, Pattern pattern){
        for (Map.Entry<String, String> entry : fileNames.entrySet()) {
            String oldValue = entry.getValue();
            String newValue = pattern.matcher(oldValue).replaceAll("");
            entry.setValue(newValue);
        }
    }

    private static void separateFirstAndLastNames(Map<String,String> fileNames) {
        for (Map.Entry<String, String> entry : fileNames.entrySet()) {
            String oldValue = entry.getValue();
            String newValue = separateValuesWithSpace(oldValue);
            entry.setValue(newValue);
        }
    }

    private static String separateValuesWithSpace(String name) {
        StringBuilder firstName = new StringBuilder();
        StringBuilder lastName = new StringBuilder();
        boolean lastNameStarted = false;
        char[] nameCharacters = name.toCharArray();

        for (int i = 0; i < nameCharacters.length; i++) {
            if (!lastNameStarted){
                firstName.append(nameCharacters[i]);
                if ((i+1) < nameCharacters.length){
                    char nextChar = nameCharacters[i+1];
                    if (Character.isUpperCase(nextChar)) lastNameStarted = true;
                }
            }
            else{
                lastName.append(nameCharacters[i]);
                if ((i+1) < nameCharacters.length){
                    char nextChar = nameCharacters[i+1];
                    if (Character.isUpperCase(nextChar)) break;
                }
            }
        }

        String separatedValues = firstName.toString().trim() + " " + lastName.toString().trim();
        return separatedValues;
    }

    private static List<Candidate> createCandidates(Recruitment recruitment, Map<String,String> fileNames) {
        List<Candidate> candidates = new ArrayList<>();
        String firstName = "";
        String lastName;

        for (Map.Entry<String, String> entry : fileNames.entrySet()) {
            String[] firstAndLastName = entry.getValue().split(" ");

            try{
                if (firstAndLastName[0] != null && firstAndLastName[0].length() > 0) firstName = firstAndLastName[0];
                else firstName = "???";
                lastName = firstAndLastName[1];
            } catch (ArrayIndexOutOfBoundsException oneName){
                Model.logger.warn("Candidate has only one name element or none: " + oneName.getMessage());
                lastName = "???";
            }

            Candidate candidate = new Candidate(recruitment, firstName, lastName);
            candidate.setPathToResumeFile(entry.getKey());
            candidates.add(candidate);
        }
        Model.logger.info("Candidate object(s) created from their resume files.");

        return candidates;
    }
}
