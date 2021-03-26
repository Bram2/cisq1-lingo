package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Round {

    @Id
    @GeneratedValue
    private int id;

    private String word;

    @OneToMany
    private final List<Feedback> feedback = new ArrayList<>();

    public boolean wordGuessed = false;

    public Round(){}

    public Round(String word){
        this.word = word;
    }

    public void guess(String attempt){
        if(feedback.size() < 5 && !wordGuessed) {
            Feedback newFeedback = Feedback.create(word, attempt);

            if(newFeedback.wordIsGuessed())
                wordGuessed = true;

            feedback.add(newFeedback);
            giveHint();
        }
        else
            throw new RuntimeException("Round is done");

    }

    public List<String> giveHint(){
        if(feedback.size() <= 1){
            List<String> firstHint = new ArrayList<>();

            firstHint.add(word.substring(0, 1));
            for(int i = 1; i < word.length(); i++)
                firstHint.add(".");

            if(feedback.isEmpty())
                return firstHint;
            else
                return feedback.get(0).giveHint(firstHint, word);
        }else
            return feedback.get(feedback.size() - 1).giveHint(feedback.get(feedback.size() - 2).getHint(), word);
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public String getWord() {
        return word;
    }
}
