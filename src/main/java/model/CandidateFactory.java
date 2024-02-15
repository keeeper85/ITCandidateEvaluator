package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CandidateFactory {

    private final static Pattern removePdfExtension = Pattern.compile("pdf");;
    private final static Pattern removeNonLetterCharacters = Pattern.compile("[^a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ]");
    private final static Pattern removeCvTags = Pattern.compile("resume|CV", Pattern.CASE_INSENSITIVE);

    public static List<Candidate> getCandidatesFromResumes(String directoryWithResumesInPdf, Recruitment recruitment){
        List<String> fileNames = getFileNames(directoryWithResumesInPdf);
        iterateOverListElementsAndReplace(fileNames, removePdfExtension);
        iterateOverListElementsAndReplace(fileNames, removeNonLetterCharacters);
        iterateOverListElementsAndReplace(fileNames, removeCvTags);
        separateFirstAndLastNames(fileNames);
        List<Candidate> candidates = createCandidates(recruitment, fileNames);
        return candidates;
    }

    public static List<String> getFileNames(String directoryWithResumesInPdf){
        List<String> fileNames = new ArrayList<>();

        try (Stream<Path> paths = Files.list(Paths.get(directoryWithResumesInPdf))) {
            paths.filter(file -> file.toString().endsWith(".pdf"))
                    .forEach(file -> {
                        fileNames.add(file.getFileName().toString());
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileNames;
    }

    private static void iterateOverListElementsAndReplace(List<String> fileNames, Pattern pattern){
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            fileName = pattern.matcher(fileName).replaceAll("");
            fileNames.set(i, fileName);
        }
    }

    private static void separateFirstAndLastNames(List<String> fileNames) {
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            fileName = separateValuesWithSpace(fileName);
            fileNames.set(i, fileName);
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

    private static List<Candidate> createCandidates(Recruitment recruitment, List<String> fileNames) {
        List<Candidate> candidates = new ArrayList<>();
        String firstName = "???";
        String lastName = "???";

        for (String fileName : fileNames) {
            String[] firstAndLastName = fileName.split(" ");

            if (firstAndLastName[0] != null && firstAndLastName[0].length() > 0) firstName = firstAndLastName[0];
            else firstName = "???";

            try{
                lastName = firstAndLastName[1];
            } catch (ArrayIndexOutOfBoundsException oneName){
                lastName = "???";
            }

            candidates.add(new Candidate(recruitment, firstName, lastName));
        }
        return candidates;
    }

















}
