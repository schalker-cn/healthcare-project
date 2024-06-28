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

import backend.models.Hospital;


@Contract(
        name = "HospitalContract",
        info = @Info(
                title = "hospital contract",
                description = "contract to handle hospital information",
                version= "1.0.0"
        )
)

public final class HospitalContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum HospitalErrors {
        HOSPITAL_NOT_FOUND,
        HOSPITAL_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean HospitalExists(final Context ctx, final String hospitalID) {
        ChaincodeStub stub = ctx.getStub();
        String hospitalJSON = stub.getStringState(hospitalID);
        return (hospitalJSON != null && !hospitalJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock hospitals here
        
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Hospital CreateHospital(final Context ctx, final String hospitalID, final String name, final String address, final String phone, final String email) {
        ChaincodeStub stub = ctx.getStub();

        if (HospitalExists(ctx, hospitalID)) {
            String errorMessage = String.format("hospital %s already exists", hospitalID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HospitalErrors.HOSPITAL_ALREADY_EXISTS.toString());
        }

        Hospital hospital = new Hospital(hospitalID, name, address, phone, email);
        String hospitalJSON = genson.serialize(hospital);
        stub.putStringState(hospitalID, hospitalJSON);
        return hospital;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Hospital ReadHospital(final Context ctx, final String hospitalID) {
        ChaincodeStub stub = ctx.getStub();
        String hospitalJSON = stub.getStringState(hospitalID);

        if (hospitalJSON == null || hospitalJSON.isEmpty()) {
            String errorMessage = String.format("hospital %s does not exist", hospitalID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HospitalErrors.HOSPITAL_NOT_FOUND.toString());
        }

        Hospital hospital = genson.deserialize(hospitalJSON, Hospital.class);
        return hospital;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Hospital UpdateHospital(final Context ctx, final String hospitalID, final String name, final String address, final String phone, final String email) {
        ChaincodeStub stub = ctx.getStub();

        if(!HospitalExists(ctx, hospitalID)) {
            String errorMessage = String.format("hospital %s does not exist", hospitalID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HospitalErrors.HOSPITAL_NOT_FOUND.toString());
        }

        Hospital newHospital = new Hospital(hospitalID, name, address, phone, email);
        String hospitalJSON = genson.serialize(newHospital);
        stub.putStringState(hospitalID, hospitalJSON);
        return newHospital;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteHospital(final Context ctx, final String hospitalID) {
        ChaincodeStub stub = ctx.getStub();

        if(!HospitalExists(ctx, hospitalID)) {
            String errorMessage = String.format("hospital %s does not exist", hospitalID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, HospitalErrors.HOSPITAL_NOT_FOUND.toString());
        }

        stub.delState(hospitalID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllHospitals(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Hospital> queryResults = new ArrayList<Hospital>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Hospital hospital = genson.deserialize(result.getStringValue(), Hospital.class);
            queryResults.add(hospital);
        }

        final String response = genson.serialize(queryResults);
        return response;

    }
}

