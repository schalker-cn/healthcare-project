package controller;
import backend.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.hyperledger.fabric.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
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

    @Autowired
    @Qualifier("producerContract")
    private Contract producerContract;

    @Autowired
    @Qualifier("hospitalContract")
    private Contract hospitalContract;

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
    @PostMapping("/createPatient/{patientId}")
    public ResponseEntity<String> createPatient(@PathVariable String patientId, @RequestBody Patient patient) {
        try {
            byte[] result = patientContract.submitTransaction("CreatePatient", patientId, patient.getHospitalID(), patient.getName(), String.valueOf(patient.getAge()), patient.getGender(), patient.getEmail(), patient.getPhone(), patient.getAccessToDoctors().toString());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readPatient/{patientId}")
    public ResponseEntity<String> readPatientById(@PathVariable String patientId) {
        try {
            System.out.println("\n--> Evaluate Transaction: ReadPatient, function returns patient attributes");

            byte[] evaluateResult = patientContract.evaluateTransaction("ReadPatient", patientId);

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
    @PutMapping("/updatePatient/{patientId}")
    public ResponseEntity<String> updatePatient(@PathVariable String patientId, @RequestBody Patient patient) {
        try {
            byte[] result = patientContract.submitTransaction("UpdatePatient", patientId, patient.getHospitalID(), patient.getName(), String.valueOf(patient.getAge()), patient.getGender(), patient.getEmail(), patient.getPhone(), patient.getAccessToDoctors().toString());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePatient/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable String patientId) {
        try {
            patientContract.submitTransaction("DeletePatient", patientId);
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

            byte[] evaluateResult = patientContract.evaluateTransaction("GetAllPatients");

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
    @PostMapping("/createPrescription/{prescriptionId}")
    public ResponseEntity<String> createPrescription(@PathVariable String prescriptionId, @RequestBody Prescription prescription) {
        try {
            byte[] result = prescriptionContract.submitTransaction("CreatePrescription", prescriptionId, prescription.getMedicineID(), String.valueOf(prescription.getDosage()), String.valueOf(prescription.getDuration()));
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readPrescription/{prescriptionId}")
    public ResponseEntity<String> readPrescriptionById(@PathVariable String prescriptionId) {
        try {
            System.out.println("\n--> Evaluate Transaction: ReadPrescription, function returns prescription attributes");

            byte[] evaluateResult = prescriptionContract.evaluateTransaction("ReadPrescription", prescriptionId);

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updatePrescription/{prescriptionId}")
    public ResponseEntity<String> updatePrescription(@PathVariable String prescriptionId, @RequestBody Prescription prescription) {
        try {
            byte[] result = prescriptionContract.submitTransaction("UpdatePrescription", prescriptionId, prescription.getMedicineID(), String.valueOf(prescription.getDosage()), String.valueOf(prescription.getDuration()));
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deletePrescription/{prescriptionId}")
    public ResponseEntity<String> deletePrescription(@PathVariable String prescriptionId) {
        try {
            prescriptionContract.submitTransaction("DeletePrescription", prescriptionId);
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

            byte[] evaluateResult = prescriptionContract.evaluateTransaction("GetAllPrescriptions");

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
    @PostMapping("/createMedicine/{medicineId}")
    public ResponseEntity<String> createMedicine(@PathVariable String medicineId, @RequestBody Medicine medicine) {
        try {
            byte[] result = medicineContract.submitTransaction("CreateMedicine", medicineId, medicine.getProducerID(), medicine.getName(), medicine.getProductionDate(), medicine.getExpirationDate(), medicine.getCurrentOwner(), medicine.getPreviousOwners());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readMedicine/{medicineId}")
    public ResponseEntity<String> readMedicineById(@PathVariable String medicineId) {
        try {
            System.out.println("\n--> Evaluate Transaction: ReadMedicine, function returns medicine attributes");

            byte[] evaluateResult = medicineContract.evaluateTransaction("ReadMedicine", medicineId);

            String resultJSON = prettyJson(evaluateResult);
            System.out.println("*** Result:" + resultJSON);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateMedicine/{medicineId}")
    public ResponseEntity<String> updateMedicine(@PathVariable String medicineId, @RequestBody Medicine medicine) {
        try {
            byte[] result = medicineContract.submitTransaction("UpdateMedicine", medicineId, medicine.getProducerID(), medicine.getName(), medicine.getProductionDate(), medicine.getExpirationDate(), medicine.getCurrentOwner(), medicine.getPreviousOwners());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteMedicine/{medicineId}")
    public ResponseEntity<String> deleteMedicine(@PathVariable String medicineId) {
        try {
            medicineContract.submitTransaction("DeleteMedicine", medicineId);
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

            byte[] evaluateResult = medicineContract.evaluateTransaction("GetAllMedicines");

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

    @PostMapping("/createHealthRecord/{recordId}")
    public ResponseEntity<String> createHealthRecord(@PathVariable String recordId, @RequestBody HealthRecord healthRecord) {
        try {
            byte[] result = healthRecordContract.submitTransaction("CreateHealthRecord", recordId, healthRecord.getDate(), healthRecord.getPatientID(), healthRecord.getDoctorID(), healthRecord.getSymptom(), healthRecord.getDiagnosis(), healthRecord.getTreatment(), healthRecord.getPrescriptionID());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readHealthRecord/{recordId}")
    public ResponseEntity<String> readHealthRecordById(@PathVariable String recordId) {
        try {
            byte[] evaluateResult = healthRecordContract.evaluateTransaction("ReadHealthRecord", recordId);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateHealthRecord/{recordId}")
    public ResponseEntity<String> updateHealthRecord(@PathVariable String recordId, @RequestBody HealthRecord healthRecord) {
        try {
            byte[] result = healthRecordContract.submitTransaction("UpdateHealthRecord", recordId, healthRecord.getDate(), healthRecord.getPatientID(), healthRecord.getDoctorID(), healthRecord.getSymptom(), healthRecord.getDiagnosis(), healthRecord.getTreatment(), healthRecord.getPrescriptionID());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteHealthRecord/{recordId}")
    public ResponseEntity<String> deleteHealthRecord(@PathVariable String recordId) {
        try {
            healthRecordContract.submitTransaction("DeleteHealthRecord", recordId);
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
            byte[] evaluateResult = healthRecordContract.evaluateTransaction("GetAllRecords");
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

    @PostMapping("/createDoctor/{doctorId}")
    public ResponseEntity<String> createDoctor(@PathVariable String doctorId, @RequestBody Doctor doctor) {
        try {
            byte[] result = doctorContract.submitTransaction("CreateDoctor", doctorId, doctor.getName(), String.valueOf(doctor.getAge()), doctor.getGender(), doctor.getAddress(), doctor.getEmail(), doctor.getPhone(), doctor.getHospitalID(), doctor.getSpeciality());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readDoctor/{doctorId}")
    public ResponseEntity<String> readDoctorById(@PathVariable String doctorId) {
        try {
            byte[] evaluateResult = doctorContract.evaluateTransaction("ReadDoctor", doctorId);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateDoctor/{doctorId}")
    public ResponseEntity<String> updateDoctor(@PathVariable String doctorId, @RequestBody Doctor doctor) {
        try {
            byte[] result = doctorContract.submitTransaction("UpdateDoctor", doctorId, doctor.getName(), String.valueOf(doctor.getAge()), doctor.getGender(), doctor.getAddress(), doctor.getEmail(), doctor.getPhone(), doctor.getHospitalID(), doctor.getSpeciality());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteDoctor/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String doctorId) {
        try {
            doctorContract.submitTransaction("DeleteDoctor", doctorId);
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
            byte[] evaluateResult = doctorContract.evaluateTransaction("GetAllDoctors");
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

    @PostMapping("/createProducer/{producerId}")
    public ResponseEntity<String> createProducer(@PathVariable String producerId, @RequestBody Producer producer) {
        try {
            byte[] result = producerContract.submitTransaction("CreateProducer", producerId, producer.getName(), producer.getAddress());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readProducer/{producerId}")
    public ResponseEntity<String> readProducerById(@PathVariable String producerId) {
        try {
            byte[] evaluateResult = producerContract.evaluateTransaction("ReadProducer", producerId);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProducer/{producerId}")
    public ResponseEntity<String> updateProducer(@PathVariable String producerId, @RequestBody Producer producer) {
        try {
            byte[] result = producerContract.submitTransaction("UpdateProducer", producerId, producer.getName(), producer.getAddress());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteProducer/{producerId}")
    public ResponseEntity<String> deleteProducer(@PathVariable String producerId) {
        try {
            producerContract.submitTransaction("DeleteProducer", producerId);
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
            byte[] evaluateResult = producerContract.evaluateTransaction("GetAllProducers");
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
    @PostMapping("/createHospital/{hospitalId}")
    public ResponseEntity<String> createHospital(@PathVariable String hospitalId, @RequestBody Hospital hospital) {
        try {
            byte[] result = hospitalContract.submitTransaction("CreateHospital", hospitalId, hospital.getName(), hospital.getAddress(), hospital.getPhone(), hospital.getEmail());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.CREATED);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/readHospital/{hospitalId}")
    public ResponseEntity<String> readHospitalById(@PathVariable String hospitalId) {
        try {
            byte[] evaluateResult = hospitalContract.evaluateTransaction("ReadHospital", hospitalId);
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateHospital/{hospitalId}")
    public ResponseEntity<String> updateHospital(@PathVariable String hospitalId, @RequestBody Hospital hospital) {
        try {
            byte[] result = hospitalContract.submitTransaction("UpdateHospital", hospitalId, hospital.getName(), hospital.getAddress(), hospital.getPhone(), hospital.getEmail());
            return new ResponseEntity<>(prettyJson(result), HttpStatus.OK);
        } catch (EndorseException | SubmitException | CommitStatusException | CommitException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteHospital/{hospitalId}")
    public ResponseEntity<String> deleteHospital(@PathVariable String hospitalId) {
        try {
            hospitalContract.submitTransaction("DeleteHospital", hospitalId);
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
            byte[] evaluateResult = hospitalContract.evaluateTransaction("GetAllHospitals");
            String resultJSON = prettyJson(evaluateResult);
            return new ResponseEntity<>(resultJSON, HttpStatus.OK);
        } catch (GatewayException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
