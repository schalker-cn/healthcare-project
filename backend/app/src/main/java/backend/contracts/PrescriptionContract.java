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

import backend.models.Prescription;

@Contract(
        name = "PrescriptionContract",
        info = @Info(
                title = "prescription contract",
                description = "contract to handle prescription issued by doctors",
                version= "1.0.0"
        )
)

public final class PrescriptionContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum PrescriptionErrors {
        PRESCRIPTION_NOT_FOUND,
        PRESCRIPTION_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean PrescriptionExists(final Context ctx, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();
        String prescriptionJSON = stub.getStringState(prescriptionID);
        return (prescriptionJSON != null && !prescriptionJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock prescription records here
        System.out.println("init the ledger");
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Prescription CreatePrescription(final Context ctx, final String prescriptionID, final String medicineID, final int dosage, final int duration) {
        ChaincodeStub stub = ctx.getStub();

        if (PrescriptionExists(ctx, prescriptionID)) {
            String errorMessage = String.format("prescription %s already exists", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PrescriptionErrors.PRESCRIPTION_ALREADY_EXISTS.toString());
        }

        Prescription prescription = new Prescription(prescriptionID, medicineID, dosage, duration);
        String prescriptionJSON = genson.serialize(prescription);
        stub.putStringState(prescriptionID, prescriptionJSON);
        return prescription;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Prescription ReadPrescription(final Context ctx, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();
        String prescriptionJSON = stub.getStringState(prescriptionID);

        if (prescriptionJSON == null || prescriptionJSON.isEmpty()) {
            String errorMessage = String.format("prescription %s does not exist", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PrescriptionErrors.PRESCRIPTION_NOT_FOUND.toString());
        }

        Prescription prescription = genson.deserialize(prescriptionJSON, Prescription.class);
        return prescription;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Prescription UpdatePrescription(final Context ctx,  final String prescriptionID, final String medicineID, final int dosage, final int duration) {
        ChaincodeStub stub = ctx.getStub();

        if(!PrescriptionExists(ctx, prescriptionID)) {
            String errorMessage = String.format("prescription %s does not exist", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PrescriptionErrors.PRESCRIPTION_NOT_FOUND.toString());
        }

        Prescription newPrescription = new Prescription(prescriptionID, medicineID, dosage, duration);
        String prescriptionJSON = genson.serialize(newPrescription);
        stub.putStringState(prescriptionID, prescriptionJSON);
        return newPrescription;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeletePrescription(final Context ctx, final String prescriptionID) {
        ChaincodeStub stub = ctx.getStub();

        if(!PrescriptionExists(ctx, prescriptionID)) {
            String errorMessage = String.format("prescription %s does not exist", prescriptionID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, PrescriptionErrors.PRESCRIPTION_NOT_FOUND.toString());
        }

        stub.delState(prescriptionID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllPrescriptions(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Prescription> queryResults = new ArrayList<Prescription>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Prescription prescription = genson.deserialize(result.getStringValue(), Prescription.class);
            queryResults.add(prescription);
        }

        final String response = genson.serialize(queryResults);
        return response;

    }
}


