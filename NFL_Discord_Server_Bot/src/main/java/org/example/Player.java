package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Player {

    public static void getQBInfo(String playerName) {
        try {
            String formattedUrl = formatPlayerUrl(playerName);
            // Set a timeout for the connection (e.g., 5000 milliseconds)
            Document doc = Jsoup.connect(formattedUrl).get();

            // Select the table body containing player stats
            Element tbody = doc.select("table.statistics tbody").first();

            if (tbody != null) {
                // Select the third-to-last row within the tbody
                Elements rows = tbody.select("tr");
                int thirdToLastRowIndex = rows.size() - 3;

                if (thirdToLastRowIndex >= 0) {
                    Element secondToLastRow = rows.get(thirdToLastRowIndex);

                    // Select all cells within the second-to-last row
                    Elements cells = secondToLastRow.select("td");

                    int year = Integer.parseInt(cells.get(0).text());

                    String fullTeam = cells.get(1).text();
                    String team = extractTeamInitials(fullTeam);

                    double completionPercentage = Double.parseDouble(cells.get(7).text());

                    String yardsWithCommas = cells.get(8).text();
                    String yardsWithoutCommas = yardsWithCommas.replace(",", "");
                    int yards = Integer.parseInt(yardsWithoutCommas);

                    int passingTouchdowns = Integer.parseInt(cells.get(11).text());
                    int interceptions = Integer.parseInt(cells.get(13).text());

                    System.out.println("year = " + year);
                    System.out.println("team = " + team);
                    System.out.println("completion percentage = " + completionPercentage);
                    System.out.println("yards = " + yards);
                    System.out.println("passing touchdowns =  " + passingTouchdowns);
                    System.out.println("interceptions = " + interceptions);
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

    public static String extractTeamInitials(String input){
        // Iterate over the last 3 characters and check if they are all uppercase
        for (int i = input.length() - 3; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (!Character.isUpperCase(currentChar)) {
                return input.substring(input.length() - 2); // If any character is not uppercase, return last two digits
            }
        }
        return input.substring(input.length() - 3); // else return last three digits
    }
    public static String formatPlayerUrl(String playerName) {
        // Convert the player name to lowercase
        String lowercaseName = playerName.toLowerCase();

        // Replace spaces with hyphens
        String hyphenatedName = lowercaseName.replace(" ", "-");

        // Extract first five letters of last name and first two letters of the first name
        String[] nameParts = lowercaseName.split(" ");
        String lastName = nameParts[nameParts.length - 1];
        // Ensure that the last name has at least 5 characters before extracting
        if (lastName.length() > 5) {
            // Extract the first 5 letters
            lastName = lastName.substring(0, 5);
        }
        String firstNameAbbreviation = nameParts[0].substring(0, Math.min(2, nameParts[0].length()));

        // Generate the unique identifier
        String uniqueIdentifier = lastName + firstNameAbbreviation + "01";

        // Build the formatted URL
        String formattedUrl = "https://www.footballdb.com/players/" + hyphenatedName + "-" + uniqueIdentifier;
        System.out.println(formattedUrl);
        return formattedUrl;
    }

    public static void main(String[] args) {
    //Get player position
    //QB, Receiver, Defensive Player, Kicker, Punter, OL(other?)
        getQBInfo("Jalen Hurts");
        getQBInfo("Sam Howell");
        getQBInfo("Daniel Jones");
        getQBInfo("Dak Prescott");


    }
}
