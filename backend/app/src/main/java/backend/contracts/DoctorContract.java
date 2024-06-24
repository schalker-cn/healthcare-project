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

import backend.models.Doctor;

@Contract(
        name = "DoctorContract",
        info = @Info(
                title = "doctor contract",
                description = "contract to handle doctor information modification",
                version= "1.0.0"
        )
)

public final class DoctorContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum DoctorErrors {
        DOCTOR_NOT_FOUND,
        DOCTOR_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean DoctorExists(final Context ctx, final String doctorID) {
        ChaincodeStub stub = ctx.getStub();
        String doctorJSON = stub.getStringState(doctorID);
        return (doctorJSON != null && !doctorJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock doctor records here
        System.out.println("init the ledger");
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Doctor CreateDoctor(final Context ctx, final String doctorID, final String name, final int age, final String gender, final String address, final String email, final String phone, final String hospitalID, final String speciality) {
        ChaincodeStub stub = ctx.getStub();

        if (DoctorExists(ctx, doctorID)) {
            String errorMessage = String.format("doctor %s already exists", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, DoctorErrors.DOCTOR_ALREADY_EXISTS.toString());
        }

        Doctor doctor = new Doctor(doctorID, name, age, gender, address, email, phone, hospitalID, speciality);
        String doctorJSON = genson.serialize(doctor);
        stub.putStringState(doctorID, doctorJSON);
        return doctor;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Doctor ReadDoctor(final Context ctx, final String doctorID) {
        ChaincodeStub stub = ctx.getStub();
        String doctorJSON = stub.getStringState(doctorID);

        if (doctorJSON == null || doctorJSON.isEmpty()) {
            String errorMessage = String.format("doctor %s does not exist", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, DoctorErrors.DOCTOR_NOT_FOUND.toString());
        }

        Doctor doctor = genson.deserialize(doctorJSON, Doctor.class);
        return doctor;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Doctor updateDoctor(final Context ctx, final String doctorID, final String name, final int age, final String gender, final String address, final String email, final String phone, final String hospitalID, final String speciality) {
        ChaincodeStub stub = ctx.getStub();

        if(!DoctorExists(ctx, doctorID)) {
            String errorMessage = String.format("doctor %s does not exist", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, DoctorErrors.DOCTOR_NOT_FOUND.toString());
        }

        Doctor newDoctor = new Doctor(doctorID, name, age, gender, address, email, phone, hospitalID, speciality);
        String doctorJSON = genson.serialize(newDoctor);
        stub.putStringState(doctorID, doctorJSON);
        return newDoctor;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteDoctor(final Context ctx, final String doctorID) {
        ChaincodeStub stub = ctx.getStub();

        if(!DoctorExists(ctx, doctorID)) {
            String errorMessage = String.format("doctor %s does not exist", doctorID);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, DoctorErrors.DOCTOR_NOT_FOUND.toString());
        }

        stub.delState(doctorID);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllDoctors(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Doctor> queryResults = new ArrayList<Doctor>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");
        for (KeyValue result: results) {
            Doctor doctor = genson.deserialize(result.getStringValue(), Doctor.class);
            queryResults.add(doctor);
        }

        final String response = genson.serialize(queryResults);
        return response;

    }
}

