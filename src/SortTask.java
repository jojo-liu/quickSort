import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
class SortTask extends RecursiveAction {
    final Car[] car;
    final int start;
    final int end;
    private int THRESHOLD = 300;

    public SortTask(Car[] car) {
        this.car = car;
        this.start = 0;
        this.end = car.length - 1;
    }

    public SortTask(Car[] car, int start, int end) {
        this.car = car;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start < THRESHOLD) {
            Arrays.sort(car, start, end + 1, new SortByCar());
        } else {
            int pivot = 0;
            try {
                pivot = partition(car, start, end);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            SortTask left = null;
            SortTask right = null;
            if (start < pivot - 1) {
                left = new SortTask(car, start, pivot - 1);
            }
            if (pivot + 1 < end) {
                right = new SortTask(car, pivot + 1, end);
            }
            if (left != null) {
                left.fork();
            }
            if (right != null) {
                right.fork();
            }
        }
    }

    private int partition(Car[] car, int start, int end) throws CloneNotSupportedException {
        int i = start;
        int j = end;

        if (j - i > 2) {
            if((car[i].toString().compareTo(car[j - i].toString()) < 0 &&
                    car[j - i].toString().compareTo(car[j].toString()) < 0)
                    || (car[j].toString().compareTo(car[j - i].toString()) < 0 &&
                    car[j - i].toString().compareTo(car[i].toString()) < 0)) {
                Car temp = car[i];
                car[i] = (Car) car[j - i].clone();
                car[j - i] = (Car) temp.clone();
            } else {
                if((car[i].toString().compareTo(car[j].toString()) < 0 &&
                        car[j].toString().compareTo(car[j - i].toString()) < 0)
                        || (car[j - i].toString().compareTo(car[j].toString()) < 0 &&
                        car[j].toString().compareTo(car[i].toString()) < 0)) {
                    Car temp = car[i];
                    car[i] = (Car) car[j].clone();
                    car[j] = (Car) temp.clone();
                }
            }
        }
        Car pivot = new Car("", "", "");
        pivot = (Car) car[i].clone();
        while (i < j) {
            while (i < j && car[j].toString().compareTo(pivot.toString()) > 0) {
                j--;
            }
            if (i < j) {
                car[i++] = (Car) car[j].clone();
            }
            while (i < j && car[i].toString().compareTo(pivot.toString()) < 0) {
                i++;
            }
            if (i < j) {
                car[j--] = (Car) car[i].clone();
            }
        }
        car[i] = (Car) pivot.clone();
        return i;
    }
}