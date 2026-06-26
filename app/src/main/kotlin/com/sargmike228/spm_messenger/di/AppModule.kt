package com.sargmike228.spm_messenger.di

import android.content.Context
import androidx.room.Room
import com.sargmike228.core.database.SPMDatabase
import com.sargmike228.core.database.dao.MessageDao
import com.sargmike228.core.network.BluetoothP2PManager
import com.sargmike228.core.network.DirectSocketP2PManager
import com.sargmike228.core.network.mDNSDiscoveryManager
import com.sargmike228.core.security.EncryptionManager
import com.sargmike228.core.security.RSAEncryptionManager
import com.sargmike228.core.security.SignalProtocolManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SPMDatabase {
        return Room.databaseBuilder(
            context,
            SPMDatabase::class.java,
            "spm_messenger_db"
        ).build()
    }
    
    @Singleton
    @Provides
    fun provideMessageDao(database: SPMDatabase): MessageDao {
        return database.messageDao()
    }
    
    @Singleton
    @Provides
    fun provideEncryptionManager(@ApplicationContext context: Context): EncryptionManager {
        return EncryptionManager(context)
    }
    
    @Singleton
    @Provides
    fun provideRSAEncryptionManager(): RSAEncryptionManager {
        return RSAEncryptionManager()
    }
    
    @Singleton
    @Provides
    fun provideSignalProtocolManager(): SignalProtocolManagerImpl {
        return SignalProtocolManagerImpl()
    }
    
    @Singleton
    @Provides
    fun provideDirectSocketP2PManager(): DirectSocketP2PManager {
        return DirectSocketP2PManager()
    }
    
    @Singleton
    @Provides
    fun provideBluetoothP2PManager(@ApplicationContext context: Context): BluetoothP2PManager {
        return BluetoothP2PManager(context)
    }
    
    @Singleton
    @Provides
    fun provideMDNSDiscoveryManager(@ApplicationContext context: Context): mDNSDiscoveryManager {
        return mDNSDiscoveryManager(context)
    }
}