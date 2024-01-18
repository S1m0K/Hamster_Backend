package com.example.hamster_backend.model.entities;

import com.example.hamster_backend.model.QuickSort;
import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "PROGRAM")
public class Program implements Comparable {
    @Id
    @SequenceGenerator(name = "program_seq", sequenceName = "PROGRAM_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_seq")
    @Column(name = "ID")
    private long programId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "PROGRAM_NAME", unique = true)
    private String programName;

    @Column(name = "SOURCE_CODE")
    private String sourceCode;

    @Column(name = "PROGRAM_PATH")
    private String programPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program that = (Program) o;
        return Objects.equals(programName, that.programName) && Objects.equals(sourceCode, that.sourceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programName, sourceCode);
    }

    @Override
    public int compareTo(Object o) throws ClassCastException {
        if (!(o instanceof Program)) throw new ClassCastException("A program object expected");
        Program program = (Program) o;

        Set<String> usedExternalClasses = this.extractUsedExternalClasses();
        Set<String> usedExternalClassesFromSecondProgram = program.extractUsedExternalClasses();

        if (usedExternalClasses.contains(program.programName)) {
            return 1;
        } else if (usedExternalClassesFromSecondProgram.contains(this.programName)) {
            return -1;
        }
        return 0;
    }


    private static ArrayList<String> extractClassNames(String input) {
        String pattern = "\\s+(class|interface)\\s+([A-Z][a-zA-Z0-9_]*)\\s*";
        ArrayList<String> classNames = new ArrayList<>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        while (m.find()) {
            String s = m.group();
            s = s.replace("interface", "");
            s = s.replace("class", "");
            s = s.replace(" ", "");
            classNames.add(s);
        }

        return classNames;
    }

    public Set<String> extractUsedExternalClasses() {
        Set<String> usedExternalClasses = new HashSet<>();
        String stringPattern = "(=\\s*new\\s+[A-z]*\\s*\\()";
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(this.sourceCode);

        while (matcher.find()) {
            String s = matcher.group();
            s = s.replace("new", "");
            s = s.replace("(", "");
            s = s.replace("=", "");
            usedExternalClasses.add(s.trim());
        }
        return usedExternalClasses;
    }

    public Program(long programId, String programName, String programPath) {
        this.programId = programId;
        this.programName = programName;
        this.programPath = programPath;
    }

    public char getProgramType() {
        if (this.sourceCode.contains("class")) {
            return 'o';
        }
        return 'c';
    }
}
