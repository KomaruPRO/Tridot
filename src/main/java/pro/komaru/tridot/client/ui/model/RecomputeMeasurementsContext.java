package stellar.qrix.neoforge.infrastructure.ui.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class RecomputeMeasurementsContext {
    float availableWidth, availableHeight;
}