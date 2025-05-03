import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { DatabaseModule } from './database/database.module';
import { TestModule } from './test/test.module';
import { UsersModule } from './users/users.module'; // Importa UsersModule

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }), // Restaura ConfigModule
    DatabaseModule,
    TestModule,
    UsersModule, // Añade UsersModule
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}