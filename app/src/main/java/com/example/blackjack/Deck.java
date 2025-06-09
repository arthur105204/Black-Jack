package com.example.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private int currentIndex;

    public Deck() {
        cards = new ArrayList<>();
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 1; rank <= 13; rank++) {
                cards.add(new Card(suit, rank));
            }
        }
        shuffle();
    }

    public Deck(int deckCount) {
        cards = new ArrayList<>();
        for (int d = 0; d < deckCount; d++) {
            for (int suit = 0; suit < 4; suit++) {
                for (int rank = 1; rank <= 13; rank++) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
        currentIndex = 0;
    }

    public Card drawCard() {
        if (currentIndex < cards.size()) {
            return cards.get(currentIndex++);
        }
        return null;
    }
}
