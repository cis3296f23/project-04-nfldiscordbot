package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A simple example, used on the jsoup website.
 */
public class TeamRecord {
    public static void main(String[] args) {
        try {
            String url = "https://www.footballdb.com/teams/nfl/philadelphia-eagles";
            Document doc = Jsoup.connect(url).get();

            // Find the element containing the Eagles' record
            Element recordElement = doc.select("#teambanner > div:nth-child(3) > b").first();

            // Extract and print the record
            String record = recordElement.text();
            System.out.println("Philadelphia Eagles " + record);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
