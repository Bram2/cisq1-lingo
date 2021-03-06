package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import javax.persistence.*;
import java.util.*;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue
    private int id;

    private String attempt;

    @Enumerated
    @ElementCollection
    private List<Mark> marks;

    @ElementCollection
    private final List<String> hint = new ArrayList<>();

    public Feedback(){}

    public Feedback(String attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;

        if(marks.size() != attempt.length())
            throw new InvalidFeedbackException();

    }

    public static Feedback create(String word, String attempt){
        if(word.length() != attempt.length())
            throw new InvalidFeedbackException();

        List<Mark> markList = new ArrayList<>();

        StringBuilder present = new StringBuilder();
        StringBuilder notCorrect = new StringBuilder();

        for(int i = 0; i < word.length(); i++)
            if(!word.substring(i, i+1).equals(attempt.substring(i, i+1)))
                notCorrect.append(word.charAt(i));

        for(int i = 0; i < word.length(); i++){
            String letter = attempt.substring(i, i + 1);
            if (word.substring(i, i + 1).equals(letter)){
                markList.add(CORRECT);
                present.append(letter);
            }else if (word.contains(letter)
                    && word.chars().filter(ch -> ch == letter.charAt(0)).count() > present.chars().filter(ch -> ch == letter.charAt(0)).count()
            && notCorrect.toString().contains(letter)){

                markList.add(PRESENT);
                present.append(letter);
                notCorrect.deleteCharAt(notCorrect.indexOf(letter));
            }else {
                markList.add(ABSENT);
            }
        }

        return new Feedback(attempt, markList);
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public List<String> getHint() {
        return hint;
    }

    public List<String> giveHint(List<String> previousHint, String word){
        if(word.length() != previousHint.size())
            throw new InvalidFeedbackException();

        if(hint.isEmpty()) {
            for (int i = 0; i < word.length(); i++)
                if ((marks.get(i) == CORRECT) || (!previousHint.get(i).equals(".")))
                    this.hint.add(word.substring(i, i + 1));
                else
                    this.hint.add(".");
        }

        return hint;
    }

    public boolean wordIsGuessed(){
        return marks.stream().allMatch(m -> m.equals(CORRECT));
    }

    public boolean guessIsValid(){
        return marks.stream().noneMatch(m -> m.equals(INVALID));
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                ", hint=" + hint +
                '}';
    }
}
