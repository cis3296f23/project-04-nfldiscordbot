package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Player {

    public static void getPlayerInfo(String playerName) {

        try {
            String formattedUrl = formatPlayerUrl(playerName);
            // Set a timeout for the connection (e.g., 5000 milliseconds)
            Document doc = Jsoup.connect(formattedUrl).get();

            // Select the table body containing player stats
            Element tbody = doc.select("table.statistics tbody").first();

            if (tbody != null) {
                // Select all rows within the tbody
                Elements rows = tbody.select("tr");

                // Display headers
                displayHeaders();

                // Iterate through rows and display player stats
                for (Element row : rows) {
                    // Select all cells within the row
                    Elements cells = row.select("td");

                    // Display each cell's text content
                    for (int i = 0; i < cells.size(); i++) {
                        String cellText = cells.get(i).text();

                        // For the "Team" column, extract the acronym
                        if (i == 1) {
                            String[] teamNameParts = cellText.split("\\s");
                            System.out.print(teamNameParts[teamNameParts.length - 1] + "\t\t");
                        } else {
                            System.out.print(cellText + "\t\t");
                        }
                    }

                    // Move to the next line after processing each row
                    System.out.println();
                }
            }


        } catch (IOException e) {
            // Handle IOException (e.g., network issues, invalid URL)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }

    private static void displayHeaders() {
        System.out.println("Year\t\tTeam\t\tLeague\tG\tGS\tAtt\tCmp\tPct\tYds\tYPA\tYPG\tTD\tTD%\tInt\tInt%\tLg\tFD\t20+\tSack\tLoss\tRate");
    }

    public static String formatPlayerUrl(String playerName) {
        // Convert the player name to lowercase
        String lowercaseName = playerName.toLowerCase();

        // Replace spaces with hyphens
        String hyphenatedName = lowercaseName.replace(" ", "-");

        // Extract last name and first two letters of the first name
        String[] nameParts = lowercaseName.split(" ");
        String lastName = nameParts[nameParts.length - 1];
        String firstNameAbbreviation = nameParts[0].substring(0, Math.min(2, nameParts[0].length()));

        // Generate the unique identifier
        String uniqueIdentifier = lastName + firstNameAbbreviation + "01";

        // Build the formatted URL
        String formattedUrl = "https://www.footballdb.com/players/" + hyphenatedName + "-" + uniqueIdentifier;

        return formattedUrl;
    }

    public static void main(String[] args) {

        String playerName = "Jalen Hurts"; // Replace with the actual player name
        getPlayerInfo(playerName);
    }
}
