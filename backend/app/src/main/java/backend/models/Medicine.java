package backend.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Medicine {

    @Property()
    private final String medicineID;

    @Property()
    private final String name;

    @Property()
    private final String productionDate;

    @Property()
    private final Object currentOwner;

    @Property()
    private final String ownerHistory;

    public String getMedicineID() {
        return medicineID;
    }

    public String getName() {
        return name;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public Object getCurrentOwner() {
        return currentOwner;
    }

    public String getOwnerHistory() {
        return ownerHistory;
    }

    public Medicine(@JsonProperty("medicineID") String medicineID, @JsonProperty("name") String name, @JsonProperty("productionDate") String productionDate, @JsonProperty("currentOwner") Object currentOwner, @JsonProperty("ownerHistory") String ownerHistory) {
        this.medicineID = medicineID;
        this.name = name;
        this.productionDate = productionDate;
        this.currentOwner = currentOwner;
        this.ownerHistory = ownerHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(getMedicineID(), medicine.getMedicineID()) && Objects.equals(getName(), medicine.getName()) && Objects.equals(getProductionDate(), medicine.getProductionDate()) && Objects.equals(getCurrentOwner(), medicine.getCurrentOwner()) && Objects.equals(getOwnerHistory(), medicine.getOwnerHistory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicineID(), getName(), getProductionDate(), getCurrentOwner(), getOwnerHistory());
    }
}
