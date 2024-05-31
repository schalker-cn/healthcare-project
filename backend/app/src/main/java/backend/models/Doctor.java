package backend.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class Doctor {

    @Property()
    private final String doctorID;

    @Property()
    private final String name;

    @Property()
    private final int age;

    @Property()
    private final String gender;

    @Property()
    private final String address;

    @Property()
    private final String email;

    @Property()
    private final String phone;

    @Property()
    private final String hospitalID;

    @Property()
    private final String speciality;

    public String getDoctorID() {
        return doctorID;
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

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Doctor(@JsonProperty("doctorID") String doctorID, @JsonProperty("name") String name, @JsonProperty("age") int age, @JsonProperty("gender") String gender, @JsonProperty("address") String address, @JsonProperty("email") String email, @JsonProperty("phone") String phone, @JsonProperty("hospitalID") String hospitalID, @JsonProperty("speciality") String speciality) {
        this.doctorID = doctorID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.hospitalID = hospitalID;
        this.speciality = speciality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return getAge() == doctor.getAge() && Objects.equals(getDoctorID(), doctor.getDoctorID()) && Objects.equals(getName(), doctor.getName()) && Objects.equals(getGender(), doctor.getGender()) && Objects.equals(getAddress(), doctor.getAddress()) && Objects.equals(getHospitalID(), doctor.getHospitalID()) && Objects.equals(getSpeciality(), doctor.getSpeciality());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDoctorID(), getName(), getAge(), getGender(), getAddress(), getEmail(), getPhone(), getHospitalID(), getSpeciality());
    }
}
