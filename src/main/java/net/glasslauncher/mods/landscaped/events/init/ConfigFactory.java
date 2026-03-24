package net.glasslauncher.mods.landscaped.events.init;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi3.impl.SeptFunction;
import net.glasslauncher.mods.gcapi3.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi3.impl.object.entry.EnumConfigEntryHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

public class ConfigFactory implements ConfigFactoryProvider {
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> builder) {
        builder.put(LandscapedConfig.BiomeScale.class, getEnumBuilder(LandscapedConfig.BiomeScale.class));
        builder.put(LandscapedConfig.CaveScale.class, getEnumBuilder(LandscapedConfig.CaveScale.class));
        builder.put(LandscapedConfig.BiomeDistributorOption.class, getEnumBuilder(LandscapedConfig.BiomeDistributorOption.class));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> builder) {
        builder.put(LandscapedConfig.BiomeScale.class, enumEntry -> enumEntry);
        builder.put(LandscapedConfig.CaveScale.class, enumEntry -> enumEntry);
        builder.put(LandscapedConfig.BiomeDistributorOption.class, enumEntry -> enumEntry);
    }

    @SuppressWarnings("unchecked") // cry more
    private <T extends Enum<?>> SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>> getEnumBuilder(Class<T> enu) {
        return (id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) -> {
            int enumOrdinal;
            if(enumOrOrdinal instanceof Integer ordinal) {
                enumOrdinal = ordinal;
            }
            else {
                enumOrdinal = ((T) enumOrOrdinal).ordinal();

            }
            return new EnumConfigEntryHandler<T>(id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrdinal, ((T) defaultEnum).ordinal(), enu);
        };
    }
}
