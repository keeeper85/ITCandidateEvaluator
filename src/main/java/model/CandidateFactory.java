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

public class CandidateFactory {

    private final static Pattern removePdfExtension = Pattern.compile("pdf");;
    private final static Pattern removeNonLetterCharacters = Pattern.compile("[^a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ]");
    private final static Pattern removeCvTags = Pattern.compile("resume|CV", Pattern.CASE_INSENSITIVE);

    public static List<Candidate> getCandidatesFromResumes(String directoryWithResumesInPdf, Recruitment recruitment){
        HashMap<String,String> fileNames = getFileNames(directoryWithResumesInPdf);
        iterateOverMapElementsAndReplace(fileNames, removePdfExtension);
        iterateOverMapElementsAndReplace(fileNames, removeNonLetterCharacters);
        iterateOverMapElementsAndReplace(fileNames, removeCvTags);
        separateFirstAndLastNames(fileNames);
        List<Candidate> candidates = createCandidates(recruitment, fileNames);
        return candidates;
    }

    public static HashMap<String,String> getFileNames(String directoryWithResumesInPdf){
        HashMap<String,String> filesAndTheirNames = new HashMap<>();

        try (Stream<Path> paths = Files.list(Paths.get(directoryWithResumesInPdf))) {
            paths.filter(file -> file.toString().endsWith(".pdf"))
                    .forEach(file -> {
//                        fileNames.add(file.getFileName().toString());
                        filesAndTheirNames.put(file.toString(), file.getFileName().toString());
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filesAndTheirNames;
    }

    private static void iterateOverMapElementsAndReplace(HashMap<String,String> fileNames, Pattern pattern){
        for (Map.Entry<String, String> entry : fileNames.entrySet()) {
            String oldValue = entry.getValue();
            String newValue = pattern.matcher(oldValue).replaceAll("");
            entry.setValue(newValue);
        }
    }

    private static void separateFirstAndLastNames(HashMap<String,String> fileNames) {
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

    private static List<Candidate> createCandidates(Recruitment recruitment, HashMap<String,String> fileNames) {
        List<Candidate> candidates = new ArrayList<>();
        String firstName = "???";
        String lastName = "???";

        for (Map.Entry<String, String> entry : fileNames.entrySet()) {
            String[] firstAndLastName = entry.getValue().split(" ");

            if (firstAndLastName[0] != null && firstAndLastName[0].length() > 0) firstName = firstAndLastName[0];
            else firstName = "???";

            try{
                lastName = firstAndLastName[1];
            } catch (ArrayIndexOutOfBoundsException oneName){
                lastName = "???";
            }

            Candidate candidate = new Candidate(recruitment, firstName, lastName);
            candidate.setPathToResumeFile(entry.getKey());
            candidates.add(candidate);
        }
        return candidates;
    }

















}
