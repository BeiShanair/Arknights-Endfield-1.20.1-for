package com.besson.endfield.renderer.item;

import com.besson.endfield.item.custom.production2.GearingUnitItem;
import com.besson.endfield.model.item.production2.GearingUnitItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GearingUnitItemRenderer extends GeoItemRenderer<GearingUnitItem> {
    public GearingUnitItemRenderer() {
        super(new GearingUnitItemModel());
    }
}
