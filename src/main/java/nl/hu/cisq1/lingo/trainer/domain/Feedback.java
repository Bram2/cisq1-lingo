package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Feedback {

    private final String attempt;
    private final List<Mark> marks;
    private List<String> hint = new ArrayList<>();

    public Feedback(String attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;

        if(marks.size() != attempt.length())
            throw new InvalidFeedbackException();

    }

    public List<String> giveHint(List<String> previousHint, String word){
        if(word.length() != previousHint.size())
            throw new InvalidFeedbackException();

        for(int i = 0; i < word.length(); i++)
            if((marks.get(i) == Mark.CORRECT) || (!previousHint.get(i).equals(".")))
                this.hint.add(word.substring(i, i+1));
            else
                this.hint.add(".");

        return hint;
    }

    public boolean wordIsGuessed(){
        return marks.stream().allMatch(m -> m.equals(Mark.CORRECT));
    }

    public boolean guessIsValid(){
        return marks.stream().noneMatch(m -> m.equals(Mark.INVALID));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(attempt, feedback.attempt) && Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                '}';
    }
}
