package de.techfak.gse.template.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class DeckServiceImpl implements DeckService {

    Date today = new Date(2025, 4,29);

    public DeckServiceImpl() {
    }

    @Override
    public List<Stapel> getDecks() {
        return List.of(new Stapel((long) 1, 1, today, true),
                new Stapel((long) 2, 2, today, false),
                new Stapel((long) 3, 2, today, true));
    }

    @Override
    public Optional<Stapel> getDeckById(long id) {
        if(id == 1) {
            return Optional.of(new Stapel((long) 1, 1, today, true));
        }
        return Optional.empty();
    }
}
