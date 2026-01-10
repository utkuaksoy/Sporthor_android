package com.iamkurtgoz.core.connectivity;

import android.app.Application;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class NetworkConnectivityObserver_Factory implements Factory<NetworkConnectivityObserver> {
  private final Provider<Application> applicationProvider;

  private NetworkConnectivityObserver_Factory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public NetworkConnectivityObserver get() {
    return newInstance(applicationProvider.get());
  }

  public static NetworkConnectivityObserver_Factory create(
      Provider<Application> applicationProvider) {
    return new NetworkConnectivityObserver_Factory(applicationProvider);
  }

  public static NetworkConnectivityObserver newInstance(Application application) {
    return new NetworkConnectivityObserver(application);
  }
}
