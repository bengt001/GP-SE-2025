package de.techfak.gse.template.web.command;

/**
 * Cmd for requester and recipient.
 *
 * @param requester
 * @param recipient
 */
public record FriendSendCmd(String requester, String recipient) {}
