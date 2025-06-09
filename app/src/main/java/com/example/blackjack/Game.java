package com.example.blackjack;

public class Game {
    private Deck deck;
    private Player player;
    private Player dealer;
    private boolean playerTurn;
    private boolean gameOver;
    private int playerMoney = 1000;
    private int currentBet = 0;
    private boolean outOfMoney = false;
    private boolean canDoubleDown = true;
    private boolean canSurrender = true;
    private boolean surrendered = false;

    // New fields for rules and deck count
    private int deckCount = 1;
    private boolean payoutIs65 = false;
    private boolean dealerHitsSoft17 = false;

    // New constructor
    public Game(int deckCount, int playerMoney, boolean payoutIs65, boolean dealerHitsSoft17) {
        this.deckCount = deckCount;
        this.playerMoney = playerMoney;
        this.payoutIs65 = payoutIs65;
        this.dealerHitsSoft17 = dealerHitsSoft17;
        deck = new Deck(deckCount);
        player = new Player(false);
        dealer = new Player(true);
        playerTurn = true;
        gameOver = false;
        startNewGame();
    }

    // Overload for just deck count
    public Game(int deckCount) {
        this(deckCount, 1000, false, false);
    }

    public Game() {
        deck = new Deck();
        player = new Player(false);
        dealer = new Player(true);
        playerTurn = true;
        gameOver = false;
        startNewGame();
    }

    public void startNewGame() {
        if (playerMoney <= 0) {
            outOfMoney = true;
            gameOver = true;
            return;
        }
        deck = new Deck(deckCount);
        player.clearHand();
        dealer.clearHand();
        playerTurn = true;
        gameOver = false;
        canDoubleDown = true;
        canSurrender = true;
        surrendered = false;
        // Deal initial cards
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        // Check for blackjack
        if (isBlackjack(player) || isBlackjack(dealer)) {
            playerTurn = false;
            gameOver = true;
            settleBet();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
        return dealer;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public boolean isOutOfMoney() {
        return outOfMoney;
    }

    public boolean canDoubleDown() {
        return canDoubleDown && player.getHand().size() == 2 && playerTurn && !gameOver && playerMoney >= currentBet;
    }

    public boolean canSurrender() {
        return canSurrender && player.getHand().size() == 2 && playerTurn && !gameOver;
    }

    public boolean isSurrendered() {
        return surrendered;
    }

    public void setBet(int bet) {
        if (bet > 0 && bet <= playerMoney) {
            currentBet = bet;
        }
    }

    public void playerHit() {
        if (playerTurn && !gameOver) {
            player.addCard(deck.drawCard());
            if (player.getScore() > 21) {
                gameOver = true;
                settleBet();
            } else if (player.getScore() == 21) {
                playerTurn = false;
                dealerTurn();
                settleBet();
            }
        }
    }

    public void playerStand() {
        if (playerTurn && !gameOver) {
            playerTurn = false;
            dealerTurn();
            settleBet();
        }
    }

    public void playerDoubleDown() {
        if (canDoubleDown()) {
            playerMoney -= currentBet;
            currentBet *= 2;
            player.addCard(deck.drawCard());
            playerTurn = false;
            dealerTurn();
            settleBet();
            canDoubleDown = false;
            canSurrender = false;
        }
    }

    public void playerSurrender() {
        if (canSurrender()) {
            surrendered = true;
            playerMoney -= currentBet / 2;
            gameOver = true;
            canDoubleDown = false;
            canSurrender = false;
        }
    }

    private void dealerTurn() {
        // Dealer hits or stands on soft 17 based on rule
        while (dealer.getScore() < 17 || (dealer.getScore() == 17 && dealerHitsSoft17 && hasSoft17(dealer))) {
            dealer.addCard(deck.drawCard());
        }
        gameOver = true;
    }

    private boolean hasSoft17(Player p) {
        int score = p.getScore();
        boolean hasAce = false;
        int total = 0;
        for (Card card : p.getHand()) {
            if (card.rank == 1) hasAce = true;
            if (card.rank > 10) total += 10;
            else total += card.rank;
        }
        return hasAce && score == 17;
    }

    private void settleBet() {
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();
        boolean playerBJ = isBlackjack(player);
        boolean dealerBJ = isBlackjack(dealer);
        if (playerBJ && dealerBJ) {
            // Push, no money change
        } else if (playerBJ) {
            if (payoutIs65) playerMoney += (int) (currentBet * 1.2);
            else playerMoney += (int) (currentBet * 1.5);
        } else if (dealerBJ) {
            playerMoney -= currentBet;
        } else if (playerScore > 21) {
            playerMoney -= currentBet;
        } else if (dealerScore > 21) {
            playerMoney += currentBet;
        } else if (playerScore > dealerScore) {
            playerMoney += currentBet;
        } else if (playerScore < dealerScore) {
            playerMoney -= currentBet;
        } // else push
        if (playerMoney <= 0) {
            outOfMoney = true;
        }
    }

    public boolean isBlackjack(Player p) {
        return p.getHand().size() == 2 && p.getScore() == 21;
    }

    public String getResult() {
        if (surrendered) return "You surrendered. Half your bet is lost.";
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();
        boolean playerBJ = isBlackjack(player);
        boolean dealerBJ = isBlackjack(dealer);
        if (playerBJ && dealerBJ) {
            return "Push! Both have Blackjack.";
        } else if (playerBJ) {
            return "You win with Blackjack!";
        } else if (dealerBJ) {
            return "Dealer wins with Blackjack!";
        } else if (playerScore > 21) {
            return "You bust! Dealer wins.";
        } else if (dealerScore > 21) {
            return "Dealer busts! You win.";
        } else if (playerScore > dealerScore) {
            return "You win!";
        } else if (playerScore < dealerScore) {
            return "Dealer wins!";
        } else {
            return "Push! It's a tie.";
        }
    }
}
