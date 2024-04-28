package me.tulio.yang.profile.deatheffects;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.ClassHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class DeathEffect {

    @Getter public static List<Data> deathEffects = Lists.newArrayList();

    public static void init() {
        for (Class<?> aClass : ClassHelper.getClassesInPackage("me.tulio.yang.profile.deatheffects.impl")) {
            try {
                deathEffects.add((Data) aClass.getConstructor(Profile.class).newInstance(new Profile(UUID.randomUUID())));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (NoSuchMethodException ignored) {}
        }
    }

    public static Data getByNameAndProfile(String name, Profile profile) {
        for (Data deathEffect : deathEffects) {
            if (deathEffect.getClass().getSimpleName().equals(name)) {
                try {
                    return (Data) Class.forName("me.tulio.yang.profile.deatheffects.impl." + name).getConstructor(Profile.class).newInstance(profile);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Death Effect " + name + " not found, please reload your profile data or report this in discord.");
                }
                catch (NoSuchMethodException ignored) {}
            }
        }
        throw new IllegalArgumentException("Death Effect " + name + " not found, please reload your profile data or report this in discord.");
    }
}
