package utilites;

import framework.SimulationType;
import org.jbox2d.dynamics.BodyType;

public class SimulationTypeToBodyTypeConverter {
    public BodyType Convert(SimulationType type){

        switch (type){
            case Full: return BodyType.DYNAMIC;
            case Movable: return BodyType.KINEMATIC;
            case NonMovable: return BodyType.STATIC;
            default: return BodyType.DYNAMIC;
        }
    }
}
