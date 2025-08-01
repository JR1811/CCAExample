package net.shirojr.ccaexample.init;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.shirojr.ccaexample.CCAExample;

public class CCAExampleTags {
    public static class ItemTags {
        public static final TagKey<Item> DISPLAYABLE = TagKey.of(RegistryKeys.ITEM, CCAExample.getId("displayable"));
    }
}
