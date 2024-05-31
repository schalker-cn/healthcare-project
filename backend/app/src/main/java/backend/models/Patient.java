package backend.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;
import java.util.Objects;

@DataType()
public final class Patient {
    @Property()
    private final String patientID;

    @Property()
    private final String name;

    @Property()
    private final int age;

    @Property()
    private final String gender;

    @Property()
    private final String email;

    @Property()
    private final String phone;

    @Property()
    private final List accessToDoctors;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List getAccessToDoctors() {
        return accessToDoctors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return age == patient.age && Objects.equals(name, patient.name) && Objects.equals(gender, patient.gender) && Objects.equals(email, patient.email) && Objects.equals(phone, patient.phone) && Objects.equals(accessToDoctors, patient.accessToDoctors);
    }

    public Patient(@JsonProperty("patientID") String patientID, @JsonProperty("name") String name, int age, @JsonProperty("gender") String gender, @JsonProperty("email") String email, @JsonProperty("phone") String phone, @JsonProperty("accessToDoctors") List accessToDoctors) {
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.accessToDoctors = accessToDoctors;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), getGender(), getEmail(), getPhone(), getAccessToDoctors());
    }
}
