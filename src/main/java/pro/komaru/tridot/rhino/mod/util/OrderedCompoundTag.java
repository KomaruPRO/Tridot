package pro.komaru.tridot.rhino.mod.util;

import net.minecraft.nbt.*;

import java.io.*;
import java.util.*;

public class OrderedCompoundTag extends CompoundTag {
	public final Map<String, Tag> tagMap;

	public OrderedCompoundTag(Map<String, Tag> map) {
		super(map);
		tagMap = map;
	}

	public OrderedCompoundTag() {
		this(new LinkedHashMap<>());
	}

	@Override
	public void write(DataOutput dataOutput) throws IOException {
		for (Map.Entry<String, Tag> entry : tagMap.entrySet()) {
			Tag tag = entry.getValue();
			dataOutput.writeByte(tag.getId());

			if (tag.getId() != 0) {
				dataOutput.writeUTF(entry.getKey());
				tag.write(dataOutput);
			}
		}

		dataOutput.writeByte(0);
	}
}
