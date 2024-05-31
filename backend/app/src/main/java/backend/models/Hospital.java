package backend.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Hospital {
    @Property()
    private final String hospitalID;

    @Property()
    private final String name;

    @Property()
    private final String address;

    @Property()
    private final String phone;

    @Property()
    private final String email;

    public String getHospitalID() {
        return hospitalID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Hospital(@JsonProperty("hospitalID") String hospitalID, @JsonProperty("name") String name, @JsonProperty("address") String address, @JsonProperty("phone") String phone, @JsonProperty("email") String email) {
        this.hospitalID = hospitalID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hospital hospital = (Hospital) o;
        return Objects.equals(getHospitalID(), hospital.getHospitalID()) && Objects.equals(getName(), hospital.getName()) && Objects.equals(getAddress(), hospital.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHospitalID(), getName(), getAddress(), getPhone(), getEmail());
    }
}
