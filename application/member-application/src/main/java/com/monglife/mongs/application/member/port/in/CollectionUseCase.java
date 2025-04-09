package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.domain.*;
import com.monglife.mongs.application.member.port.command.*;

import java.util.List;

public interface CollectionUseCase {

    void createCollectionMapUseCase(CreateCollectionMapCommand createCollectionMapCommand);

    void createCollectionMongUseCase(CreateCollectionMongCommand createCollectionMongCommand);

    List<CollectionMap> getCollectionMapsUseCase(GetCollectionMapsCommand getCollectionMapsCommand);

    List<CollectionMong> getCollectionMongsUseCase(GetCollectionMongsCommand getCollectionMongsCommand);
}
