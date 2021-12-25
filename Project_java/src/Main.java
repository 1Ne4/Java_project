import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import java.sql.*;
import java.util.*;

public class Main {

    //Основной метод приложения
    public static void main(String[] args) throws IOException {
        String filePath = "Показатель счастья по странам 2017.csv";
        List<Country> countries = ParseCountryCsv(filePath);


        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:project.db");
            System.out.println("Connected");
            Statement statement = connection.createStatement();
            statement.execute("Drop table if exists Project");
            statement.execute("CREATE TABLE Project(" +
                    "ID integer PRIMARY key AUTOINCREMENT," +
                    "Country text," +
                    "Region text," +
                    "HappinessRank integer," +
                    "HappinessScore double," +
                    "WhiskerHigh double ," +
                    "WhiskerLow double ," +
                    "EconomyGDPPerCapita double ," +
                    "Family double ," +
                    "HealthLifeExpectancy double ," +
                    "Freedom double ," +
                    "Generosity double ," +
                    "TrustGovernmentCorruption double, " +
                    "DystopiaResidual double )");

            countries.forEach(country -> {
                var query = country.toString();

                try (PreparedStatement stat = connection.prepareStatement(
                        "INSERT INTO Project " +
                                "VALUES(?, ?, ?,?,?,?,?,?,?,?,?,?,?,?)")) {
                    stat.setObject(1,null);
                    stat.setObject(2, country.Country);
                    stat.setObject(3, country.Region);
                    stat.setObject(4, country.HappinessRank);
                    stat.setObject(5, country.HappinessScore);
                    stat.setObject(6, country.WhiskerHigh);
                    stat.setObject(7, country.WhiskerLow);
                    stat.setObject(8, country.EconomyGDPPerCapita);
                    stat.setObject(9, country.Family);
                    stat.setObject(10, country.HealthLifeExpectancy);
                    stat.setObject(11, country.Freedom);
                    stat.setObject(12, country.Generosity);
                    stat.setObject(13, country.TrustGovernmentCorruption);
                    stat.setObject(14, country.DystopiaResidual);

                    stat.execute();
                } catch (Exception e) {
                    System.out.println(e.getMessage());}
            });
            var result1 =statement.executeQuery("Select Avg(HealthLifeExpectancy) from Project where Region like 'Western Europe' or 'Sub-Saharan Africa'");
            while(result1.next()){

                System.out.println(result1.getString(1));
            }
            var result2 = statement.executeQuery("Select median(d),Country from  (select Country," +
                    "(HappinessScore + WhiskerHigh + WhiskerLow + EconomyGDPPerCapita + Family + HealthLifeExpectancy " +
                    "+ Freedom + Generosity + TrustGovernmentCorruption + DystopiaResidual)/10 as d from Project)");
            while(result2.next()){

                System.out.println(result2.getDouble(1));
                System.out.println(result2.getString(2));
            }

            var chartMaker=new ChartMaker(countries);
            chartMaker.frame.setVisible(true);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Country> ParseCountryCsv(String filePath) throws IOException {

        List<Country> countries = new ArrayList<Country>();
        List<String> fileLines = Files.readAllLines(Paths.get(filePath));
        for (int j = 1; j < fileLines.size(); j++) {
            String[] splitedText = fileLines.get(j).split(",");
            ArrayList<String> columnList = new ArrayList<String>();
            for (int i = 0; i < splitedText.length; i++) {
                //Если колонка начинается на кавычки или заканчиваеться на кавычки
                if (IsColumnPart(splitedText[i])) {
                    String lastText = columnList.get(columnList.size() - 1);
                    columnList.set(columnList.size() - 1, lastText + "," + splitedText[i]);
                } else {
                    columnList.add(splitedText[i]);
                }
            }
            Country country = new Country();
            country.Country = columnList.get(0);
            country.Region = columnList.get(1);
            country.HappinessRank = Integer.parseInt(columnList.get(2));
            country.HappinessScore = Double.parseDouble(columnList.get(3));
            country.WhiskerHigh = Double.parseDouble(columnList.get(4));
            country.WhiskerLow = Double.parseDouble(columnList.get(5));
            country.EconomyGDPPerCapita = Double.parseDouble(columnList.get(6));
            country.Family = Double.parseDouble(columnList.get(7));
            country.HealthLifeExpectancy = Double.parseDouble(columnList.get(8));
            country.Freedom = Double.parseDouble(columnList.get(9));
            country.Generosity = Double.parseDouble(columnList.get(10));
            country.TrustGovernmentCorruption = Double.parseDouble(columnList.get(11));
            country.DystopiaResidual = Double.parseDouble(columnList.get(12));
            countries.add(country);
        }
        return countries;
    }


    private static boolean IsColumnPart(String text) {
        String trimText = text.trim();
        return trimText.indexOf("\"") == trimText.lastIndexOf("\"") && trimText.endsWith("\"");
    }
}

class Country {
    public String Country;
    public String Region;
    public int HappinessRank;
    public double HappinessScore;
    public double WhiskerHigh;
    public double WhiskerLow;
    public double EconomyGDPPerCapita;
    public double Family;
    public double HealthLifeExpectancy;
    public double Freedom;
    public double Generosity;
    public double TrustGovernmentCorruption;
    public double DystopiaResidual;

    @Override
    public String toString() {
        return
                HappinessRank + "," +
                        HappinessScore + "," +
                        WhiskerHigh + "," +
                        WhiskerLow + "," +
                        EconomyGDPPerCapita + "," +
                        Family + "," +
                        HealthLifeExpectancy + "," +
                        Freedom + "," +
                        Generosity + "," +
                        TrustGovernmentCorruption + "," +
                        DystopiaResidual;
    }
}


