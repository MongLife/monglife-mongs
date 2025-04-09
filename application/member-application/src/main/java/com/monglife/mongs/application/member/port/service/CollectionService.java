package com.monglife.mongs.application.member.port.service;

import com.monglife.mongs.application.member.domain.CollectionMap;
import com.monglife.mongs.application.member.domain.CollectionMong;
import com.monglife.mongs.application.member.port.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.command.CreateCollectionMongCommand;
import com.monglife.mongs.application.member.port.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.command.GetCollectionMongsCommand;
import com.monglife.mongs.application.member.port.in.CollectionUseCase;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService implements CollectionUseCase {

    private final MemberPersistencePort memberPersistencePort;

    @Override
    public void createCollectionMapUseCase(CreateCollectionMapCommand createCollectionMapCommand) {

    }

    @Override
    public void createCollectionMongUseCase(CreateCollectionMongCommand createCollectionMongCommand) {

    }

    @Override
    public List<CollectionMap> getCollectionMapsUseCase(GetCollectionMapsCommand getCollectionMapsCommand) {
        return List.of();
    }

    @Override
    public List<CollectionMong> getCollectionMongsUseCase(GetCollectionMongsCommand getCollectionMongsCommand) {
        return List.of();
    }
}
