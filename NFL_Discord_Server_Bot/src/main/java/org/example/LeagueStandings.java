package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeagueStandings {

    public static String displayTable(List<TeamData> teams){
        StringBuilder strTable = new StringBuilder("NFL LEAGUE STANDINGS\n\n");

        String header = String.format("%-30s %-15s %s\n\n", "TEAM NAME:", "RECORD:", "WIN PERCENTAGE(IN DECIMAL FORMAT):");
        strTable.append(header);

        strTable.append("\nAMERICAN FOOTBALL CONFERENCE (AFC)\n");
        strTable.append("AFC EAST\n");

        // Initialize the counters correctly
        int divisionCounter = 0;
        int conferenceCounter = 1; // Start with the first conference (AFC)

        // Define the format string for each line of team data
        String lineFormat = "%-30s %-15s %.3f\n";

        for(TeamData team: teams){
            divisionCounter++; // Increment divisionCounter for each team

            // Add division headers
            //For AFC
            if(conferenceCounter == 1){ // For the AFC
                if(divisionCounter == 5){
                    strTable.append("\nAFC NORTH\n");
                } else if(divisionCounter == 9){
                    strTable.append("\nAFC SOUTH\n");
                } else if(divisionCounter == 13){
                    strTable.append("\nAFC WEST\n");
                }
            } else { // For the NFC
                if(divisionCounter == 1){
                    strTable.append("\nNATIONAL FOOTBALL CONFERENCE (NFC)\n");
                    strTable.append("NFC EAST\n");
                } else if(divisionCounter == 5){
                    strTable.append("\nNFC NORTH\n");
                } else if(divisionCounter == 9){
                    strTable.append("\nNFC SOUTH\n");
                } else if(divisionCounter == 13){
                    strTable.append("\nNFC WEST\n");
                }
            }

            // Append the team information formatted to line up with the headers
            strTable.append(String.format(lineFormat, team.name, team.strRecord(), team.winPct));

            // Check if it's time to switch conferences
            if(divisionCounter == 16){
                divisionCounter = 0; // Reset division counter for NFC
                conferenceCounter++; // Increment conference counter to switch to NFC
            }
        }

        return strTable.toString(); //convert it to a String object
    }


    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.footballdb.com/standings/index.html").get();
            List<TeamData> teams = new ArrayList<>(); // Create a list to store team data.

            // Select all tables with the class "statistics"
            Elements tables = doc.select("table.statistics");

            // Go through each table and get team data
            for (Element table : tables) {
                // For each table, go through each row inside the body of the table
                for (Element row : table.select("tbody tr")) {
                    // In each row, get all the cells
                    Elements cols = row.select("td");

                    // Get the team's name and numbers from the cells
                    String teamName = row.select("td.left span.hidden-xs").text();
                    int wins = Integer.parseInt(cols.get(1).text());
                    int losses = Integer.parseInt(cols.get(2).text());
                    int draws = Integer.parseInt(cols.get(3).text());
                    double winPercentage = Double.parseDouble(cols.get(4).text());

                    // Create a new TeamData object with the extracted data
                    teams.add(new TeamData(teamName, wins, losses, draws, winPercentage));
                }
            }

//            Print out the list of team data for testing
//            for (TeamData team : teams) {
//                System.out.println(team);
//            }

            String strTable = displayTable(teams);
            System.out.println(strTable);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}