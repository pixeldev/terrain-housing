package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.user.repo.BinaryUserRepository;
import net.cosmogrp.thousing.user.repo.UserRepository;
import net.cosmogrp.thousing.user.service.SimpleUserService;
import net.cosmogrp.thousing.user.service.UserService;

public class UserModule extends AbstractModule {

    @Override
    public void configure() {
        bind(UserRepository.class).to(BinaryUserRepository.class).singleton();
        bind(UserService.class).to(SimpleUserService.class).singleton();
    }

}
