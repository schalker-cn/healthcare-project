package backend.models;

import java.util.List;
import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonCreator;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Patient {
    @Property()
    private final String patientID;

    @Property()
    private final String hospitalID;

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
    private final List<String> accessToDoctors;

    public String getPatientID() {
        return patientID;
    }

    public String getHospitalID() {
        return hospitalID;
    }

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

    public Patient() {
        // mock constructor, will not be used
        this.patientID = "mock";
        this.hospitalID = "mock";
        this.name = "mock";
        this.age = 0;
        this.gender = "mock";
        this.email = "mock";
        this.phone = "mock";
        this.accessToDoctors = List.of();
    }

    @JsonCreator
    public Patient(@JsonProperty("patientID") String patientID, @JsonProperty("hospitalID") String hospitalID, @JsonProperty("name") String name, int age, @JsonProperty("gender") String gender, @JsonProperty("email") String email, @JsonProperty("phone") String phone, @JsonProperty("accessToDoctors") List<String> accessToDoctors) {
        this.patientID = patientID;
        this.hospitalID = hospitalID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.accessToDoctors = accessToDoctors;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPatientID(), getHospitalID(), getName(), getAge(), getGender(), getEmail(), getPhone(), getAccessToDoctors());
    }
}
