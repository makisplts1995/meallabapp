package gr.unipi.meallab.model;

/*Κλάση που αναπαριστά ένα υλικό (Ingredient) μιας συνταγής.
Χρησιμοποιείται για την αποθήκευση του ονόματος και της ποσότητας/μονάδας μέτρησης.*/
public class Ingredient {
    private String name; // Το όνομα του υλικού
    private String measure; // Η ποσότητα και η μονάδα μέτρησης

    // Default constructor απαραίτητος για τη βιβλιοθήκη Jackson (JSON parsing)
    public Ingredient() {
    }

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public String toString() {
        // Επιστρέφει name + measure σε παρένθεση
        return name + " (" + measure + ")";
    }
}
