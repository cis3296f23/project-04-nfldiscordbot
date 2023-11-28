package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Obtains overall team statistics for the entire season
 */

public class TeamStatistics {
    public static String getTeamStatistics(String param){
        String result = "";

        //Turn dashes into spaces for comparing
        char[] paramArray = param.toCharArray();
        for(int i = 0; i < param.length(); i++){
            if(paramArray[i] == '-'){paramArray[i] = ' ';}
        }
        param = new String(paramArray);

        try{
            //Stats page for offense
            String url = "https://www.footballdb.com/stats/teamstat.html?lg=NFL&yr=2023&type=reg&cat=T&group=O&conf=";
            Document doc = Jsoup.connect(url).get();

            //Print team name when retrieving offense stats
            boolean isOffense = true;

            //Get stats for offense
            result = getStats(param, doc, isOffense, result);

            //Stats page for defense
            url = "https://www.footballdb.com/stats/teamstat.html?lg=NFL&yr=2023&type=reg&cat=T&group=D&conf=";
            doc = Jsoup.connect(url).get();

            isOffense = false;
            System.out.print("\n");

            //Get stats for defense
            result = getStats(param, doc, isOffense, result);

        } catch(Exception e) {
            e.printStackTrace();
        }

        if(result.equals("")){result = "Team not found.";}
        return result;
    }

    private static String getStats(String param, Document doc, boolean isOffense, String result){
        try{
            //Get all team names and compare them to input to find specified team
            Elements trElements = doc.select("tr.row0.right, tr.row1.right");
            for(Element team : trElements){
                Elements tdElements = team.select("td");
                Element tdLeftNoWrap = tdElements.first();
                Element span = tdLeftNoWrap.selectFirst("span.hidden-xs");
                Element a = span.selectFirst("a");
                String teamName = a.text();
                if(teamName.equalsIgnoreCase(param)) {
                    if(isOffense){
                        result += "# __**" + teamName + " Stats**__\n\n# __***Offense***__\n";
                    } else{
                        result += "# __***Defense***__\n";
                    }

                    //Gms
                    Element gms = tdElements.get(1);
                    result += "__**Gms**__\n" + gms.text() + "\n";

                    //Tot Pts
                    Element totPts = tdElements.get(2);
                    result += "__**Tot Pts**__\n" + totPts.text() + "\n";

                    //Pts/G
                    Element ptsPerG = tdElements.get(3);
                    result += "__**Pts/G**__\n" + ptsPerG.text() + "\n";

                    //RushYds
                    Element rushYds = tdElements.get(4);
                    result += "__**RushYds**__\n" + rushYds.text() + "\n";

                    //RYds/G
                    Element rYdsPerG = tdElements.get(5);
                    result += "__**RYds/G**__\n" + rYdsPerG.text() + "\n";

                    //PassYds
                    Element passYds = tdElements.get(6);
                    result += "__**PassYds**__\n" + passYds.text() + "\n";

                    //PYds/G
                    Element pYdsPerG = tdElements.get(7);
                    result += "__**PYds/G**__\n" + pYdsPerG.text() + "\n";

                    //TotYds
                    Element totYds = tdElements.get(8);
                    result += "__**TotYds**__\n" + totYds.text() + "\n";

                    //Yds/G
                    Element ydsPerG = tdElements.get(9);
                    result += "__**Yds/G**__\n" + ydsPerG.text() + "\n";
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
