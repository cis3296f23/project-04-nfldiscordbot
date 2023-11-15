package org.example;

public class TeamData {
    String name;
    int wins;
    int losses;
    int draws;
    double winPct;

    public TeamData(String name, int wins, int losses, int draws, double pct) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.winPct = pct;
    }

    @Override
    public String toString() {
        return name + " " + wins + "-" + losses + "-" + draws + " " + winPct;
    }

    public String strRecord() {
        return wins + "-" + losses + "-" + draws;
    }
}
