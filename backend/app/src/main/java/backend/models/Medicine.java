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
    private final String producerID;

    @Property()
    private final String name;

    @Property()
    private final String productionDate;

    @Property()
    private final String expirationDate;

    @Property()
    private final Object currentOwner;

    @Property()
    private final String previousOwners;

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

    public String getPreviousOwners() {
        return previousOwners;
    }

    public Medicine(@JsonProperty("medicineID") String medicineID, @JsonProperty("producerID") String producerID, @JsonProperty("name") String name, @JsonProperty("productionDate") String productionDate, @JsonProperty("expirationDate") String expirationDate, @JsonProperty("currentOwner") Object currentOwner, @JsonProperty("ownerHistory") String previousOwners) {
        this.medicineID = medicineID;
        this.producerID = producerID;
        this.name = name;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
        this.currentOwner = currentOwner;
        this.previousOwners = previousOwners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(getMedicineID(), medicine.getMedicineID()) && Objects.equals(getName(), medicine.getName()) && Objects.equals(getProductionDate(), medicine.getProductionDate()) && Objects.equals(getCurrentOwner(), medicine.getCurrentOwner()) && Objects.equals(getPreviousOwners(), medicine.getPreviousOwners());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicineID(), getName(), getProductionDate(), getCurrentOwner(), getPreviousOwners());
    }
}
