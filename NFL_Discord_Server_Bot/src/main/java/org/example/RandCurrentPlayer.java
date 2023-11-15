package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Random;

/**
 * Lists a random current player
 */
public class RandCurrentPlayer {
    public static String getPlayer() {
        String player = "";
        try {
            String url = "https://www.footballdb.com/players/current.html";
            Document doc = Jsoup.connect(url).get();

            // Select all 'a' elements within the specified structure
            Elements aElements = doc.select("#leftcol .divtable-striped a");

            // Generate a random number to choose an 'a' element
            Random random = new Random();
            int randomNum = random.nextInt(aElements.size());

            //If randomNum is odd, the 'a' element is a place rather than a player
            //Subtract 1 from randomNum to make it even, making the 'a' element reference to a player
            if(randomNum % 2 != 0){
                randomNum--;
            }
            Element randomAElement = aElements.get(randomNum);

            //Print the random name
            String name = randomAElement.text();
            player += "Random current player: " + name;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return player;
    }
}
