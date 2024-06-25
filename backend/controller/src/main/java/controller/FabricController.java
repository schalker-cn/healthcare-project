package controller;
import backend.models.Patient;
import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class FabricController {
    @Autowired
    @Qualifier("patientContract")
    private Contract patientContract;

    @Autowired
    @Qualifier("prescriptionContract")
    private Contract prescriptionContract;

    @Autowired
    @Qualifier("medicineContract")
    private Contract medicineContract;

    @Autowired
    @Qualifier("healthRecordContract")
    private Contract healthRecordContract;

    @Autowired
    @Qualifier("doctorContract")
    private Contract doctorContract;

    @PostMapping("/createPatient")
    public String createPatient(@RequestBody Patient patient) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        try {
            byte[] result = patientContract.submitTransaction("CreatePatient", patient.getPatientID(), patient.getHospitalID(), patient.getName(), String.valueOf(patient.getAge()), patient.getGender(), patient.getEmail(), patient.getPhone(), patient.getAccessToDoctors());
            return new String(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
