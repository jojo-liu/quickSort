public class Car implements Cloneable {
    private String destination;
    private String color;
    private String serialNumber;

    public Car(String destination, String color, String serialNumber) {
        this.destination = destination;
        this.color = color;
        this.serialNumber = serialNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        String city = destination;
        switch (city) {
            case "NY": destination = "0"; break;
            case "Miami": destination = "1"; break;
            case "Huston": destination = "2"; break;
            case "LA": destination = "3"; break;
        }

        String col = color;
        switch (col) {
            case "Blue": color = "0"; break;
            case "Green": color = "1"; break;
            case "Red": color = "2"; break;
        }
        return destination + color + serialNumber;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
