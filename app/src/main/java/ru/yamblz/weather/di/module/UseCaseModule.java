package ru.yamblz.weather.di.module;


import dagger.Module;
import dagger.Provides;
import ru.yamblz.weather.data.usecase.OverviewUseCase;
import ru.yamblz.weather.data.usecase.OverviewUseCaseImpl;

@Module
public class UseCaseModule {

    @Provides
    OverviewUseCase provideOverviewUseCase(OverviewUseCaseImpl overviewUseCase) {
        return overviewUseCase;
    }
}
