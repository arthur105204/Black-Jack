package com.example.blackjack;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class MainActivityFragment extends Fragment {
    private Game game;
    private LinearLayout dealerCardsLayout, playerCardsLayout;
    private TextView dealerScoreText, playerScoreText, gameStatusText;
    private Button btnHit, btnStand, btnNewGame;
    private View rootView;

    public MainActivityFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootView.setBackgroundColor(Color.BLACK);

        dealerCardsLayout = rootView.findViewById(R.id.dealer_cards_layout);
        playerCardsLayout = rootView.findViewById(R.id.player_cards_layout);
        dealerScoreText = rootView.findViewById(R.id.dealer_score_text);
        playerScoreText = rootView.findViewById(R.id.player_score_text);
        gameStatusText = rootView.findViewById(R.id.game_status_text);
        btnHit = rootView.findViewById(R.id.btn_hit);
        btnStand = rootView.findViewById(R.id.btn_stand);
        btnNewGame = rootView.findViewById(R.id.btn_new_game);

        game = new Game(1); // Always use 1 deck for simplicity
        game.startNewGame();
        updateUI();

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.playerHit();
                updateUI();
            }
        });
        btnStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.playerStand();
                updateUI();
            }
        });
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.startNewGame();
                updateUI();
            }
        });

        return rootView;
    }

    private void updateUI() {
        // Dealer cards
        dealerCardsLayout.removeAllViews();
        for (int i = 0; i < game.getDealer().getHand().size(); i++) {
            Card card = game.getDealer().getHand().get(i);
            ImageView cardView = createCardImageView(card, i == 1 && game.isPlayerTurn());
            // Card size 3x: 160*3=480, 240*3=720; overlay nhiều hơn: marginStart -60*5=-300
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(480, 720);
            if (i > 0) params.setMarginStart(-300);
            cardView.setLayoutParams(params);
            dealerCardsLayout.addView(cardView);
        }
        dealerScoreText.setText("Dealer: " + (game.isPlayerTurn() ? getDealerFirstCardScore() : game.getDealer().getScore()));

        // Player cards
        playerCardsLayout.removeAllViews();
        for (int i = 0; i < game.getPlayer().getHand().size(); i++) {
            Card card = game.getPlayer().getHand().get(i);
            ImageView cardView = createCardImageView(card, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(480, 720);
            if (i > 0) params.setMarginStart(-300);
            cardView.setLayoutParams(params);
            playerCardsLayout.addView(cardView);
        }
        playerScoreText.setText("Player: " + game.getPlayer().getScore());

        // Status and buttons
        if (game.isGameOver()) {
            String result = game.getResult();
            if (result == null) result = "";
            gameStatusText.setText(result);
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
        } else {
            gameStatusText.setText("Your turn");
            btnHit.setEnabled(true);
            btnStand.setEnabled(true);
        }
    }

    private ImageView createCardImageView(Card card, boolean hide) {
        ImageView imageView = new ImageView(getContext());
        int resId = getCardResId(card);
        if (hide) {
            resId = getResources().getIdentifier("back_light", "drawable", getContext().getPackageName());
        }
        imageView.setImageResource(resId);
        imageView.setPadding(0, 0, 0, 0);
        return imageView;
    }

    private int getCardResId(Card card) {
        String suitName = "";
        switch (card.suit) {
            case 0: suitName = "clubs"; break;
            case 1: suitName = "diamonds"; break;
            case 2: suitName = "hearts"; break;
            case 3: suitName = "spades"; break;
        }
        String rankName;
        switch (card.rank) {
            case 1: rankName = "a"; break;
            case 11: rankName = "j"; break;
            case 12: rankName = "q"; break;
            case 13: rankName = "k"; break;
            default: rankName = String.valueOf(card.rank); break;
        }
        String resName = suitName + "_" + rankName;
        return getResources().getIdentifier(resName, "drawable", getContext().getPackageName());
    }

    private int getDealerFirstCardScore() {
        Card first = game.getDealer().getHand().get(0);
        int rank = first.rank;
        if (rank > 10) return 10;
        if (rank == 1) return 11;
        return rank;
    }
}