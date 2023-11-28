package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class League {

    public String displayTable(List<TeamData> teams){
        StringBuilder strTable = new StringBuilder("# NFL LEAGUE STANDINGS\n\n");

        String header = String.format("%-30s %-15s %s\n\n", "**TEAM NAME:**", "**RECORD:**", "**WIN PERCENTAGE (IN DECIMAL FORMAT):**");
        strTable.append(header);

        strTable.append("\n## AMERICAN FOOTBALL CONFERENCE (AFC)\n");
        strTable.append("### AFC EAST\n");

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
                    strTable.append("\n### AFC NORTH\n");
                } else if(divisionCounter == 9){
                    strTable.append("\n### AFC SOUTH\n");
                } else if(divisionCounter == 13){
                    strTable.append("\n### AFC WEST\n");
                }
            } else { // For the NFC
                if(divisionCounter == 1){
                    strTable.append("\n## NATIONAL FOOTBALL CONFERENCE (NFC)\n");
                    strTable.append("### NFC EAST\n");
                } else if(divisionCounter == 5){
                    strTable.append("\n### NFC NORTH\n");
                } else if(divisionCounter == 9){
                    strTable.append("\n### NFC SOUTH\n");
                } else if(divisionCounter == 13){
                    strTable.append("\n### NFC WEST\n");
                }
            }

            // Append the team information formatted to line up with the headers
            strTable.append(String.format(lineFormat, "**" + team.name + "**", team.strRecord(), team.winPct));

            // Check if it's time to switch conferences
            if(divisionCounter == 16){
                divisionCounter = 0; // Reset division counter for NFC
                conferenceCounter++; // Increment conference counter to switch to NFC
            }
        }

        return strTable.toString(); //convert it to a String object
    }


    public String getLeagueStandings() {

        String strTable="";

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

            strTable = displayTable(teams);
//            System.out.println(strTable);

            return strTable;

        } catch (IOException e) {
            e.printStackTrace();
        };

        return strTable;
    }

    public String getLeagueSchedule() {
        String result = "";
        try {
            String url = "https://www.footballdb.com/games/previews.html";
            Document doc = Jsoup.connect(url).get();

            Element previewElement = doc.select("#leftcol > h1").first();

            String preview = previewElement.text();

            //Get digits that make up the week number
            //E.g. If it is Week 14 of the season, digit1 = 1 and digit2 = 4
            char digit1 = preview.charAt(preview.length() - 2);
            char digit2 = preview.charAt(preview.length() - 1);

            //Get week number, which could be a single-digit or double-digit number
            //If week number is single-digit number, use only second digit since first digit is whitespace
            //If week number is double-digit, both digits make up the number
            String weekNum;
            if(digit1 == ' '){
                weekNum = String.valueOf(digit2);
            } else{
                weekNum = String.valueOf(digit1) + String.valueOf(digit2);
            }

            //Get the number of the upcoming week
            int upcomingWeekNumInt = Integer.parseInt(weekNum) + 1;
            String upcomingWeekNum = Integer.toString(upcomingWeekNumInt);

            //Get the year
            String year = preview.substring(0, 4);

            //https://www.footballdb.com/games/previews.html?yr=2023&wk=9&type=reg
            //Create url to the upcoming week
            StringBuilder url2 = new StringBuilder("https://www.footballdb.com/games/previews.html?yr=");
            url2.append(year + "&wk=" + upcomingWeekNum + "&type=reg");

            Document doc2 = Jsoup.connect(url2.toString()).get();

            //Select all div elements with class "divider"
            Elements allDivs = doc2.select("div");

            result += "__**Upcoming " + year + " NFL Schedule - Week " + upcomingWeekNum + "**__\n";

            for (Element thisDiv : allDivs) {
                if (thisDiv.hasClass("divider")) {
                    Element h2Element = thisDiv.selectFirst("h2");
                    if (h2Element != null) {
                        //Print date
                        result += "**" + h2Element.text() + "**\n";
                    }
                } else if (thisDiv.hasClass("section_half")) {
                    Element nestedDiv = thisDiv.select("div").first();
                    Element bElement = nestedDiv.select("b").first();

                    //Print teams
                    result += bElement.text() + "\n";

                    //Print time
                    int length = bElement.text().length() + 1;
                    String time = "";
                    int whitespaces = 0;
                    for(int i = length; whitespaces < 3; i++){
                        time += nestedDiv.text().charAt(i);
                        if(nestedDiv.text().charAt(i) == ' '){whitespaces++;}
                        if(whitespaces == 3){break;}
                    }
                    result += time + "\n";
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}