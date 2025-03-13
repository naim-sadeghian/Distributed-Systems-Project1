package ds.project1task3;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ParksHandler {
    private static final String base_url = "developer.nps.gov/api/v1";
    private Map<String, Park> parks;

    public String getName(String parkCode) {
        return parks.get(parkCode).name;
    }

    //    Park information struct
//    Used CHAT GPT to create park struct
    class Park {
        String name;
        String latitude;
        String longitude;

        public Park(String name, String latitude, String longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    ParksHandler() {
        parks = new HashMap<>();
        parks.put("acad", new Park("Acadia", "44.3962", "-68.2246"));
        parks.put("cuva", new Park("Cuyahoga Valley", "41.0969", "-81.4611"));
        parks.put("grsm", new Park("Great Smoky Mountains", "35.726", "-83.482"));
        parks.put("maca", new Park("Mammoth Cave", "37.1246", "-86.0968"));
        parks.put("neri", new Park("New River Gorge", "37.9263", "-81.1547"));
        parks.put("shen", new Park("Shenandoah", "38.6633", "-78.4635"));
    }

//    Get the names of all of the park in order to print them in the html
    public String[] getParkNames() {
        String[] parkNames = new String[parks.size()];
        int i = 0;
        for (Park park : parks.values()) {
            parkNames[i] = park.name;
            i++;
        }
        return parkNames;
    }


//    Returns URL of image to be loaded, returns null if none is found
//    The Scraping functions using Jsoup were created by Chat GPT with the prompt:
    //    I want to use jsoup to request using validateTLSCertificates(false) the url:
    //    String url = "https://www.nps.gov/"+park_code+"/index.html";
    //    Once the html Is brought I want to get the first element of the class "picturefill-background". Here is an example of what it looks like: I specifically want to extract the url to the image
    //
    //<div class="picturefill-background" role="img" style="background-image: url(&quot;/common/uploads/banner_image/akr/homepage/0E2033EC-9C99-C8C7-A8BF29BB1EA11146.jpg?mode=crop&amp;quality=90&amp;width=2400&amp;height=700&quot;); background-size: cover; background-repeat: no-repeat; background-position: 50% 50%;" aria-label="Snow covering the ground at the historic entrance to Mammoth Cave. ">
    //....
    //</div>
    public String getParkImage(String park_code) {
        String url = "https://www.nps.gov/" + park_code + "/index.htm";
        String imageUrl = "https://www.nps.gov/";

        try {
            // Fetch the webpage with SSL validation disabled
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
            // Select the first element with class 'picturefill-background'
            Element imageSpan = doc.selectFirst(".picturefill-background span[data-src]");
            if (imageSpan != null) {
                // Extract from the 'span' attribute
                imageUrl += imageSpan.attr("data-src");

                // Print the extracted image URL
//                System.out.println("Image URL: " + imageUrl);
            } else {
                System.out.println("No image found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }


//    Taken From Chat GPT using the following prompt
//        Using java and gson to accept the response how do I replicate the following request done in python:
//        import urllib.request, json
//        # Configure API request endpoint = "https://developer.nps.gov/api/v1/parks?stateCode=me"
//        HEADERS = {"X-Api-Key":"INSERT_API_KEY_HERE"}
//        req = urllib.request.Request(endpoint,headers=HEADER

    public List<String> getParkActivites(String park_code) {
        String endpoint = "https://developer.nps.gov/api/v1/parks?parkCode="+park_code;
        String apiKey = "kJzRqzFhZZRf0vyGyRfAd015fHsFuha3Fzr4uYH1";
        String urlString = endpoint + "&apiKey=" + apiKey;
        List<String> activites = new ArrayList<>();

        try {
            // Create URL object from endpoint
            URL url = new URL(urlString);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Api-Key", apiKey);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response using Gson
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            // Get the 'data' array
            JsonArray dataArray = jsonResponse.getAsJsonArray("data");

            // Assuming only 1 park, get the first park object (index 0)
            JsonObject park = dataArray.get(0).getAsJsonObject();

            // Get the 'activities' array for the first park
            JsonArray acts = park.getAsJsonArray("activities");

            // Iterate over the activities
            for (int i = 0; i < acts.size(); i++) {
                JsonObject activity = acts.get(i).getAsJsonObject();
                activites.add( activity.get("name").getAsString() ) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(activites);
        return activites;
    }

//    Get climate info
    public String[] getWeatherInfo(String park_code) {
        String lat = parks.get(park_code).latitude;
        String lon = parks.get(park_code).longitude;
        String[] info = new String[3];
        String url = "https://forecast.weather.gov/MapClick.php?textField1="+lat+"&textField2="+lon;
        try {
            // Fetch the webpage with SSL validation disabled
            Document doc = Jsoup.connect(url).get();
            Element tempElement = doc.selectFirst(".myforecast-current-lrg");

            Element table = doc.getElementById("current_conditions_detail");

            if (tempElement != null && table != null) {
                Elements rows = table.getElementsByTag("td");
                info[1] = rows.get(0).text() + ": " + rows.get(1).text(); // Humidity
                info[2] = rows.get(2).text() + ": " + rows.get(3).text(); // Wind Speed
                info[0] = tempElement.text();

            } else {
                System.out.println("Weather info not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

}
