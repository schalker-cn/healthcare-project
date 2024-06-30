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

import backend.CheckIdPrefix;
import backend.models.Producer;



@Contract(
        name = "ProducerContract",
        info = @Info(
                title = "producer contract",
                description = "contract to handle medicine producer details",
                version= "1.0.0"
        )
)

public final class ProducerContract implements ContractInterface {
    private final Genson genson = new Genson();

    private enum ProducerErrors {
        PRODUCER_NOT_FOUND,
        PRODUCER_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean ProducerExists(final Context ctx, final String producerID) {
        ChaincodeStub stub = ctx.getStub();
        String producerJSON = stub.getStringState(producerID);
        return (producerJSON != null && !producerJSON.isEmpty());
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void InitLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        // add mock producers here
        
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Producer CreateProducer(final Context ctx, final String producerID, final String name, final String address) {
        String producerIdCopy = CheckIdPrefix.checkAndAddPrefix(producerID, "producer_");
        ChaincodeStub stub = ctx.getStub();

        if (ProducerExists(ctx, producerIdCopy)) {
            String errorMessage = String.format("producer %s already exists", producerIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProducerErrors.PRODUCER_ALREADY_EXISTS.toString());
        }

        Producer producer = new Producer(producerIdCopy, name, address); 
        String producerJSON = genson.serialize(producer);
        stub.putStringState(producerIdCopy, producerJSON);
        return producer;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Producer ReadProducer(final Context ctx, final String producerID) {
        String producerIdCopy = CheckIdPrefix.checkAndAddPrefix(producerID, "producer_");
        ChaincodeStub stub = ctx.getStub();
        String producerJSON = stub.getStringState(producerIdCopy);

        if (producerJSON == null || producerJSON.isEmpty()) {
            String errorMessage = String.format("producer %s does not exist", producerIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProducerErrors.PRODUCER_NOT_FOUND.toString());
        }

        Producer producer = genson.deserialize(producerJSON, Producer.class);
        return producer;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Producer UpdateProducer(final Context ctx, final String producerID, final String name, final String address) {
        String producerIdCopy = CheckIdPrefix.checkAndAddPrefix(producerID, "producer_");
        ChaincodeStub stub = ctx.getStub();

        if(!ProducerExists(ctx, producerIdCopy)) {
            String errorMessage = String.format("producer %s does not exist", producerIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProducerErrors.PRODUCER_NOT_FOUND.toString());
        }

        Producer newProducer = new Producer(producerIdCopy, name, address);
        String producerJSON = genson.serialize(newProducer);
        stub.putStringState(producerIdCopy, producerJSON);
        return newProducer;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void DeleteProducer(final Context ctx, final String producerID) {
        String producerIdCopy = CheckIdPrefix.checkAndAddPrefix(producerID, "producer_");
        ChaincodeStub stub = ctx.getStub();

        if(!ProducerExists(ctx, producerIdCopy)) {
            String errorMessage = String.format("producer %s does not exist", producerIdCopy);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, ProducerErrors.PRODUCER_NOT_FOUND.toString());
        }

        stub.delState(producerIdCopy);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetAllProducers(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        List<Producer> queryResults = new ArrayList<Producer>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange("producer_", "producer_\uFFFF");
        for (KeyValue result: results) {
            Producer producer = genson.deserialize(result.getStringValue(), Producer.class);
            queryResults.add(producer);
        }

        final String response = genson.serialize(queryResults);
        return response;

    }
}


