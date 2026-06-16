package game.units;

import game.enums.Clan;
import game.enums.UnitType;



public class KingUnit extends ClanUnit {
    private static final long serialVersionUID = 1L;

    public KingUnit(Clan clan) {
        super(UnitType.KING, clan, 14);
    }
}