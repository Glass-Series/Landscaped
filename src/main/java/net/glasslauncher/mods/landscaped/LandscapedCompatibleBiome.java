package net.glasslauncher.mods.landscaped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// You may wonder why I've taken this route. Simple:
// Vanilla biome code is a fucking train wreck
// And stapi's biome """API""" does fuck all to help it
// And some folk likely don't want to be forced to make landscaped a runtime dependency
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LandscapedCompatibleBiome {}
