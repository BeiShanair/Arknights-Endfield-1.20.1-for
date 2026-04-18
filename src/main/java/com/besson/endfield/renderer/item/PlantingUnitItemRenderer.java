package com.besson.endfield.renderer.item;

import com.besson.endfield.item.custom.production1.PlantingUnitItem;
import com.besson.endfield.model.item.production1.PlantingUnitItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PlantingUnitItemRenderer extends GeoItemRenderer<PlantingUnitItem> {
    public PlantingUnitItemRenderer() {
        super(new PlantingUnitItemModel());
    }
}
