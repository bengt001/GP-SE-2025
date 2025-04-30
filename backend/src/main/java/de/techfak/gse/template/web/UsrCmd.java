package de.techfak.gse.template.web;

public record UsrCmd(String email, String username, String password,String... roles) {
}
