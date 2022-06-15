.class public Ltv/twitch/android/app/consumer/TwitchApplication;
.super Ljava/lang/Object;
.source "TwitchApplication.java"


# direct methods
.method public constructor <init>()V
    .registers 1

    .prologue
    .line 5
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onCreate()V
    .registers 3

    .prologue
    .line 9
    sget-object v0, Ltv/orange/core/Logger;->INSTANCE:Ltv/orange/core/Logger;

    const-string v1, "Hello world!"

    invoke-virtual {v0, v1}, Ltv/orange/core/Logger;->debug(Ljava/lang/String;)I

    .line 10
    return-void
.end method
