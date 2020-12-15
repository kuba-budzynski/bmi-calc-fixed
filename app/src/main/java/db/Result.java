package db;

public class Result {

    private String weight;
    private String height;
    private String bmi;
    private String date;

    public Result(String weight, String height, String bmi, String date) {
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Result{" +
                "weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", bmi='" + bmi + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
