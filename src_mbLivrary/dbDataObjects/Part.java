package dbDataObjects;

public class Part {

    private int idPart;
    private PartsType type;
    private int fabricationTime;
    private String dimensions;
    private float productionCost;
    
    public Part(int id, PartsType type, int fabricationTime, String dimensions, float productionCost) {
        this.idPart = id;
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
    
    public int getIdPart() {
        return this.idPart;
    }
}
