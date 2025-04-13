package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.CreateCollectionMapCommand;
import com.monglife.mongs.application.member.port.in.command.CreateCollectionMongCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMapsCommand;
import com.monglife.mongs.application.member.port.in.command.GetCollectionMongsCommand;
import com.monglife.mongs.domain.model.CollectionMap;
import com.monglife.mongs.domain.model.CollectionMong;

import java.util.List;

public interface CollectionUseCase {

    void createCollectionMapUseCase(CreateCollectionMapCommand command);

    void createCollectionMongUseCase(CreateCollectionMongCommand command);

    List<CollectionMap> getCollectionMapsUseCase(GetCollectionMapsCommand command);

    List<CollectionMong> getCollectionMongsUseCase(GetCollectionMongsCommand command);
}
