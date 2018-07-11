package edu.harvard.integration.Trello;

import com.github.cliftonlabs.json_simple.JsonArray;
import edu.harvard.integration.Integrator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {
    public static List<Map<String, Object>> getInfo(JsonArray arr){
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (Object aList : arr) {
            Map<String, Object> item = (Map<String, Object>) aList;
            Map<String, Object> map = new HashMap<String, Object>();
            String name = (String) item.get("name");
            String[] pattern = Commons.extractPts(name);
            map.put("pts", pattern[0]);
            map.put("name", pattern[1]);
            map.put("id", item.get("id"));
            res.add(map);
        }
        return res;
    }

    public static String[] extractPts(String name){
        Pattern pattern = Pattern.compile("^\\([0-9]+\\)");
        Matcher matcher = pattern.matcher(name);
        String pts = "";
        if(matcher.find()) pts = matcher.group();
        pts = pts.replace("(", "");
        pts = pts.replace(")", "");
        name = name.replaceFirst("^\\([0-9]+\\)\\s+", "");
        return new String[]{pts, name};
    }

    public static String getPageSource(String pageUrl) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(pageUrl);
            String encoding = "UTF-8";

            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream(), encoding));
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            in.close();
        } catch (Exception ignored) {

        }
        return sb.toString();
    }

    public static String getTrelloToken(){
        return Integrator.getConfig().getOrDefault("TRELLO_TOKEN", "trello_token");
    }

    public static String getTrelloKey(){
        return Integrator.getConfig().getOrDefault("TRELLO_KEY", "trello_key");
    }

    public static String formatPBI(Map<String, Object> pbi){
        String name = (String) pbi.get("name");
        String pts = (String) pbi.get("pts");
        String due = (String) pbi.get("due");
        String list = (String) pbi.get("list_name");
        return "Name: " + name + "\nStory points: " + pts + "\nBelongs to list: " +
                list + "\nDue at: " + due + "\n\n";
    }

    public static String concatArr(String[] arr){
        if(arr.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for(String s : arr){
            sb.append(s);
            sb.append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static List<Map<String, Object>> getCardsDetails(List<Map<String, Object>> cardsInfo){
        List<Map<String, Object>> result = new ArrayList<>();
        for(Map<String, Object> card : cardsInfo){
            String cardId = (String) card.get("id");
            Map<String, Object> cardDetail = CardUtils.getCardContent(cardId);
            String cardName = (String) cardDetail.get("name");
            String[] pattern = Commons.extractPts(cardName);
            cardDetail.put("pts", pattern[0]);
            cardDetail.put("name", pattern[1]);
            result.add(cardDetail);
        }
        return result;
    }
}
