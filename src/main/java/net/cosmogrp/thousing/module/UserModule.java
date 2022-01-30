package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.user.repo.BinaryUserRepository;
import net.cosmogrp.thousing.user.repo.UserRepository;

public class UserModule extends AbstractModule {

    @Override
    public void configure() {
        bind(UserRepository.class).to(BinaryUserRepository.class).singleton();
    }

}
