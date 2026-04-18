package com.besson.endfield.renderer.item;

import com.besson.endfield.item.custom.production1.MouldingUnitItem;
import com.besson.endfield.model.item.production1.MouldingUnitItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MouldingUnitItemRenderer extends GeoItemRenderer<MouldingUnitItem> {
    public MouldingUnitItemRenderer() {
        super(new MouldingUnitItemModel());
    }
}
