package dbDataObjects;

public class Part {

    private PartsType type;
    private int fabricationTime;
    private String dimensions;
    private float productionCost;
    
    public Part(PartsType type, int fabricationTime, String dimensions, float productionCost) {
        this.productionCost = productionCost;
        this.dimensions = dimensions;
        this.fabricationTime = fabricationTime;
        this.type = type;
    }
    
    public int getfabricationTime(){
        return fabricationTime;
    }
    
    public float getProductionCost(){
        return productionCost;
    }
    
    public PartsType getPartType() {
        return type;
    }
    
    public String getDimensions() {
        return dimensions;
    }
}
