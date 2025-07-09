package de.techfak.gse.template.parsingutils;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for Überschrift und Vorder- und Rückseiten von Cards.
 */
@Setter
@Getter
public class CardHelper {

    private String[] frontback;
    private String ueberschrift;

    CardHelper(String[] frontback, String ueberschrift) {
        this.frontback = frontback;
        this.ueberschrift = ueberschrift;
    }

}
