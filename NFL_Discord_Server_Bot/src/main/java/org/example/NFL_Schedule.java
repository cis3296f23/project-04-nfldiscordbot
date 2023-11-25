package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Fetches upcoming NFL schedule (times and teams playing)
 */

public class NFL_Schedule {
    public static String schedule() {
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

            //Get the year
            String year = preview.substring(0, 4);

            //https://www.footballdb.com/games/previews.html?yr=2023&wk=9&type=reg
            //Create url to the upcoming week
            StringBuilder url2 = new StringBuilder("https://www.footballdb.com/games/previews.html?yr=");
            url2.append(year + "&wk=" + weekNum + "&type=reg");

            Document doc2 = Jsoup.connect(url2.toString()).get();

            //Select all div elements with class "divider"
            Elements allDivs = doc2.select("div");

            result += "__**Upcoming " + year + " NFL Schedule - Week " + weekNum + "**__\n";

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
