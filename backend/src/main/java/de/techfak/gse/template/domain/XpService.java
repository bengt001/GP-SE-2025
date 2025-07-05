
package de.techfak.gse.template.domain;

/**
 * Interface für den XpService.
 */
public interface XpService {


    /**
     * Berechnet die XP für eine gelernte Karte.
     *
     * @param cardType Typ der Karte (definition, problem, schema)
     * @param uncoveredItems Anzahl aufgedeckter Elemente (nur für Schemata)
     * @param rating Bewertung durch User (0 = sehr schlecht, 4 = super)
     * @return Vergebene XP als Ganzzahl
     */



    int calculateXp(String cardType, int uncoveredItems, int rating);
    int addXp(String userId, String cardType, int uncoveredItems, int rating);
}

