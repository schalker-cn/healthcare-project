package backend.models;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonCreator;
import com.owlike.genson.annotation.JsonProperty;

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
    private final String prescriptionID;

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

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public HealthRecord() {
        this.recordID = "mock";
        this.date = "mock";
        this.patientID = "mock";
        this.doctorID = "mock";
        this.symptom = "mock";
        this.diagnosis = "mock";
        this.treatment = "mock";
        this.prescriptionID = "mock";
    }

    @JsonCreator
    public HealthRecord(@JsonProperty("recordID") String recordID, @JsonProperty("date") String date, @JsonProperty("patientID") String patientID, @JsonProperty("doctorID") String doctorID, @JsonProperty("symptom") String symptom, @JsonProperty("diagnosis") String diagnosis, @JsonProperty("treatment") String treatment, @JsonProperty("prescriptionID") String prescriptionID) {
        this.recordID = recordID;
        this.date = date;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.symptom = symptom;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescriptionID = prescriptionID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthRecord that = (HealthRecord) o;
        return Objects.equals(getRecordID(), that.getRecordID()) && Objects.equals(getDate(), that.getDate()) && Objects.equals(getPatientID(), that.getPatientID()) && Objects.equals(getDoctorID(), that.getDoctorID()) && Objects.equals(getSymptom(), that.getSymptom()) && Objects.equals(getDiagnosis(), that.getDiagnosis()) && Objects.equals(getTreatment(), that.getTreatment()) && Objects.equals(getPrescriptionID(), that.getPrescriptionID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecordID(), getDate(), getPatientID(), getDoctorID(), getSymptom(), getDiagnosis(), getTreatment(), getPrescriptionID());
    }
}
