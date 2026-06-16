package game.units;

import game.enums.UnitType;
import game.model.Battle;
import game.model.PlayedUnit;
import java.io.Serializable;

public abstract class Unit implements Serializable {
    private static final long serialVersionUID = 1L;
    private UnitType unitType;

    public Unit(UnitType unitType) {
        this.unitType = unitType;
    }

    public UnitType getType() {
        return unitType;
    }

    public abstract int getBattlePriority(Battle context, PlayedUnit self);
}