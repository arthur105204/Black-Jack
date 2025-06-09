package com.example.blackjack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    private Spinner spinnerMode, spinnerDecks, spinnerBet;
    private Button btnStartGame;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        root.setBackgroundColor(0xFFFFFFFF); // Set white background
        spinnerMode = root.findViewById(R.id.spinner_mode);
        spinnerDecks = root.findViewById(R.id.spinner_decks);
        spinnerBet = root.findViewById(R.id.spinner_bet);
        btnStartGame = root.findViewById(R.id.btn_start_game);

        // Set up spinners
        String[] modes = {"Classic", "Vegas Strip", "European"};
        String[] decks = {"1 deck", "2 decks", "4 decks", "6 decks", "8 decks"};
        String[] bets = {"$10", "$50", "$100", "$200", "$500"};
        spinnerMode.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, modes));
        spinnerDecks.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, decks));
        spinnerBet.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, bets));

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = spinnerMode.getSelectedItemPosition();
                int deckCount = 1;
                switch (spinnerDecks.getSelectedItemPosition()) {
                    case 0: deckCount = 1; break;
                    case 1: deckCount = 2; break;
                    case 2: deckCount = 4; break;
                    case 3: deckCount = 6; break;
                    case 4: deckCount = 8; break;
                }
                int bet = 10;
                switch (spinnerBet.getSelectedItemPosition()) {
                    case 0: bet = 10; break;
                    case 1: bet = 50; break;
                    case 2: bet = 100; break;
                    case 3: bet = 200; break;
                    case 4: bet = 500; break;
                }
                // Pass options to MainActivityFragment
//                MainActivityFragment gameFragment = MainActivityFragment.newInstance(mode, deckCount, bet);
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment, gameFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return root;
    }
}
