package com.monglife.mongs.application.mong.port.in;

import com.monglife.mongs.application.mong.port.in.command.*;
import com.monglife.mongs.domain.mong.model.*;

import java.util.List;

public interface InteractionUseCase {

    List<Food> getFoodsUseCase(GetFoodsCommand command);

    List<Snack> getSnacksUseCase(GetSnacksCommand command);

    Mong feedFoodUseCase(FeedFoodCommand command);

    Mong feedSnackUseCase(FeedSnackCommand command);

    List<Inventory> getInventoriesUseCase(GetInventoriesCommand command);

    Mong useInventoryUseCase(UseInventoryCommand command);

    Mong buyRandomDrawTicketUseCase(BuyRandomDrawTicketCommand command);

    RandomDraw randomDrawUseCase(RandomDrawCommand command);
}
