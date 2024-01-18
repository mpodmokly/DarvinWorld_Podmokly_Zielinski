package agh.daycare;
import agh.statistics.Statisticer;
import agh.mapEntities.WorldMap;

public class DayCare {
    private int dayCount = 0;
    private final MovingMechanism movingMechanism;
    private final PlantConsumptionMechanism plantConsumptionMechanism;
    private final PlantGrowthMechanism plantGrowthMechanism;
    private final ReproductionMechanism reproductionMechanism;
    public final WorldMap worldMap;
    final Statisticer statisticer;
    public DayCare(MapVariant mapVariant, GeneVariant geneVariant, WorldMap worldMap, int plantsCount, int energyRequirements, int energyReproduce,
                   int maxMutation, int minMutation, int energyLoss, int energyGain){
        this.worldMap = worldMap;

        switch (mapVariant){
            case EARTH -> {
                movingMechanism = new MovingMechanismNormal(worldMap,energyLoss,this);
            }
            case PORTALS -> {
                movingMechanism = new MovingMechanismSpecial(worldMap, energyLoss, this, energyReproduce);
            }
            default -> throw new IllegalStateException("Unexpected value: " + mapVariant);
        }
        switch (geneVariant){
            case NORMAL -> {
                reproductionMechanism = new ReproductionMechanismNormal(worldMap, energyRequirements,
                        energyReproduce, maxMutation, minMutation, this);
            }
            case SPECIAL -> {
                reproductionMechanism = new ReproductionMechanismSpecial(worldMap, energyRequirements,
                        energyReproduce, maxMutation, minMutation, this);
            }
            default -> throw new IllegalStateException("Unexpected value: " + mapVariant);
        }

        plantGrowthMechanism = new PlantGrowthMechanism(this,plantsCount);
        plantConsumptionMechanism = new PlantConsumptionMechanism(energyGain, this);
        statisticer = new Statisticer(this);
    }

    public int getDayCount() {
        return dayCount;
    }

    public PlantGrowthMechanism getPlantGrowthMechanism() {return plantGrowthMechanism;}

    public void simulateDay(){
        dayCount++;
        movingMechanism.work(); //also includes killing mechanism and lowering energy
        plantConsumptionMechanism.work();
        reproductionMechanism.work();
        plantGrowthMechanism.work();
        statisticer.showStats();
    }

}
