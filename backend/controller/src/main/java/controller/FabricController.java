package controller;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Restcontroller
public class FabricController {
    @Autowired
    private Contract contract;

    @PostMapping("/createAsset")
    public String createAsset(@RequestBody Asset asset) {
        try {
            byte[] result = contract.submitTransaction("CreateAsset", asset.getId(), asset.getColor(), asset.getSize(), asset.getOwner(), asset.getValue());
            return new String(result);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
