package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Team {

    String name;

    public Team (String name) {
        this.name = name;
    }

    public String getRecord() {
        String result = "";

        try {
            StringBuilder url = new StringBuilder("https://www.footballdb.com/teams/nfl/");
            url.append(name);
            // System.out.println(url);

            Document doc = Jsoup.connect(url.toString()).get();

            Element recordElement = doc.select("#teambanner > div:nth-child(3) > b").first();

            // Extract and print the record
            String record = recordElement.text();
            // System.out.println(formatString(name) + " " + record);
            result += formatString(name) + " " + record;

        }catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    private String formatString(String input) {
        String[] words = input.split("-");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                if (formatted.length() > 0) {
                    formatted.append(" ");
                }
                formatted.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1));
            }
        }

        return formatted.toString();
    }

}
