package pro.komaru.tridot.core.util;

import pro.komaru.tridot.core.util.utils.FileHelper;
import pro.komaru.tridot.platform.Services;
import pro.komaru.tridot.platform.services.IPlatformHelper;

public class Helpers {
    public static IPlatformHelper PLATFORM = Services.PLATFORM;
    public static FileHelper FILE = FileHelper.get();
}
