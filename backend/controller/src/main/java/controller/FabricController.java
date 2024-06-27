package controller;
import java.nio.charset.StandardCharsets;

import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.GatewayException;
import org.hyperledger.fabric.client.SubmitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import backend.models.Doctor;
import backend.models.HealthRecord;
import backend.models.Hospital;
import backend.models.Medicine;
import backend.models.Patient;
import backend.models.Prescription;
import backend.models.Producer;

@RestController
@RequestMapping("/api")
public class FabricController {

    @Autowired
    private Contract contract;


    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // method to convert transaction payload to JSON object
    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        var parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }

    // TODO: check parameter type of submitTransaction()

    /**
     *
     * <li>HTTP endpoints of Patient class</li>
     */
    @PostMapping("/createPatient/{patientID}")
    public ResponseEntity<String> createPatient(@PathVariable String patientID, @RequestBody Patient patient) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String accessToDoctorsJson = objectMapper.writeValueAsString(patient.getAccessToDoctors());
            
            byte[] result = contract.submitTransaction("PatientContract:CreatePatient", patientID, patient.getHospitalID(), patient.getName(), String.valueOf(patient.getAge()), patient.getGender(), patient.getEmail(), patient.getPhone(), accessToDoctorsJson);
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readPatient/{patientID}")
    public ResponseEntity<String> readPatientById(@PathVariable String patientID) {
        try {
            System.out.println("\n--> Evaluate Transaction: ReadPatient, function returns patient attributes");

            byte[] evaluateResult = contract.evaluateTransaction("PatientContract:ReadPatient", patientID);

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // TODO: check how to handle accessToDoctors list
    @PutMapping("/updatePatient/{patientID}")
    public ResponseEntity<String> updatePatient(@PathVariable String patientID, @RequestBody Patient patient) {
        try {
            byte[] result = contract.submitTransaction("PatientContract:UpdatePatient", patientID, patient.getHospitalID(), patient.getName(), String.valueOf(patient.getAge()), patient.getGender(), patient.getEmail(), patient.getPhone(), patient.getAccessToDoctors().toString());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePatient/{patientID}")
    public ResponseEntity<String> deletePatient(@PathVariable String patientID) {
        try {
            contract.submitTransaction("PatientContract:DeletePatient", patientID);
            return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPatients")
    public ResponseEntity<String> getAllPatients() {
        try {
            System.out.println("\n--> Evaluate Transaction: GetAllPatients, function returns all patient attributes");

            byte[] evaluateResult = contract.evaluateTransaction("PatientContract:GetAllPatients");

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * <li>HTTP endpoints of Prescription class</li>
     */
    @PostMapping("/createPrescription/{prescriptionID}")
    public ResponseEntity<String> createPrescription(@PathVariable String prescriptionID, @RequestBody Prescription prescription) {
        try {
            byte[] result = contract.submitTransaction("PrescriptionContract:CreatePrescription", prescriptionID, prescription.getMedicineID(), String.valueOf(prescription.getDosage()), String.valueOf(prescription.getDuration()));
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readPrescription/{prescriptionID}")
    public ResponseEntity<String> readPrescriptionById(@PathVariable String prescriptionID) {
        try {
            System.out.println("\n--> Evaluate Transaction: ReadPrescription, function returns prescription attributes");

            byte[] evaluateResult = contract.evaluateTransaction("PrescriptionContract:ReadPrescription", prescriptionID);

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updatePrescription/{prescriptionID}")
    public ResponseEntity<String> updatePrescription(@PathVariable String prescriptionID, @RequestBody Prescription prescription) {
        try {
            byte[] result = contract.submitTransaction("PrescriptionContract:UpdatePrescription", prescriptionID, prescription.getMedicineID(), String.valueOf(prescription.getDosage()), String.valueOf(prescription.getDuration()));
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePrescription/{prescriptionID}")
    public ResponseEntity<String> deletePrescription(@PathVariable String prescriptionID) {
        try {
            contract.submitTransaction("PrescriptionContract:DeletePrescription", prescriptionID);
            return new ResponseEntity<>("Prescription deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPrescriptions")
    public ResponseEntity<String> getAllPrescriptions() {
        try {
            System.out.println("\n--> Evaluate Transaction: GetAllPrescriptions, function returns all prescription attributes");

            byte[] evaluateResult = contract.evaluateTransaction("PrescriptionContract:GetAllPrescriptions");

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * <li>HTTP endpoints of Medicine class</li>
     */
    @PostMapping("/createMedicine/{medicineID}")
    public ResponseEntity<String> createMedicine(@PathVariable String medicineID, @RequestBody Medicine medicine) {
        try {
            byte[] result = contract.submitTransaction("MedicineContract:CreateMedicine", medicineID, medicine.getProducerID(), medicine.getName(), medicine.getProductionDate(), medicine.getExpirationDate(), medicine.getCurrentOwner(), medicine.getPreviousOwners());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readMedicine/{medicineID}")
    public ResponseEntity<String> readMedicineById(@PathVariable String medicineID) {
        try {
            System.out.println("\n--> Evaluate Transaction: ReadMedicine, function returns medicine attributes");

            byte[] evaluateResult = contract.evaluateTransaction("MedicineContract:ReadMedicine", medicineID);

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateMedicine/{medicineID}")
    public ResponseEntity<String> updateMedicine(@PathVariable String medicineID, @RequestBody Medicine medicine) {
        try {
            byte[] result = contract.submitTransaction("MedicineContract:UpdateMedicine", medicineID, medicine.getProducerID(), medicine.getName(), medicine.getProductionDate(), medicine.getExpirationDate(), medicine.getCurrentOwner(), medicine.getPreviousOwners());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteMedicine/{medicineID}")
    public ResponseEntity<String> deleteMedicine(@PathVariable String medicineID) {
        try {
            contract.submitTransaction("MedicineContract:DeleteMedicine", medicineID);
            return new ResponseEntity<>("Medicine deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllMedicines")
    public ResponseEntity<String> getAllMedicines() {
        try {
            System.out.println("\n--> Evaluate Transaction: GetAllMedicines, function returns all medicine attributes");

            byte[] evaluateResult = contract.evaluateTransaction("MedicineContract:GetAllMedicines");

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * <li>HTTP endpoints of HealthRecord class</li>
     */

    @PostMapping("/createHealthRecord/{recordID}")
    public ResponseEntity<String> createHealthRecord(@PathVariable String recordID, @RequestBody HealthRecord healthRecord) {
        try {
            byte[] result = contract.submitTransaction("CreateHealthRecord", recordID, healthRecord.getDate(), healthRecord.getPatientID(), healthRecord.getDoctorID(), healthRecord.getSymptom(), healthRecord.getDiagnosis(), healthRecord.getTreatment(), healthRecord.getPrescriptionID());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readHealthRecord/{recordID}")
    public ResponseEntity<String> readHealthRecordById(@PathVariable String recordID) {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("ReadHealthRecord", recordID);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateHealthRecord/{recordID}")
    public ResponseEntity<String> updateHealthRecord(@PathVariable String recordID, @RequestBody HealthRecord healthRecord) {
        try {
            byte[] result = contract.submitTransaction("UpdateHealthRecord", recordID, healthRecord.getDate(), healthRecord.getPatientID(), healthRecord.getDoctorID(), healthRecord.getSymptom(), healthRecord.getDiagnosis(), healthRecord.getTreatment(), healthRecord.getPrescriptionID());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteHealthRecord/{recordID}")
    public ResponseEntity<String> deleteHealthRecord(@PathVariable String recordID) {
        try {
            contract.submitTransaction("DeleteHealthRecord", recordID);
            return new ResponseEntity<>("Health record deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllHealthRecords")
    public ResponseEntity<String> getAllHealthRecords() {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("GetAllRecords");
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * <li>HTTP endpoints of Doctor class</li>
     */

    @PostMapping("/createDoctor/{doctorID}")
    public ResponseEntity<String> createDoctor(@PathVariable String doctorID, @RequestBody Doctor doctor) {
        try {
            byte[] result = contract.submitTransaction("DoctorContract:CreateDoctor", doctorID, doctor.getName(), String.valueOf(doctor.getAge()), doctor.getGender(), doctor.getAddress(), doctor.getEmail(), doctor.getPhone(), doctor.getHospitalID(), doctor.getSpeciality());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readDoctor/{doctorID}")
    public ResponseEntity<String> readDoctorById(@PathVariable String doctorID) {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("DoctorContract:ReadDoctor", doctorID);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateDoctor/{doctorID}")
    public ResponseEntity<String> updateDoctor(@PathVariable String doctorID, @RequestBody Doctor doctor) {
        try {
            byte[] result = contract.submitTransaction("DoctorContract:UpdateDoctor", doctorID, doctor.getName(), String.valueOf(doctor.getAge()), doctor.getGender(), doctor.getAddress(), doctor.getEmail(), doctor.getPhone(), doctor.getHospitalID(), doctor.getSpeciality());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteDoctor/{doctorID}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String doctorID) {
        try {
            contract.submitTransaction("DoctorContract:DeleteDoctor", doctorID);
            return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllDoctors")
    public ResponseEntity<String> getAllDoctors() {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("DoctorContract:GetAllDoctors");
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * <li>HTTP endpoints of Producer class</li>
     */

    @PostMapping("/createProducer/{producerID}")
    public ResponseEntity<String> createProducer(@PathVariable String producerID, @RequestBody Producer producer) {
        try {
            byte[] result = contract.submitTransaction("ProducerContract:CreateProducer", producerID, producer.getName(), producer.getAddress());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readProducer/{producerID}")
    public ResponseEntity<String> readProducerById(@PathVariable String producerID) {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("ProducerContract:ReadProducer", producerID);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProducer/{producerID}")
    public ResponseEntity<String> updateProducer(@PathVariable String producerID, @RequestBody Producer producer) {
        try {
            byte[] result = contract.submitTransaction("ProducerContract:UpdateProducer", producerID, producer.getName(), producer.getAddress());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteProducer/{producerID}")
    public ResponseEntity<String> deleteProducer(@PathVariable String producerID) {
        try {
            contract.submitTransaction("ProducerContract:DeleteProducer", producerID);
            return new ResponseEntity<>("Producer deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllProducers")
    public ResponseEntity<String> getAllProducers() {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("ProducerContract:GetAllProducers");
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * <li>HTTP endpoints of Hospital class</li>
     */
    @PostMapping("/createHospital/{hospitalID}")
    public ResponseEntity<String> createHospital(@PathVariable String hospitalID, @RequestBody Hospital hospital) {
        try {
            byte[] result = contract.submitTransaction("HospitalContract:CreateHospital", hospitalID, hospital.getName(), hospital.getAddress(), hospital.getPhone(), hospital.getEmail());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readHospital/{hospitalID}")
    public ResponseEntity<String> readHospitalById(@PathVariable String hospitalID) {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("HospitalContract:ReadHospital", hospitalID);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateHospital/{hospitalID}")
    public ResponseEntity<String> updateHospital(@PathVariable String hospitalID, @RequestBody Hospital hospital) {
        try {
            byte[] result = contract.submitTransaction("HospitalContract:UpdateHospital", hospitalID, hospital.getName(), hospital.getAddress(), hospital.getPhone(), hospital.getEmail());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteHospital/{hospitalID}")
    public ResponseEntity<String> deleteHospital(@PathVariable String hospitalID) {
        try {
            contract.submitTransaction("HospitalContract:DeleteHospital", hospitalID);
            return new ResponseEntity<>("Hospital deleted successfully", HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllHospitals")
    public ResponseEntity<String> getAllHospitals() {
        try {
            byte[] evaluateResult = contract.evaluateTransaction("HospitalContract:GetAllHospitals");
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
