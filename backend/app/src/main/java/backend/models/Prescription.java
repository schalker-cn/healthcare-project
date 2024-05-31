package backend.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Prescription {

    @Property()
    private final String prescriptionID;

    @Property()
    private final String medicineID;

    @Property()
    private final int dosage;

    @Property()
    private final int duration;

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public int getDosage() {
        return dosage;
    }

    public int getDuration() {
        return duration;
    }

    public Prescription(@JsonProperty("prescriptionID") String prescriptionID, @JsonProperty("medicineID") String medicineID, @JsonProperty("dosage") int dosage, @JsonProperty("duration") int duration) {
        this.prescriptionID = prescriptionID;
        this.medicineID = medicineID;
        this.dosage = dosage;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return getDosage() == that.getDosage() && getDuration() == that.getDuration() && Objects.equals(getPrescriptionID(), that.getPrescriptionID()) && Objects.equals(getMedicineID(), that.getMedicineID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrescriptionID(), getMedicineID(), getDosage(), getDuration());
    }
}
