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

import backend.models.HealthRecord;
import backend.models.Prescription;

@Contract(
        name = "HealthRecordContract",
        info = @Info(
                title = "health record contract",
                description = "contract to handle health record modification",
                version= "1.0.0"
        )
)

@Default
public final class HealthRecordContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum HealthRecordErrors {
        RECORD_NOT_FOUND,
        RECORD_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean RecordExists(final Context ctx, final String recordID) {
        ChaincodeStub stub = ctx.getStub();
        String recordJSON = stub.getStringState(recordID);
        return (recordJSON != null && recordJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock health records here
        CreateHealthRecord(ctx, "123", "2024-06-06", "001", "001", "headache, fever", "have a cold", "take medicine and get rest", "001");
        
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public HealthRecord CreateHealthRecord(final Context ctx, final String recordID, final String date, final String patientID, final String doctorID, final String symptom, final String diagnosis, final String treatment, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();

        if (RecordExists(ctx, recordID)) {
            String errorMessage = String.format("Health record %s of patient %s already exists", recordID, patientID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HealthRecordErrors.RECORD_ALREADY_EXISTS.toString());
        }

        HealthRecord record = new HealthRecord(recordID, date, patientID, doctorID, symptom, diagnosis, treatment, prescriptionID);
        String recordJSON = genson.serialize(record);
        stub.putStringState(recordID, recordJSON);
        return record;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public HealthRecord ReadHealthRecord(final Context ctx, final String recordID) {
        ChaincodeStub stub = ctx.getStub();
        String recordJSON = stub.getStringState(recordID);

        if (recordJSON == null || recordJSON.isEmpty()) {
            String errorMessage = String.format("Health record %s does not exist", recordID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HealthRecordErrors.RECORD_NOT_FOUND.toString());
        }

        HealthRecord record = genson.deserialize(recordJSON, HealthRecord.class);
        return record;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public HealthRecord updateHealthRecord(final Context ctx, final String recordID, final String date, final String patientID, final String doctorID, final String symptom, final String diagnosis, final String treatment, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();

        if(!RecordExists(ctx, recordID)) {
            String errorMessage = String.format("Health record %s does not exist", recordID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HealthRecordErrors.RECORD_NOT_FOUND.toString());
        }

        HealthRecord newRecord = new HealthRecord(recordID, date, patientID, doctorID, symptom, diagnosis, treatment, prescriptionID);
        String recordJSON = genson.serialize(newRecord);
        stub.putStringState(recordID, recordJSON);
        return newRecord;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteHealthRecord(final Context ctx, final String recordID) {
        ChaincodeStub stub = ctx.getStub();

        if(!RecordExists(ctx, recordID)) {
            String errorMessage = String.format("Health record %s does not exist", recordID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HealthRecordErrors.RECORD_NOT_FOUND.toString());
        }

        stub.delState(recordID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllRecords(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<HealthRecord> queryResults = new ArrayList<HealthRecord>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            HealthRecord record = genson.deserialize(result.getStringValue(), HealthRecord.class);
            queryResults.add(record);
        }

        final String response = genson.serialize(queryResults);
        return response;

    }
}
