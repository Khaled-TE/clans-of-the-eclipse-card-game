package game.units;

import game.enums.UnitType;




public abstract class SpecialUnit extends Unit {
    private static final long serialVersionUID = 1L;
    private String description;
    public SpecialUnit( UnitType unitType,String description){
        super(unitType);
        this.description = description;
    }
   public String getDescription(){
        return description;
   }

   public String toString(){
        return ""+getType();
   }
}
