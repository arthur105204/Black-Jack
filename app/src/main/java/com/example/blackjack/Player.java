package com.example.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;
    private boolean isDealer;

    public Player(boolean isDealer) {
        this.isDealer = isDealer;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void clearHand() {
        hand.clear();
    }

    public int getScore() {
        int score = 0;
        int aceCount = 0;
        for (Card card : hand) {
            int rank = card.rank;
            if (rank > 10) {
                score += 10;
            } else if (rank == 1) {
                aceCount++;
                score += 11;
            } else {
                score += rank;
            }
        }
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        return score;
    }

    public boolean isDealer() {
        return isDealer;
    }
}

