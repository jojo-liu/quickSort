import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickSort {
    public static final String DESTINATION[] = {"NY", "Miami", "Huston", "LA"};
    public static final String COLOR[] = {"Blue", "Green", "Red"};
    public static final String DIRECTORY = "./carFile/";
    public static final File RANDOMFILE = new File(DIRECTORY, "randomFile.txt");
    public static final File SORTFILE = new File(DIRECTORY, "sortedFile.txt");
    public static final String CHARSETNAME = "UTF-8";
    public static final int N = 20_0000;
    public static final int S = 3_0000;
    public static final Car[] CAR = new Car[N];

    //use AtomicInteger interface which is thread safe to implement increment and decrement operation
    //THREADCOUNT records the number of executing threads
    public static final int MAXIMUMTHREAD = 100;
    public static final AtomicInteger THREADCOUNT = new AtomicInteger(MAXIMUMTHREAD);
    public static final AtomicInteger VALIDCOUNT = new AtomicInteger(Integer.MIN_VALUE);


    public static void main(String[] args) throws IOException,
            InterruptedException {
        writeRandomFile();
        readRandomFile();
        sort();

        System.out.println();
        System.out.println("The number of defined maximum threads in this system is: " + MAXIMUMTHREAD);
        System.out.println("The number of efficiently used maximum threads is: " + String.valueOf(VALIDCOUNT));

//        forkJoinSort();
        checkSort();
        writeSortFile();
    }

    public static void arraysSort() {
        long beginTime = System.currentTimeMillis();
        java.util.Arrays.sort(CAR, new SortByCar());
        long endTime = System.currentTimeMillis();
        System.out.println("arrays sort file:" + (endTime - beginTime) + "ms");
    }

    public static void forkJoinSort() {
        long beginTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(new SortTask(CAR, 0, CAR.length - 1));
        forkJoinPool.shutdown();
        try {
            forkJoinPool.awaitTermination(10_000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("sort file:" + (endTime - beginTime) + "ms");
    }

    public static void writeRandomFile() {
        String dest = "";
        String carColor = "";
        String serialNum = "";

        File dir = new File(DIRECTORY);
        if(!dir.isDirectory()) {
            System.out.println("The directory of defined file does not exist, the system will " +
                    "create the directory");
            dir.mkdirs();
        } else {
            System.out.println("The directory of defined file exist");
        }

        if(!RANDOMFILE.exists()) {
            System.out.println("The system will create the defined file and write new car information into it");
            try {
                RANDOMFILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The system will overwrite the new car information into defined file");
        }

        System.out.println();
        System.out.println("Start to randomly generate car information and write them into file: ");
        long beginTime = System.currentTimeMillis();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(RANDOMFILE), CHARSETNAME))) {
            for (int i = 0; i < N; i++) {
                Random rd = new Random();
                dest = DESTINATION[rd.nextInt(4)];
                carColor = COLOR[rd.nextInt(3)];
                serialNum = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();

                //write format car information into file
                bw.write(String.format("%-10s %-10s %-10s", dest, carColor, serialNum));
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Writing car information into file costs: " + (endTime - beginTime) + "ms");
    }

    public static void readRandomFile() {
        System.out.println();
        System.out.println("Start to read car information from file: ");
        long beginTime = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(RANDOMFILE), CHARSETNAME));) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] carInfo = line.split("     ");

                if(carInfo.length != 3) {
                    CAR[index++] = new Car("", "", "");
                } else {
                    CAR[index++] = new Car(carInfo[0], carInfo[1].trim(), carInfo[2].trim());
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Reading file costs: " + (endTime - beginTime) + "ms");
    }

    public static void sort() {
        System.out.println();
        System.out.println("Start to quick sort car information: ");
        long beginTime = System.currentTimeMillis();
        try {
            sort(CAR, 0, CAR.length - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Multithreading quick sort costs: " + (endTime - beginTime) + "ms");
    }

    public static void checkSort() {
        long beginTime = System.currentTimeMillis();
        boolean flag = true;
        try {
            int Nmm = N - 2;
            for (int i = 0; i < Nmm; i++) {
                if(CAR[i].toString().compareTo(CAR[i + 1].toString()) > 0) {
                    System.out.println();
                    System.out.println("The sorted car information is not correct, here is the outlier： ");
                    System.out.println(CAR[i].toString() + " > " + CAR[i + 1].toString());
                    flag = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        if(flag) {
            System.out.println();
            System.out.println("The sorted car information is correct");
        }
        System.out.println("Checking sorted car information costs: "
                + (endTime - beginTime) + "ms");
    }

    public static void writeSortFile() {
        File dir = new File(DIRECTORY);
        System.out.println();
        if(!dir.isDirectory()) {
            System.out.println("The directory of defined file does not exist, the system will " +
                    "create the directory");
            dir.mkdirs();
        } else {
            System.out.println("The directory of defined file exist");
        }

        if(!SORTFILE.exists()) {
            System.out.println("The system will create the defined file and write new car information into it");
            try {
                SORTFILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The system will overwrite the new car information into defined file");
        }

        System.out.println();
        System.out.println("Start to write sorted car information into file: ");
        long beginTime = System.currentTimeMillis();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(SORTFILE), CHARSETNAME))) {
            for (int i = 0; i < N; i++) {
                bw.write(String.format("%-10s %-10s %-10s", DESTINATION[Integer.valueOf(CAR[i].getDestination())], COLOR[Integer.valueOf(CAR[i].getColor())], CAR[i].getSerialNumber()));
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Writing sorted car information into file costs: " + (endTime - beginTime) + "ms");
    }

    public static void sort(final Car car[], final int low, final int high) throws CloneNotSupportedException {
        if (high - low < S) {
            Arrays.sort(car, low, high + 1, new SortByCar());
            return;
        }
        int i = low;
        int j = high;

        //since quick sort performs bad in sorted sequence, we need reconstruct the sequence
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
        pivot = (Car) car[low].clone();
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
        List<Thread> threads = new ArrayList<Thread>();
        final int imm = i - 1;
        if (low < imm) {
            if (QuickSort.THREADCOUNT.get() > 0 && imm - low > S) {
                Thread t = new Thread() {
                    {
                        QuickSort.THREADCOUNT.decrementAndGet();
//                        System.out.println("thread left: " + QuickSort.THREADCOUNT.decrementAndGet());
                        if(QuickSort.VALIDCOUNT.get() < MAXIMUMTHREAD - QuickSort.THREADCOUNT.get()) {
                            QuickSort.VALIDCOUNT.set(MAXIMUMTHREAD - QuickSort.THREADCOUNT.get());
                        }
                    }

                    @Override
                    public void run() {
                        try {
                            sort(car, low, imm);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }

                        QuickSort.THREADCOUNT.addAndGet(1);
                    }
                };
                t.start();
                threads.add(t);
            } else {
                Arrays.sort(car, low, imm + 1, new SortByCar());
            }
        }
        final int ipp = i + 1;
        if (high > ipp) {
            if (QuickSort.THREADCOUNT.get() > 0 && high - ipp > S) {
                Thread t = new Thread() {
                    {
                        QuickSort.THREADCOUNT.decrementAndGet();
//                        System.out.println("thread right: " + QuickSort.THREADCOUNT.decrementAndGet());
                        if(QuickSort.VALIDCOUNT.get() < MAXIMUMTHREAD - QuickSort.THREADCOUNT.get()) {
                            QuickSort.VALIDCOUNT.set(MAXIMUMTHREAD - QuickSort.THREADCOUNT.get());
                        }
                    }

                    @Override
                    public void run() {
                        try {
                            sort(car, ipp, high);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        QuickSort.THREADCOUNT.addAndGet(1);
                    }
                };
                t.start();
                threads.add(t);
            } else {
                Arrays.sort(car, ipp, high + 1, new SortByCar());
            }
        }

        try {
            for (Thread t : threads)
                t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

