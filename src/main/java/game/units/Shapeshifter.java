package game.units;

import game.enums.UnitType;
import game.model.Battle;
import game.model.PlayedUnit;

public class Shapeshifter extends SpecialUnit {
    private static final long serialVersionUID = 1L;

    public Shapeshifter(UnitType unitType, String description) {super(unitType, description);}

    @Override
    public int getBattlePriority(Battle context, PlayedUnit self) {
        if (self.getIdentity() == game.enums.DeclaredIdentity.RAIDER) return 900;
        if (self.getIdentity() == game.enums.DeclaredIdentity.SCOUT) return 500;
        return 0;
    }
}