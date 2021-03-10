package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private final Word word;
    private final List<Feedback> feedback = new ArrayList<>();
    public boolean roundDone = false;

    public Round(Word word){
        this.word = word;
    }

    public void guess(String attempt){
        if(feedback.size() <5 && !roundDone) {
            Feedback newFeedback = Feedback.create(word.getValue(), attempt);

            if(newFeedback.wordIsGuessed())
                roundDone = true;

            feedback.add(newFeedback);
            giveHint();
        }
        else
            throw new RuntimeException("Round is done");

    }

    public List<String> giveHint(){
        if(feedback.size() <= 1){
            List<String> firstHint = new ArrayList<>();

            firstHint.add(word.getValue().substring(0, 1));
            for(int i = 1; i < word.getLength(); i++)
                firstHint.add(".");

            if(feedback.size() == 0)
                return firstHint;
            else
                return feedback.get(0).giveHint(firstHint, word.getValue());
        }else
            return feedback.get(feedback.size() - 1).giveHint(feedback.get(feedback.size() - 2).getHint(), word.getValue());
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }
}
