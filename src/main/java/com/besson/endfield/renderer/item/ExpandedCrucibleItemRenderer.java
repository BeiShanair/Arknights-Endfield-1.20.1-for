package com.besson.endfield.renderer.item;

import com.besson.endfield.item.custom.production2.ExpandedCrucibleItem;
import com.besson.endfield.model.item.production2.ExpandedCrucibleItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ExpandedCrucibleItemRenderer extends GeoItemRenderer<ExpandedCrucibleItem> {
    public ExpandedCrucibleItemRenderer() {
        super(new ExpandedCrucibleItemModel());
    }
}
