package de.techfak.gse.template.web.command;

public record UsrCmd(String email, String username, String password,String... roles) {
}
