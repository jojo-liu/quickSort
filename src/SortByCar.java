import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class SortByCar implements Comparator<Car> {

    @Override
    public int compare(Car o1, Car o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
