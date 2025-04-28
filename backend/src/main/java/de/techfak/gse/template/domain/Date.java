package de.techfak.gse.template.domain;
import jakarta.persistence.Embeddable;

@Embeddable
public class Date {
    public int year;
    public int month;
    public int day;

    protected Date() {}

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}

