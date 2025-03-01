package pro.komaru.tridot.rhino.mod.wrapper;

import net.minecraft.core.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author LatvianModder
 */
public interface DirectionWrapper {
	Direction down = Direction.DOWN;
	Direction up = Direction.UP;
	Direction north = Direction.NORTH;
	Direction south = Direction.SOUTH;
	Direction west = Direction.WEST;
	Direction east = Direction.EAST;
	Direction DOWN = Direction.DOWN;
	Direction UP = Direction.UP;
	Direction NORTH = Direction.NORTH;
	Direction SOUTH = Direction.SOUTH;
	Direction WEST = Direction.WEST;
	Direction EAST = Direction.EAST;
	Map<String, Direction> ALL = Collections.unmodifiableMap(Arrays.stream(Direction.values()).collect(Collectors.toMap(Direction::getSerializedName, Function.identity())));
}