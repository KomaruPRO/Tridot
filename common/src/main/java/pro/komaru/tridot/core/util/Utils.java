package pro.komaru.tridot.core.util;

import pro.komaru.tridot.core.util.utils.*;
import pro.komaru.tridot.platform.Services;
import pro.komaru.tridot.platform.services.*;

public class Utils {
    public static IPlatformHelper PLATFORM = Services.PLATFORM;

    public static FileHelper FILE = FileHelper.get();
    public static RaycastHelper RAYCAST = RaycastHelper.get();
    public static ReflectHelper REFLECT = ReflectHelper.get();
    public static AnglesHelper ANGLES = AnglesHelper.get();
}
