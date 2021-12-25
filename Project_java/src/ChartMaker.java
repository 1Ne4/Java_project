import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;

public class ChartMaker {
    public ChartFrame frame;

    public ChartMaker(List<Country> countries) {
        initialize(countries);
    }

    private CategoryDataset createDataset(List<Country> countries) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        countries.forEach(country -> {
            dataset.addValue(country.HealthLifeExpectancy, country.Country, country.Country);
        });
        return dataset;
    }

    private void initialize(List<Country> countries) {
        frame = new ChartFrame("name", ChartFactory.createBarChart("Графики по показателю здоровья", "Страны", "Показатель Здоровья", this.createDataset(countries)));
        frame.setTitle("Графики");
        frame.setBounds(10, 10, 1900, 990);
    }
}
