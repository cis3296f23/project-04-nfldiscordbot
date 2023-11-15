package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Fetches upcoming NFL schedule (times and teams playing)
 */

public class NFL_Schedule {
    public static void main(String[] args) {
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

            /*
            #leftcol > div:nth-child(10) > h2
            #leftcol > div:nth-child(14) > h2
            #leftcol > div:nth-child(35) > h2
             */
            //Select all div elements with class "divider"
            Elements dividerDivs = doc2.select("div.divider");

            //Loop through each div and to get each of the dates
            int n = 0;
            String date1 = "", date2 = "", date3 = "";
            for (Element dividerDiv : dividerDivs) {
                //Find the h2 element within the current div
                Element h2Element = dividerDiv.selectFirst("h2");

                if (h2Element != null) {
                    String h2Text = h2Element.text();
                    //#leftcol > div:nth-child(12)
                    //#leftcol > div:nth-child(16)
                    //#leftcol > div:nth-child(17)
                    if(n == 0){date1 = h2Element.text();}
                    if(n == 1){date2 = h2Element.text();}
                    if(n == 2){date3 = h2Element.text();}
                }
                n++;
            }

            System.out.println("Upcoming " + year + " NFL Schedule - Week " + upcomingWeekNum + "\n");

            String thisDiv, thisDivNum;
            //Games during first date of the week
            if(!date1.equals("")){
                System.out.println(date1);

                thisDivNum = "12";
                thisDiv = "#leftcol > div:nth-child(" + thisDivNum + ") > div > div";
                getTeamsAndTime(doc2, thisDiv);
            }

            //Games during second date of the week
            if(!date2.equals("")){
                System.out.println(date2);

                //16, 17, 19, 20, 22, 23, 25, 26, 28, 29, 31, 32
                int j = 2;
                for(int i = 16; i <= 32; i += j){
                    thisDivNum = String.valueOf(i);
                    thisDiv = "#leftcol > div:nth-child(" + thisDivNum + ") > div > div";
                    getTeamsAndTime(doc2, thisDiv);
                    if(j == 1){
                        j = 2;
                    } else{
                        j = 1;
                    }
                }
            }

            //Games during third date of the week
            if(!date3.equals("")){
                System.out.println(date3);

                thisDivNum = "36";
                //#leftcol > div:nth-child(37) > div > div > div > b
                thisDiv = "#leftcol > div:nth-child(" + thisDivNum + ") > div > div";
                getTeamsAndTime(doc2, thisDiv);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getTeamsAndTime(Document doc, String thisDiv){
        Element teamDiv = doc.select(thisDiv + " > div > b").first();
        String teams = teamDiv.text();
        System.out.println("\t" + teams);

        Element timeDiv = doc.select(thisDiv).first();
        String thisText = timeDiv.text(), time = "";
        int length = teams.length() + 1;

        int whitespaces = 0;
        for(int i = length; whitespaces < 3; i++){
            time += thisText.charAt(i);
            if(thisText.charAt(i) == ' '){whitespaces++;}
            if(whitespaces == 3){break;}
        }
        System.out.println("\t" + time + "\n");
    }
}
