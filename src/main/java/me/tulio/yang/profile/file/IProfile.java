package me.tulio.yang.profile.file;

import me.tulio.yang.profile.Profile;

public interface IProfile {

    void save(Profile profile);

    void load(Profile profile);
}
