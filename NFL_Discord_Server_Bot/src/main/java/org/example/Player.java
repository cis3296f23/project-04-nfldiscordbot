package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Player {

    public static String findPlayer(String playerName){

        int i = 1;
        boolean foundPlayer = false;
        String url = "";
        String retrievedName = "name";
        StringBuilder result = new StringBuilder();

        try {
            while (!retrievedName.equals("")){
                //if the retrieved name matches 
                url = formatPlayerUrl(playerName, i);
                Document doc = Jsoup.connect(url).get();
                retrievedName = getRetrievedName(doc).toLowerCase();

                if (retrievedName.equals(playerName)){
                    result.append(getPlayerInfo(playerName, url, doc));
                    foundPlayer = true;
                }
                
                i++;
            }
            if (!foundPlayer){
                result.append("Player not found.");
            }
            
            //while the URL is valid
            
        }  catch (IOException e){
            e.printStackTrace();
            result.append("Error occured while retrieving player information.");
        }

        return result.toString();
          
    }

    public static String getPlayerInfo(String playerName, String url, Document doc) {

        StringBuilder playerInfo = new StringBuilder();
        String position = getPosition(doc);
        String team = getTeam(doc);
        String retreivedName = getRetrievedName(doc);
        Element statTable = getStatTable(url, doc, position);

        if (statTable == null) {
            return playerInfo.toString();
        }
        Elements lastRow = getLastRow(statTable);

        playerInfo.append("\n**Name:** " ).append(retreivedName).append("\n");
        playerInfo.append("**Position:** " ).append(position).append("\n");
        playerInfo.append("**Team:** ").append(team).append("\n");
        
        if (position.equals("QB")){
            //completion percentage, passing yards, passing touchdowns, and interceptions
            double completionPercentage = Double.parseDouble(lastRow.get(7).text());

            String yardsWithCommas = lastRow.get(8).text();
            String yardsWithoutCommas = yardsWithCommas.replace(",", "");
            int yards = Integer.parseInt(yardsWithoutCommas);
            int passingTouchdowns = Integer.parseInt(lastRow.get(11).text());
            int interceptions = Integer.parseInt(lastRow.get(13).text());  

            playerInfo.append("**Completion Percentage:** ").append(completionPercentage).append("%\n");
            playerInfo.append("**Passing Yards:** ").append(yards).append("\n");
            playerInfo.append("**Passing Touchdowns:** ").append(passingTouchdowns).append("\n");
            playerInfo.append("**Interception:** ").append(interceptions).append("\n");

        } else if (position.equals("WR") || position.equals("TE")) {
            //receptions, yards, touchdowns
            int receptions = Integer.parseInt(lastRow.get(5).text());
            String yardsWithCommas = lastRow.get(6).text();
            String yardsWithoutCommas = yardsWithCommas.replace(",", "");
            int yards = Integer.parseInt(yardsWithoutCommas);
            int touchdowns = Integer.parseInt(lastRow.get(10).text());
            
            playerInfo.append("**Receptions:** ").append(receptions).append("\n");
            playerInfo.append("**Receiving Yards:** ").append(yards).append("\n");
            playerInfo.append("**Touchdowns:** ").append(touchdowns).append("\n");

        } else if (position.equals("RB")) {
            //Attempts, Yards, Touchdowns
            int attempts = Integer.parseInt(lastRow.get(5).text());

            String yardsWithCommas = lastRow.get(6).text();
            String yardsWithoutCommas = yardsWithCommas.replace(",", "");
            int yards = Integer.parseInt(yardsWithoutCommas);
            int touchdowns = Integer.parseInt(lastRow.get(10).text());
        
            playerInfo.append("**Attempts:** ").append(attempts).append("\n");
            playerInfo.append("**Rushing Yards:** ").append(yards).append("\n");
            playerInfo.append("**Touchdowns:** ").append(touchdowns).append("\n");


        //Defensive player
        } else if (position.equals("LB") || position.equals("DB") || position.equals("LB") ||
            position.equals("DT") || position.equals("DE")) {
            //total tackles, sacks, interceptions, pass deflected

            int totalTackles = Integer.parseInt(lastRow.get(12).text());
            double sacks = Double.parseDouble(lastRow.get(13).text());
            int passDeflected = Integer.parseInt(lastRow.get(15).text());
            int interceptions = Integer.parseInt(lastRow.get(5).text());
          
            playerInfo.append("**Total Tackles:** ").append(totalTackles).append("\n");
            playerInfo.append("**Sacks:** ").append(sacks).append("\n");
            playerInfo.append("**Passes Deflected:** ").append(passDeflected).append("\n");
            playerInfo.append("**Interceptions** ").append(interceptions).append("\n");

        //Kicker
        } else if (position.equals("K")) {

            //field goal percentage , extra point percentage, longest kick
            double fieldGoalPercentage = Double.parseDouble(lastRow.get(7).text());
            int longestMade = Integer.parseInt(lastRow.get(13).text());
            double extraPointPercentage = Double.parseDouble(lastRow.get(16).text());
          
            playerInfo.append("**Field Goal Percentage:** ").append(fieldGoalPercentage).append("\n");
            playerInfo.append("**Extra Point Percentage:** ").append(extraPointPercentage).append("\n");
            playerInfo.append("**Longest Field Goal Made:** ").append(longestMade).append("\n");

        //Punter
        } else if (position.equals("P")) {
            //average punt yards, longest, inside the 20

            double averageYards = Double.parseDouble(lastRow.get(7).text());
            int longestPunt = Integer.parseInt(lastRow.get(8).text());
            int inside20 = Integer.parseInt(lastRow.get(10).text());
            
            playerInfo.append("**Average Yards Per Punt:** ").append(averageYards).append("\n");
            playerInfo.append("**Longest Punt:** ").append(longestPunt).append("\n");
            playerInfo.append("**Punts Inside The 20:** ").append(inside20).append("\n");
        }

        return playerInfo.toString();

    }   
    

    public static Element getStatTable(String url, Document doc, String position) {
        // Select the table body containing player stats
        Element statTable = null;
        if (position.equals("WR") || position.equals("TE")) {
            // select receiving table
            statTable = doc.select("#divToggle_C_reg > table.statistics tbody").last();

        } else if (position.equals("QB")){
            //select passing table
            statTable = doc.select("#divToggle_P_reg > table.statistics tbody").first();
            
        } else if (position.equals("RB")) {
            //select rushing table
            statTable = doc.select("#divToggle_R_reg > table.statistics tbody").first();

        } else if (position.equals("K")) {
            //select kicking table
            statTable = doc.select("#divToggle_K_reg > table.statistics tbody").first();

        } else if (position.equals("P")) {
            //select punting table
            statTable = doc.select("#divToggle_U_reg > table.statistics tbody").first();

        } else {
            //select defense table
            statTable = doc.select("#divToggle_D_reg > table.statistics tbody").first();
        }

        return statTable;
    }

    public static String getPosition(Document doc){
        // Check if the element is found
        Element playerBanner = doc.select("div#playerbanner").first();

        // Check if the player banner element is found
        if (playerBanner != null) {
            // Select the b element containing the position within the player banner
            Element positionElement = playerBanner.select("b:contains(Position)").first();
            // Check if the position element is found
            if (positionElement != null) {
                // Get the text content of the next sibling, which contains the position
                String position = positionElement.nextSibling().toString().trim();
                return position;
            } else {
                return "Position element not found";
            }
        } else {
            return "Player banner element not found";
        }
    }

    public static String getTeam(Document doc){
        // Check if the element is found
        Element playerBanner = doc.select("div#playerbanner").first();

        // Check if the player banner element is found
        if (playerBanner != null) {
            // Select the b element containing the position within the player banner
            Element positionElement = playerBanner.select("#playerbanner > b:nth-child(3) > a").first();
            // Check if the position element is found
            if (positionElement != null) {
                // Get the text content of the next sibling, which contains the position
                return positionElement.text();
            } else {
                return "team not found";
            }
        } else {
            return "team not found";
        }
    }

    public static String getRetrievedName(Document doc){
        Element positionElement =  doc.select("#playertop > h1").first();
        return positionElement.text();
    }

    public static Elements getLastRow(Element statTable) {
        Elements rows = statTable.select("tr");
        int thirdToLastRowIndex = rows.size() - 3;
        if (thirdToLastRowIndex >= 0) {
            Element thirdToLastRow = rows.get(thirdToLastRowIndex);
            // Select all cells within the third-to-last row (2023 season stats)
            return thirdToLastRow.select("td");
        } else {
            System.out.println("failed to retrieve last row.");
            return null;
        }
    }


    public static String formatPlayerUrl(String playerName, int i) {

        // Remove periods, hyphens, and apostrophes from players name
        String strippedName = playerName.replace(".", "").replace("'","").replace("-","");

        // Convert the player name to lowercase
        String lowercaseName = strippedName.toLowerCase();
        // Replace spaces with hyphens
        String hyphenatedName = lowercaseName.replace(" ", "-");

        // Extract first five letters of last name and first two letters of the first name
        String[] nameParts = lowercaseName.split(" ");

        String lastName = "";

        if (nameParts.length > 2){
            lastName = nameParts[nameParts.length - 2] + nameParts[nameParts.length - 1];
        } else {
            lastName = nameParts[nameParts.length - 1];
        }

        // Ensure that the last name has at least 5 characters before extracting
        if (lastName.length() > 5) {
            // Extract the first 5 letters
            lastName = lastName.substring(0, 5);
        }
        String firstNameAbbreviation = nameParts[0].substring(0, Math.min(2, nameParts[0].length()));
        String uniqueIdentifier = "";
        // Generate the unique identifier
        if (i < 10){
            uniqueIdentifier = lastName + firstNameAbbreviation + "0" + i;
        } else {
            uniqueIdentifier = lastName + firstNameAbbreviation + i;
        }

        // Build the formatted URL
        String formattedUrl = "https://www.footballdb.com/players/" + hyphenatedName + "-" + uniqueIdentifier;
        System.out.println(formattedUrl);
        return formattedUrl;
    }

    public static void main(String[] args) {
        // findPlayer("Jalen Hurts");
        // findPlayer("Haason Reddick");
        // findPlayer("Darius Slay");
        // findPlayer("A.J. Brown");
        // findPlayer("Travis Kelce");
        // findPlayer("Christian McCaffrey");
        // findPlayer("Jake Elliott");
        // findPlayer("Braden Mann");
        // findPlayer("Jason Kelce");
        // findPlayer("Josh Allen");
        // findPlayer("Amon-Ra St. Brown");

    }

}
