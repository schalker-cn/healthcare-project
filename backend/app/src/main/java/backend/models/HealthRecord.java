package backend.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public final class HealthRecord {

    @Property()
    private final String recordID;

    @Property()
    private final String date;

    @Property()
    private final String patientID;

    @Property()
    private final String doctorID;

    @Property()
    private final String symptom;

    @Property()
    private final String diagnosis;

    @Property()
    private final String treatment;

    @Property()
    private final Prescription prescription;

    public String getRecordID() {
        return recordID;
    }

    public String getDate() {
        return date;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getSymptom() {
        return symptom;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public HealthRecord(@JsonProperty("recordID") String recordID, @JsonProperty("date") String date, @JsonProperty("patientID") String patientID, @JsonProperty("doctorID") String doctorID, @JsonProperty("symptom") String symptom, @JsonProperty("diagnosis") String diagnosis, @JsonProperty("treatment") String treatment, @JsonProperty("prescription") Prescription prescription) {
        this.recordID = recordID;
        this.date = date;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.symptom = symptom;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthRecord that = (HealthRecord) o;
        return Objects.equals(getRecordID(), that.getRecordID()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getPatientID(), that.getPatientID()) && Objects.equals(getDoctorID(), that.getDoctorID()) && Objects.equals(getSymptom(), that.getSymptom()) && Objects.equals(getDiagnosis(), that.getDiagnosis()) && Objects.equals(getTreatment(), that.getTreatment()) && Objects.equals(getPrescription(), that.getPrescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecordID(), getDate(), getPatientID(), getDoctorID(), getSymptom(), getDiagnosis(), getTreatment(), getPrescription());
    }
}
