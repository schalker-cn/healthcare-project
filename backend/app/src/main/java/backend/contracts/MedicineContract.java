package backend.contracts;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

import backend.models.Medicine;

@Contract(
        name = "MedicineContract",
        info = @Info(
                title = "medicine record contract",
                description = "contract to the life cycle of medicines",
                version= "1.0.0"
        )
)

public final class MedicineContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum MedicineRecordErrors {
        MEDICINE_NOT_FOUND,
        MEDICINE_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean MedicineExists(final Context ctx, final String medicineID) {
        ChaincodeStub stub = ctx.getStub();
        String medicineJSON = stub.getStringState(medicineID);
        return (medicineJSON != null && !medicineJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock medicine records here
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Medicine CreateMedicine(final Context ctx, final String medicineID, final String producerID, final String name, final String productionDate, final String expirationDate, final String currentOwner, final String previousOwners) {
        ChaincodeStub stub = ctx.getStub();

        if (MedicineExists(ctx, medicineID)) {
            String errorMessage = String.format("Medicine %s is already created", name);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicineRecordErrors.MEDICINE_ALREADY_EXISTS.toString());
        }

        Medicine medicine = new Medicine(medicineID, producerID, name, productionDate, expirationDate, currentOwner, previousOwners);
        String recordJSON = genson.serialize(medicine);
        stub.putStringState(medicineID, recordJSON);
        return medicine;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Medicine ReadMedicine(final Context ctx, final String medicineID) {
        ChaincodeStub stub = ctx.getStub();
        String medicineJSON = stub.getStringState(medicineID);

        if (medicineJSON == null || medicineJSON.isEmpty()) {
            String errorMessage = String.format("Medicine record %s does not exist", medicineID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicineRecordErrors.MEDICINE_NOT_FOUND.toString());
        }

        Medicine medicine = genson.deserialize(medicineJSON, Medicine.class);
        return medicine;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Medicine UpdateMedicine(final Context ctx, final String medicineID, final String producerID, final String name, final String productionDate, final String expirationDate, final String currentOwner, final String previousOwners) {
        ChaincodeStub stub = ctx.getStub();

        if (!MedicineExists(ctx, medicineID)) {
            String errorMessage = String.format("Medicine record %s does not exist", medicineID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicineRecordErrors.MEDICINE_NOT_FOUND.toString());
        }

        Medicine newMedicine = new Medicine(medicineID, producerID, name, productionDate, expirationDate, currentOwner, previousOwners);
        String medicineJSON = genson.serialize(newMedicine);
        stub.putStringState(medicineID, medicineJSON);
        return newMedicine;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteMedicine(final Context ctx, final String medicineID) {
        ChaincodeStub stub = ctx.getStub();

        if (!MedicineExists(ctx, medicineID)) {
            String errorMessage = String.format("Medicine record %s does not exist", medicineID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MedicineRecordErrors.MEDICINE_NOT_FOUND.toString());
        }

        stub.delState(medicineID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllMedicines(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Medicine> queryResults = new ArrayList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result : results) {
            Medicine medicine = genson.deserialize(result.getStringValue(), Medicine.class);
            queryResults.add(medicine);
        }

        final String response = genson.serialize(queryResults);
        return response;
    }
}
