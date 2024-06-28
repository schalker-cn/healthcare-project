package backend.models;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonCreator;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Producer {

    @Property()
    private final String producerID;

    @Property()
    private final String name;

    @Property()
    private final String address;

    public String getProducerID() {
        return producerID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Producer() {
        this.producerID = "mock";
        this.name = "mock";
        this.address = "mock";
    }

    @JsonCreator
    public Producer(@JsonProperty("producerID") String producerID, @JsonProperty("name") String name, @JsonProperty("address") String address) {
        this.producerID = producerID;
        this.name = name;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producer producer = (Producer) o;
        return Objects.equals(getProducerID(), producer.getProducerID()) && Objects.equals(getName(), producer.getName()) && Objects.equals(getAddress(), producer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProducerID(), getName(), getAddress());
    }
}
