package game.units;

import game.enums.UnitType;
import game.model.Battle;
import game.model.PlayedUnit;

public class Scout extends SpecialUnit {
    private static final long serialVersionUID = 1L;

    public Scout( UnitType unitType, String description){super(unitType,description);}

    @Override
    public int getBattlePriority(Battle context, PlayedUnit self) { return 500; }
}
