import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.net.*;
import com.google.gson.*;
    
    
public class Solution {
    static String stockResults = "";
    static String output = null;
    static ArrayList<String> ar = new ArrayList<String>();
    static String outputStockData = "";
    static StringBuilder sb = new StringBuilder();
    static String day = null;
    static String[] daysOfTheWeek = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    static List<String> daysList = Arrays.asList(daysOfTheWeek); 
    
    static boolean validateInputs(String firstDate, String lastDate, String weekDay){
        try
        { 
            if( daysList.contains(weekDay) &&  (weekDay != null) && (firstDate != null) && (lastDate != null)){
                return true;
            } else {
                return false;
            }
        } 
        catch(NullPointerException e) 
        { 
            return false; 
        } 
    }

    static void openAndClosePrices(String firstDate, String lastDate, String weekDay) { 
        boolean result = validateInputs(firstDate, lastDate, weekDay);    
        if(result == true){
            try {
                SimpleDateFormat sdfo = new SimpleDateFormat("dd-MMM-yyyy");
                Date fDate = sdfo.parse(firstDate);
                Date lDate = sdfo.parse(lastDate);

                if (lDate.compareTo(fDate) <= 0)
                    return ;

                stockResults = getStocksData();

                if( (stockResults != null) ){
                    
                    Calendar c = Calendar.getInstance();
                    sdfo.setLenient(false);
                    
                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(stockResults).getAsJsonObject();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("data");

                    for (JsonElement pa : jsonArray) {
                        JsonObject obj = pa.getAsJsonObject();
                        String dd = obj.get("date").getAsString();

                        if (null == dd ||  dd.isEmpty())
                            continue;

                        Date aDate = sdfo.parse(dd);

                        if( (aDate.compareTo(fDate) >= 0) && (aDate.compareTo(lDate) <= 0) ){
                            c.setTime(aDate);
                            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                            String dInWord = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
                            if( weekDay.equalsIgnoreCase(dInWord) ){
                                String stockDate = obj.get("date").getAsString();
                                String stockOpen = obj.get("open").getAsString();
                                String stockClose = obj.get("close").getAsString();
                                if( (stockDate != null) && (stockOpen != null) && (stockClose != null) ){
                                    outputStockData = stockDate + " " + stockOpen + " " + stockClose;
                                    System.out.println(outputStockData); 
                                }
                            }
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println(e);
            }catch (ParseException e) {
                System.out.println(e);
            } catch (NumberFormatException ex){
                System.out.println(ex);
            } catch (NullPointerException exc) {
                System.out.println(exc);
            } catch (JsonParseException err){
                System.out.println(err);
            }
            
        }
    }

    static String getStocksData() throws IOException {
        try {
            URL url = new URL("https://jsonmock.hackerrank.com/api/stocks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));
            output = br.readLine();
            conn.disconnect();
            return output;

        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String _firstDate;
        try {
            _firstDate = in.nextLine();
        } catch (Exception e) {
            _firstDate = null;
        }
        
        String _lastDate;
        try {
            _lastDate = in.nextLine();
        } catch (Exception e) {
            _lastDate = null;
        }
        
        String _weekDay;
        try {
            _weekDay = in.nextLine();
        } catch (Exception e) {
            _weekDay = null;
        }
        
        openAndClosePrices(_firstDate, _lastDate, _weekDay);
        
    }
}
