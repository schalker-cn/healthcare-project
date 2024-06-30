package backend.contracts;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

import backend.CheckIdPrefix;
import backend.models.Patient;

@Contract(
        name = "PatientContract",
        info = @Info(
                title = "patient contract",
                description = "contract to handle patient's personal information",
                version= "1.0.0"
        )
)

public final class PatientContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum PatientErrors {
        PATIENT_NOT_FOUND,
        PATIENT_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean PatientExists(final Context ctx, final String patientID) {
        ChaincodeStub stub = ctx.getStub();
        String patientJSON = stub.getStringState(patientID);
        return (patientJSON != null && !patientJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock patient records here
        System.out.println("init the ledger");
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Patient CreatePatient(final Context ctx, final String patientID, final String hospitalID, final String name, final int age, final String gender, final String email, final String phone, final String accessToDoctors) {
        String patientIdCopy = CheckIdPrefix.checkAndAddPrefix(patientID, "patient_");
        String hospitalIdCopy = CheckIdPrefix.checkAndAddPrefix(hospitalID, "hospital_");
        ChaincodeStub stub = ctx.getStub();
    
        if (PatientExists(ctx, patientIdCopy)) {
            String errorMessage = String.format("patient %s already exists", patientIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PatientErrors.PATIENT_ALREADY_EXISTS.toString());
        }
    
        Patient patient = new Patient(patientIdCopy, hospitalIdCopy, name, age, gender, email, phone, accessToDoctors);
        String patientJSON = genson.serialize(patient);
        stub.putStringState(patientIdCopy, patientJSON);
        return patient;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Patient ReadPatient(final Context ctx, final String patientID) {
        String patientIdCopy = CheckIdPrefix.checkAndAddPrefix(patientID, "patient_");
        ChaincodeStub stub = ctx.getStub();
        String patientJSON = stub.getStringState(patientIdCopy);

        if (patientJSON == null || patientJSON.isEmpty()) {
            String errorMessage = String.format("patient %s does not exist", patientIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PatientErrors.PATIENT_NOT_FOUND.toString());
        }

        Patient patient = genson.deserialize(patientJSON, Patient.class);
        return patient;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Patient UpdatePatient(final Context ctx, final String patientID, final String hospitalID, final String name, final int age, final String gender, final String email, final String phone, final String accessToDoctors) {
        String patientIdCopy = CheckIdPrefix.checkAndAddPrefix(patientID, "patient_");
        String hospitalIdCopy = CheckIdPrefix.checkAndAddPrefix(hospitalID, "hospital_");
        ChaincodeStub stub = ctx.getStub();

        if (!PatientExists(ctx, patientIdCopy)) {
            String errorMessage = String.format("patient %s does not exist", patientIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PatientErrors.PATIENT_NOT_FOUND.toString());
        }

        Patient newPatient = new Patient(patientIdCopy, hospitalIdCopy, name, age, gender, email, phone, accessToDoctors);
        String patientJSON = genson.serialize(newPatient);
        stub.putStringState(patientIdCopy, patientJSON);
        return newPatient;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeletePatient(final Context ctx, final String patientID) {
        String patientIdCopy = CheckIdPrefix.checkAndAddPrefix(patientID, "patient_");
        ChaincodeStub stub = ctx.getStub();

        if(!PatientExists(ctx, patientIdCopy)) {
            String errorMessage = String.format("patient %s does not exist", patientIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PatientErrors.PATIENT_NOT_FOUND.toString());
        }

        stub.delState(patientIdCopy);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllPatients(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Patient> queryResults = new ArrayList<Patient>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("patient_", "patient_\uFFFF");
        for (KeyValue result: results) {
            Patient patient = genson.deserialize(result.getStringValue(), Patient.class);
            queryResults.add(patient);
        }

        final String response = genson.serialize(queryResults);
        return response;

    }
}
