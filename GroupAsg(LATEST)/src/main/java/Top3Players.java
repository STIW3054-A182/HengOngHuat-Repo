
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Properties;
import java.util.concurrent.Callable;

public class Top3Players extends CheckValidTable implements Callable {

    private String link;

    public Top3Players(String link) {
        this.link = link;
    }

    @Override
    public String call() {
        PropertyFileWriting obj = new PropertyFileWriting();
        Properties write = obj.setProperties();
        String prop1 = write.getProperty("Top1");
        String prop2 = write.getProperty("Top2");
        String prop3 = write.getProperty("Top3");
        String format = "| %-5s | %-5s | %-35s| %-8s| %-15s| %-8s| %-8s|\n";

        Document doc;
        try {
            doc = Jsoup.connect(link).get();

            String title = doc.title();
            int scrape = title.indexOf("9");
            String category = title.substring(scrape + 1).replace("(", "").replace(")", "");

            Element sub = doc.select("div.defaultDialog").get(1); //subtitle
            Elements subt = sub.select("h2");
            String subtitle = subt.text();
            if (category.equals("")){
                System.out.println(subtitle);
                System.out.println("");
            }else{
                System.out.println(subtitle+" "+"("+category+" )");
                System.out.println("");
            }

            System.out.format(format, "RK", "SNo", "Name", "Rtg", "State", "Pts", "Category");
            System.out.format(format, "-----", "-----", "-----------------------------------",
                    "--------", "---------------", "--------", "--------");

            for (Element row : doc.select("table.CRs1 tr")) {
                final String rk = row.select("td:nth-of-type(1)").text();
                final String sno = row.select("td:nth-of-type(2)").text();
                final String name = row.select("td:nth-of-type(4)").text();
                final String rtg = row.select("td:nth-of-type(6)").text();
                final String state = row.select("td:nth-of-type(7)").text();
                final String pointer = row.select("td:nth-of-type(8)").text();

                if (rk.equals(prop1)) {
                    System.out.format(format, rk, sno, name, rtg, state, pointer, category);
                } else if (rk.equals(prop2)) {
                    System.out.format(format, rk, sno, name, rtg, state, pointer, category);
                } else if (rk.equals(prop3)) {
                    System.out.format(format, rk, sno, name, rtg, state, pointer, category);
                }
            }
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return link;
    }
}
