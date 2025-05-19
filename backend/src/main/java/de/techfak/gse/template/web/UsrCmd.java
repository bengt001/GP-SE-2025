package de.techfak.gse.template.web;

/**
 * Usr Record was zum Auslesen aus dem Backend genutzt wird.
 * @param email Email des nutzers
 * @param username Nutzername des Nutzers
 * @param password Passwort des Nutzers
 * @param roles Rolle(n) die der Nutzer hat
 */
public record UsrCmd(String email, String username, String password, String... roles) {
}
