package game.units;

import game.enums.UnitType;
import game.model.Battle;
import game.model.PlayedUnit;

public class Overlord extends SpecialUnit{
    private static final long serialVersionUID = 1L;

    public Overlord( UnitType unitType, String description){super(unitType,description);}

    @Override
    public int getBattlePriority(Battle context, PlayedUnit self) {
        for (PlayedUnit pu : context.getPlayedUnits()) {
            if (pu.getUnit().getType() == UnitType.ORACLE) {
                return 0;
            }
        }
        return 1000;
    }
}
